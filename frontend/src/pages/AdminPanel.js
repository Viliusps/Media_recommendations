import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { getUsers, deleteUser, updateUser } from '../api/users-axios';
import LoadingWrapper from '../components/LoadingWrapper';
import UserModal from '../components/UserModal';
import ConfirmationModal from '../components/ConfirmationModal';
import { toast } from 'react-toastify';
import { Button } from '@mui/material';

const UserContainer = styled.div`
  width: 33%;
  border: 1px solid #ccc;
  padding: 10px;
  margin: 10px auto 10px auto;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const Username = styled.h3`
  margin: 0;
`;

const ActionButtons = styled.div`
  display: flex;
  gap: 10px;
`;

const DeleteButton = styled(Button)`
  && {
    background-color: red;
    color: white;

    &:hover {
      background-color: darkred;
    }
  }
`;

export default function AdminPanel() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);
  const [open, setOpen] = useState(false);
  const [openConfirmation, setOpenConfirmation] = useState(false);
  const [editUser, setEditUser] = useState(null);
  const [toDeleteUser, setDeleteUser] = useState(null);

  const handleOpen = (user) => {
    setEditUser(user);
    setOpen(true);
  };
  const handleClose = () => setOpen(false);

  const handleOpenConfirmation = (user) => {
    setDeleteUser(user);
    setOpenConfirmation(true);
  };

  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
  };

  const handleDelete = () => {
    deleteUser(toDeleteUser.id).then(() => {
      getUsers().then((results) => {
        setUsers(results);
        showToastMessage();
      });
    });
    handleCloseConfirmation();
  };

  const handleUpdate = (index, username, email, role) => {
    updateUser(index, username, email, role, editUser.password).then(() => {
      getUsers().then((results) => {
        setUsers(results);
        showUpdateMessage();
      });
    });
    handleClose();
  };

  useEffect(() => {
    setLoading(true);
    getUsers()
      .then((results) => {
        setUsers(results);
      })
      .catch((error) => {
        setError(true);
        console.error(error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  const showToastMessage = () => {
    toast.success('Successfully deleted!', {
      position: 'top-center',
      autoClose: 1500,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: false,
      progress: undefined,
      theme: 'light'
    });
  };

  const showUpdateMessage = () => {
    toast.success('Details updated successfully!', {
      position: 'top-center',
      autoClose: 1500,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: false,
      draggable: false,
      progress: undefined,
      theme: 'light'
    });
  };

  return (
    <LoadingWrapper loading={loading} error={error}>
      <h1>User list</h1>
      {users.map((user) => (
        <UserContainer key={user.id}>
          <Username>{user.username}</Username>
          <ActionButtons>
            <Button
              variant="contained"
              onClick={() => {
                handleOpen(user);
              }}>
              Edit
            </Button>
            <DeleteButton
              variant="contained"
              onClick={() => {
                handleOpenConfirmation(user);
              }}>
              Delete
            </DeleteButton>
          </ActionButtons>
        </UserContainer>
      ))}
      {toDeleteUser && (
        <ConfirmationModal
          open={openConfirmation}
          user={toDeleteUser}
          handleDelete={handleDelete}
          handleClose={handleCloseConfirmation}
        />
      )}
      {editUser && (
        <UserModal
          user={editUser}
          open={open}
          handleClose={handleClose}
          handleUpdate={handleUpdate}
        />
      )}
    </LoadingWrapper>
  );
}
