import { Typography, Modal, Box, TextField, Button } from '@mui/material';
import styled from 'styled-components';

const StyledBox = styled(Box)`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export default function RecommendationModal({
  handleClose,
  handleClick,
  open,
  setSelection,
  type,
  recommendBy
}) {
  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description">
      <StyledBox>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          Recommend me a <b>{type.toLowerCase()}</b> based on a <b>{recommendBy.toLowerCase()}.</b>
        </Typography>
        <Typography>Please enter the name of the {recommendBy.toLowerCase()}.</Typography>
        <TextField
          onChange={(event) => {
            setSelection(event.target.value);
          }}
        />
        <Button onClick={() => handleClick()}>Recommend!</Button>
      </StyledBox>
    </Modal>
  );
}
