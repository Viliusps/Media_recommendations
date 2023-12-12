import { Modal, Box, Paper } from '@mui/material';
import styled from 'styled-components';
import CustomCard from './CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';
import spotifyImage from '../images/spotify.png';
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

export default function SelectionModal({ type, handleClose, open, handleOpen, setType, role }) {
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
            Recommend me a <b>{type}</b> based on...
          </h2>

          <StyledPaper>
            <CustomCard
              title="A movie"
              image={movieImage}
              handleOpen={() => {
                setType('Movie');
                handleOpen();
              }}
              cardHeight={350}
              cardWidth={300}
            />
            <CustomCard
              title="A song"
              image={songImage}
              handleOpen={() => {
                setType('Song');
                handleOpen();
              }}
              cardHeight={350}
              cardWidth={300}
            />
            {role !== 'GUEST' && (
              <CustomCard
                title="My Spotify history"
                image={spotifyImage}
                handleOpen={() => Navigate('/playlistRecommendation')}
                cardHeight={350}
                cardWidth={300}
              />
            )}
          </StyledPaper>
        </div>
      </StyledBox>
    </Modal>
  );
}
