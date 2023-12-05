import { styled, Modal, Box, Paper } from '@mui/material';
import CustomCard from './CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';
import { useNavigate } from 'react-router-dom';

const StyledBox = styled(Box)`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 10px;
  border-radius: 5px;
`;

const StyledPaper = styled(Paper)`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
`;

export default function SelectionModal({ type, handleClose, open, handleOpen, setType }) {
  const Navigate = useNavigate();
  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description">
      <StyledBox>
        <div>
          <h2>
            Recommend me a <b>{type}</b> based on a...
          </h2>

          <StyledPaper>
            <CustomCard
              title="Movie"
              image={movieImage}
              handleOpen={() => {
                setType('Movie');
                handleOpen();
              }}
              cardHeight={350}
              cardWidth={300}
            />
            <CustomCard
              title="Song"
              image={songImage}
              handleOpen={() => {
                setType('Song');
                handleOpen();
              }}
              cardHeight={350}
              cardWidth={300}
            />
            <button
              onClick={() => {
                Navigate('/playlistRecommendation');
              }}>
              My spotify history
            </button>
          </StyledPaper>
        </div>
      </StyledBox>
    </Modal>
  );
}
