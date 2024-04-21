import { useEffect, useState } from 'react';
import CustomCard from '../components/CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';
import gameImage from '../images/game.png';
import RecommendationModal from '../components/RecommendationModal';
import SelectionModal from '../components/SelectionModal';
import { useNavigate } from 'react-router-dom';
import { getRole } from '../api/auth-axios';
import { Grid, GridItem, Heading } from '@chakra-ui/react';

export default function Recommendation() {
  const [openSelection, setOpenSelection] = useState(false);
  const [openRecommendation, setOpenRecommendation] = useState(false);
  const [selection, setSelection] = useState();
  const [type, setType] = useState('');
  const [recommendBy, setRecommendBy] = useState('');
  const navigate = useNavigate();
  const [role, setRole] = useState('');

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
    });
  }, []);

  const handleOpenSelection = (calledBy) => {
    console.log('clicked');
    setType(calledBy);
    setOpenSelection(true);
  };
  const handleCloseSelection = () => setOpenSelection(false);

  const handleOpenRecommendation = () => {
    setOpenRecommendation(true);
    setOpenSelection(false);
  };
  const handleCloseRecommendation = () => {
    setOpenRecommendation(false);
  };

  const handleClick = () => {
    console.log('SELECTION: ' + selection);
    if (recommendBy === 'Movie') {
      navigate(
        `/recommendationResults/${type}/${selection.title}/${recommendBy}/${selection.imdbID}`
      );
    } else if (recommendBy === 'Song') {
      navigate(
        `/recommendationResults/${type}/${selection.title}/${recommendBy}/${selection.isrc}`
      );
    } else if (recommendBy === 'Game') {
      navigate(
        `/recommendationResults/${type}/${selection.name}/${recommendBy}/${selection.rawgID}`
      );
    }
  };

  return (
    <>
      <div>
        <Heading as="h2" size="xl">
          Recommend me a...
        </Heading>

        <Grid templateColumns="repeat(3, 1fr)">
          <GridItem>
            <CustomCard
              title="Movie"
              image={movieImage}
              handleOpen={() => handleOpenSelection('Movie')}
              cardHeight={400}
              cardWidth={300}
              zIndex={1}
            />
          </GridItem>
          <GridItem>
            <CustomCard
              title="Song"
              image={songImage}
              handleOpen={() => handleOpenSelection('Song')}
              cardHeight={400}
              cardWidth={300}
              zIndex={1}
            />
          </GridItem>
          <GridItem>
            <CustomCard
              title="Game"
              image={gameImage}
              handleOpen={() => handleOpenSelection('Game')}
              cardHeight={400}
              cardWidth={300}
              zIndex={1}
            />
          </GridItem>
        </Grid>
      </div>
      <SelectionModal
        type={type}
        handleClose={handleCloseSelection}
        open={openSelection}
        handleOpen={handleOpenRecommendation}
        setType={setRecommendBy}
        role={role}
      />
      <RecommendationModal
        handleClose={handleCloseRecommendation}
        handleClick={handleClick}
        open={openRecommendation}
        setSelection={setSelection}
        type={type}
        recommendBy={recommendBy}
      />
    </>
  );
}
