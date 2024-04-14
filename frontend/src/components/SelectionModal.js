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
  ModalBody,
  ModalCloseButton
} from '@chakra-ui/react';

export default function SelectionModal({ type, handleClose, open, handleOpen, setType, role }) {
  const Navigate = useNavigate();
  return (
    <Modal isOpen={open} onClose={handleClose}>
      <ModalOverlay />
      <ModalContent width="90%" maxWidth="1200px">
        <ModalHeader>
          Recommend me a <b>{type}</b> based on...
        </ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Grid
            templateColumns="repeat(6, 1fr)"
            templateRows={role !== 'GUEST' ? 'repeat(2, 1fr)' : 'repeat(1, 1fr)'}
            gap={1}>
            <GridItem rowSpan={1} colSpan={2}>
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
            <GridItem rowSpan={1} colSpan={2}>
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
            <GridItem rowSpan={1} colSpan={2}>
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
                <GridItem rowSpan={1} colSpan={3}>
                  <CustomCard
                    title="My Spotify history"
                    image={spotifyImage}
                    handleOpen={() => Navigate(`/playlistRecommendation/${type}`)}
                    cardHeight={350}
                    cardWidth={500}
                    zIndex={2}
                  />
                </GridItem>
                <GridItem rowSpan={1} colSpan={3}>
                  <CustomCard
                    title="My Steam history"
                    image={steamImage}
                    handleOpen={() => Navigate(`/gamesPlaylist/${type}`)}
                    cardHeight={350}
                    cardWidth={500}
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
