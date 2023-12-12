import React, { useState, useEffect } from 'react';
import { Modal, Box, TextField, Select, MenuItem, Button } from '@mui/material';
import styled from 'styled-components';

const StyledBox = styled(Box)`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  width: 300px;
  text-align: center;
`;

const FieldLabel = styled.p`
  font-weight: bold;
  margin: 5px 0;
`;

const StyledButton = styled(Button)`
  margin-top: 10px;
`;

export default function UserModal({ user, open, handleClose, handleUpdate }) {
  const [userData, setUserData] = useState({
    username: '',
    email: '',
    role: ''
  });

  useEffect(() => {
    setUserData({
      username: user.username,
      email: user.email,
      role: user.role.toLowerCase()
    });
  }, [user]);

  const handleFieldChange = (field, value) => {
    setUserData((prevData) => ({
      ...prevData,
      [field]: value
    }));
  };

  const handleSaveChanges = () => {
    handleUpdate(user.id, userData.username, userData.email, userData.role.toUpperCase());
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description">
      <StyledBox>
        <div>
          <h2>Edit User</h2>
          <FieldLabel>Username:</FieldLabel>
          <TextField
            value={userData.username}
            onChange={(e) => handleFieldChange('username', e.target.value)}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <FieldLabel>Email:</FieldLabel>
          <TextField
            value={userData.email}
            onChange={(e) => handleFieldChange('email', e.target.value)}
            variant="outlined"
            fullWidth
            margin="normal"
          />
          <FieldLabel>Role:</FieldLabel>
          <Select
            value={userData.role}
            onChange={(e) => handleFieldChange('role', e.target.value)}
            variant="outlined"
            fullWidth
            margin="normal">
            <MenuItem value="user">User</MenuItem>
            <MenuItem value="admin">Admin</MenuItem>
          </Select>
          <StyledButton variant="contained" color="primary" onClick={handleSaveChanges}>
            Save Changes
          </StyledButton>
        </div>
      </StyledBox>
    </Modal>
  );
}
