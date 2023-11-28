import { useState } from 'react';
import { Paper, styled, Typography, Modal, Box } from '@mui/material';
import CustomCard from '../components/CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';

const StyledPaper = styled(Paper)`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
`;

const StyledBox = styled(Box)`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400;
  background-color: white;
  border: 2px solid #000;
  boxshadow: 24;
  p: 4;
`;

export default function Recommendation() {
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <>
      <div>
        <h1>Recommend me a...</h1>

        <StyledPaper sx={{ height: '100vh' }}>
          <CustomCard title="Movie" image={movieImage} handleOpen={handleOpen} />
          <CustomCard title="Song" image={songImage} handleOpen={handleOpen} />
        </StyledPaper>
      </div>

      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <StyledBox>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Text in a modal
          </Typography>
          <Typography id="modal-modal-description" sx={{ mt: 2 }}>
            Duis mollis, est non commodo luctus, nisi erat porttitor ligula.
          </Typography>
        </StyledBox>
      </Modal>
    </>
  );
}
