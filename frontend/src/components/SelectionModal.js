import CustomCard from './CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';
import gameImage from '../images/game.png';
import spotifyImage from '../images/spotify.png';
import steamImage from '../images/steam.png';
import { useNavigate } from 'react-router-dom';
import {
  Modal,
  ModalOverlay,
  Grid,
  GridItem,
  ModalHeader,
  ModalContent,
  ModalBody
} from '@chakra-ui/react';

export default function SelectionModal({ type, handleClose, open, handleOpen, setType, role }) {
  const Navigate = useNavigate();
  return (
    <Modal
      isOpen={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description">
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>
          Recommend me a <b>{type}</b> based on...
        </ModalHeader>
        <ModalBody>
          <Grid templateColumns="repeat(3, 1fr)">
            <GridItem>
              <CustomCard
                title="A movie"
                image={movieImage}
                handleOpen={() => {
                  setType('Movie');
                  handleOpen();
                }}
                cardHeight={350}
                cardWidth={300}
                zIndex={3}
              />
            </GridItem>
            <GridItem>
              <CustomCard
                title="A song"
                image={songImage}
                handleOpen={() => {
                  setType('Song');
                  handleOpen();
                }}
                cardHeight={350}
                cardWidth={300}
                zIndex={2}
              />
            </GridItem>
            <GridItem>
              <CustomCard
                title="A game"
                image={gameImage}
                handleOpen={() => {
                  setType('Game');
                  handleOpen();
                }}
                cardHeight={350}
                cardWidth={300}
                zIndex={2}
              />
            </GridItem>
            {role !== 'GUEST' && (
              <>
                <GridItem>
                  <CustomCard
                    title="My Spotify history"
                    image={spotifyImage}
                    handleOpen={() => Navigate(`/playlistRecommendation/${type}`)}
                    cardHeight={350}
                    cardWidth={300}
                    zIndex={2}
                  />
                </GridItem>
                <GridItem>
                  <CustomCard
                    title="My Steam history"
                    image={steamImage}
                    handleOpen={() => Navigate(`/gamesPlaylist/${type}`)}
                    cardHeight={350}
                    cardWidth={300}
                    zIndex={2}
                  />
                </GridItem>
              </>
            )}
          </Grid>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
