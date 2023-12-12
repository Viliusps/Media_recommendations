import { useEffect, useState } from 'react';
import { Paper } from '@mui/material';
import styled from 'styled-components';
import CustomCard from '../components/CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';
import RecommendationModal from '../components/RecommendationModal';
import SelectionModal from '../components/SelectionModal';
import { useNavigate } from 'react-router-dom';
import { checkIfMovieExists } from '../api/movies-axios';
import { checkIfSongExists } from '../api/songs-axios';
import { getRole } from '../api/auth-axios';

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
  const [type, setType] = useState('');
  const [recommendBy, setRecommendBy] = useState('');
  const navigate = useNavigate();
  const [errorLabel, setErrorLabel] = useState('');
  const [role, setRole] = useState('');

  useEffect(() => {
    getRole().then((data) => {
      setRole(data);
    });
  }, []);

  const handleOpenSelection = (calledBy) => {
    if (calledBy === 'Movie') {
      setType('Movie');
    } else if (calledBy === 'Song') {
      setType('Song');
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
    setErrorLabel('');
    if (recommendBy === 'Movie') {
      checkIfMovieExists(selection).then((result) => {
        if (result) {
          navigate(`/choiceRecommendation/${type}/${selection}/${recommendBy}`);
        } else {
          setErrorLabel('Movie not found.');
        }
      });
    } else if (recommendBy === 'Song') {
      checkIfSongExists(selection).then((result) => {
        if (result) {
          navigate(`/choiceRecommendation/${type}/${selection}/${recommendBy}`);
        } else {
          setErrorLabel('Song not found.');
        }
      });
    }
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
        role={role}
      />
      <RecommendationModal
        handleClose={handleCloseRecommendation}
        handleClick={handleClick}
        open={openRecommendation}
        setSelection={setSelection}
        type={type}
        recommendBy={recommendBy}
        errorLabel={errorLabel}
      />
    </>
  );
}
