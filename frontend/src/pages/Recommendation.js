import { useState } from 'react';
import { Paper, styled } from '@mui/material';
import CustomCard from '../components/CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';
import { getSongs } from '../api/songs-axios';
import { getMovies, getOmdbMovie } from '../api/movies-axios';
import RecommendationModal from '../components/RecommendationModal';
import SelectionModal from '../components/SelectionModal';

const StyledPaper = styled(Paper)`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  height: 100vh;
`;

export default function Recommendation() {
  const [openSelection, setOpenSelection] = useState(false);
  const [openRecommendation, setOpenRecommendation] = useState(false);
  const [selection, setSelection] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [type, setType] = useState('');
  const [recommendBy, setRecommendBy] = useState('');

  const handleOpenSelection = (calledBy) => {
    if (calledBy === 'Movie') {
      setType('Movie');
      getMovies().then((data) => {
        setSuggestions(data);
      });
    } else if (calledBy === 'Song') {
      setType('Song');
      getSongs().then((data) => {
        setSuggestions(data);
      });
    }
    setOpenSelection(true);
  };
  const handleCloseSelection = () => setOpenSelection(false);

  const handleOpenRecommendation = () => {
    setOpenRecommendation(true);
    setOpenSelection(false);
  };
  const handleCloseRecommendation = () => setOpenRecommendation(false);

  const handleClick = () => {
    getOmdbMovie(selection.title).then((data) => {
      console.log(data);
    });
  };

  return (
    <>
      <div>
        <h1>Recommend me a...</h1>

        <StyledPaper>
          <CustomCard
            title="Movie"
            image={movieImage}
            handleOpen={() => handleOpenSelection('Movie')}
            cardHeight={700}
            cardWidth={600}
          />
          <CustomCard
            title="Song"
            image={songImage}
            handleOpen={() => handleOpenSelection('Song')}
            cardHeight={700}
            cardWidth={600}
          />
        </StyledPaper>
      </div>
      <SelectionModal
        type={type}
        handleClose={handleCloseSelection}
        open={openSelection}
        handleOpen={handleOpenRecommendation}
        setType={setRecommendBy}
      />
      <RecommendationModal
        handleClose={handleCloseRecommendation}
        handleClick={handleClick}
        open={openRecommendation}
        setSelection={setSelection}
        suggestions={suggestions}
        type={type}
        recommendBy={recommendBy}
        selection={selection}
      />
    </>
  );
}
