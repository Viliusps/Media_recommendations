import { Typography, Modal, Box, IconButton } from '@mui/material';
import styled from 'styled-components';
import { IconThumbDown, IconThumbUp } from '@tabler/icons-react';

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

const IconsContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const StyledIconButton = styled(IconButton)`
  &:hover {
    svg {
      fill: ${(props) => props.$hoverColor};
    }
  }
`;

export default function RatingModal({ handleClose, open, handleClick }) {
  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description">
      <StyledBox>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          Please rate the ChatGPT recommendation.
        </Typography>
        <IconsContainer>
          <StyledIconButton $hoverColor="green" onClick={() => handleClick(1)}>
            <IconThumbUp />
          </StyledIconButton>
          <StyledIconButton $hoverColor="red" onClick={() => handleClick(0)}>
            <IconThumbDown />
          </StyledIconButton>
        </IconsContainer>
      </StyledBox>
    </Modal>
  );
}
