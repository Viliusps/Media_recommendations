import { Typography, Modal, Box, TextField, Autocomplete, MenuItem, Button } from '@mui/material';
import styled from 'styled-components';

const StyledBox = styled(Box)`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400;
  background-color: white;
  padding: 20px;
  border-radius: 5px;
`;

export default function RecommendationModal({
  handleClose,
  handleClick,
  open,
  setSelection,
  suggestions,
  type,
  recommendBy,
  selection
}) {
  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description">
      <StyledBox>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          Recommend me a {type.toLowerCase()} based on a {recommendBy.toLowerCase()}
        </Typography>
        <Typography>
          Please select from the list, or enter a new {recommendBy.toLowerCase()}
        </Typography>

        <Autocomplete
          value={selection}
          onChange={(event, newValue) => setSelection(newValue)}
          options={suggestions}
          getOptionLabel={(option) => option.title ?? ''}
          renderInput={(params) => (
            <TextField
              {...params}
              label={`Search for a ${recommendBy.toLowerCase()}`}
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
  );
}
