import { Modal, Button, Box } from '@mui/material';
import styled from 'styled-components';

const Cancel = styled(Button)`
  margin: 0;
  float: left;
  margin-left: 10px;
  position: relative;
  -ms-transform: translate(-50%, -50%);
  margin-bottom: 25px;
  margin-top: 10px;
`;

const Approve = styled(Button)`
  margin: 0;
  float: right;
  margin-right: 10px;
  position: relative;
  -ms-transform: translate(-50%, -50%);
  margin-bottom: 25px;
  margin-top: 10px;
`;

const StyledBox = styled(Box)`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 10px;
  border-radius: 5px;
`;

export default function ConfirmationModal({ open, user, handleDelete, handleClose }) {
  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description">
      <StyledBox>
        <h2 style={{ textAlign: 'center' }}>
          Are you sure you want to delete
          <br />
          &quot;{user.username}&quot;?
        </h2>
        <div>
          <Cancel onClick={() => handleClose()}>Cancel</Cancel>
          <Approve onClick={() => handleDelete()}>Confirm</Approve>
        </div>
      </StyledBox>
    </Modal>
  );
}
