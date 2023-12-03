import { useState } from 'react';
import {
  Paper,
  styled,
  Typography,
  Modal,
  Box,
  TextField,
  Autocomplete,
  MenuItem,
  Button
} from '@mui/material';
import CustomCard from '../components/CustomCard';
import movieImage from '../images/movie.png';
import songImage from '../images/song.png';
import { getSongs } from '../api/songs-axios';
import { getMovies, getOmdbMovie } from '../api/movies-axios';

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
  const [selection, setSelection] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [type, setType] = useState('');

  const handleOpen = (calledBy) => {
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
    setOpen(true);
  };
  const handleClose = () => setOpen(false);

  const handleClick = () => {
    //console.log(selection);
    getOmdbMovie(selection.title).then((data) => {
      console.log(data);
    });
  };

  return (
    <>
      <div>
        <h1>Recommend me a...</h1>

        <StyledPaper sx={{ height: '100vh' }}>
          <CustomCard title="Movie" image={movieImage} handleOpen={() => handleOpen('Movie')} />
          <CustomCard title="Song" image={songImage} handleOpen={() => handleOpen('Song')} />
        </StyledPaper>
      </div>

      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <StyledBox>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Please select from the list, or enter a new {type.toLowerCase()}
          </Typography>

          <Autocomplete
            value={selection}
            onChange={(event, newValue) => setSelection(newValue)}
            options={suggestions}
            getOptionLabel={(option) => option.title ?? ''}
            renderInput={(params) => (
              <TextField
                {...params}
                label={`Search for a ${type.toLowerCase()}`}
                variant="outlined"
              />
            )}
            renderOption={(props, option) => (
              <MenuItem {...props} key={option.title}>
                {option.title}
              </MenuItem>
            )}
          />
          <Button onClick={() => handleClick()}>Recommend!</Button>
        </StyledBox>
      </Modal>
    </>
  );
}
