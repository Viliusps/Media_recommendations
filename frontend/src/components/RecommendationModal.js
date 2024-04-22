import {
  Modal,
  Text,
  Button,
  Input,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalBody,
  useColorModeValue,
  ModalCloseButton,
  Select
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import { getSongSuggestions } from '../api/songs-axios';
import { getMovieSuggestions } from '../api/movies-axios';
import { getGameSuggestions } from '../api/games-axios';

export default function RecommendationModal({
  handleClose,
  handleClick,
  open,
  setSelection,
  type,
  recommendBy
}) {
  const [suggestions, setSuggestions] = useState([]);
  const [enteredName, setEnteredName] = useState('');
  const [foundErrorLabel, setFoundErrorLabel] = useState('');
  const getSuggestions = () => {
    setFoundErrorLabel('');
    setSuggestions([]);
    if (enteredName === '' || enteredName == null) setFoundErrorLabel('Please enter a name');
    else {
      if (recommendBy === 'Song') {
        getSongSuggestions(enteredName).then((response) => {
          if (response.length === 0) setFoundErrorLabel('No songs found.');
          else {
            setFoundErrorLabel('');
            setSuggestions(response);
            setSelection(response[0]);
          }
        });
      } else if (recommendBy === 'Movie') {
        getMovieSuggestions(enteredName).then((response) => {
          if (response.length === 0) setFoundErrorLabel('No movies found.');
          else {
            setFoundErrorLabel('');
            setSuggestions(response);
            setSelection(response[0]);
          }
        });
      } else if (recommendBy === 'Game') {
        getGameSuggestions(enteredName).then((response) => {
          if (response.length === 0) setFoundErrorLabel('No games found.');
          else {
            setFoundErrorLabel('');
            setSuggestions(response);
            setSelection(response[0]);
          }
        });
      }
    }
  };
  useEffect(() => {}, []);

  const handleSelectionChange = (event) => {
    console.log('Event target value: ' + event.target.value);
    const index = parseInt(event.target.value, 10);
    console.log('Setting selected option: ');
    console.log(suggestions[index]);
    setSelection(suggestions[index]);
  };

  return (
    <Modal
      isOpen={open}
      onClose={() => {
        setSuggestions([]);
        handleClose();
      }}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>
          Recommend me a <b>{type.toLowerCase()}</b> based on a <b>{recommendBy.toLowerCase()}.</b>
        </ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Text>Please enter the name of the {recommendBy.toLowerCase()}.</Text>
          <Input onChange={(event) => setEnteredName(event.target.value)} />
          <Text style={{ color: 'red' }}>{foundErrorLabel}</Text>
          <Button
            marginTop={2}
            px={8}
            bg={useColorModeValue('#151f21', 'gray.900')}
            color={'white'}
            rounded={'md'}
            _hover={{
              transform: 'translateY(-2px)',
              boxShadow: 'lg'
            }}
            onClick={() => getSuggestions()}>
            Continue
          </Button>
          {suggestions.length > 0 && (
            <>
              <Text marginTop={5}>Please specify your {recommendBy.toLowerCase()}.</Text>
              <Select onChange={handleSelectionChange}>
                {suggestions.map((suggestion, index) => (
                  <option key={index} value={index}>
                    {recommendBy === 'Song' && `"${suggestion.title}" by ${suggestion.singer}`}
                    {recommendBy === 'Movie' && ` "${suggestion.title}" (${suggestion.year})`}
                    {recommendBy === 'Game' && ` "${suggestion.name}" (${suggestion.releaseDate})`}
                  </option>
                ))}
              </Select>
              <Button
                marginTop={2}
                px={8}
                bg={useColorModeValue('#151f21', 'gray.900')}
                color={'white'}
                rounded={'md'}
                _hover={{
                  transform: 'translateY(-2px)',
                  boxShadow: 'lg'
                }}
                onClick={() => handleClick(suggestions)}>
                Recommend!
              </Button>
            </>
          )}
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
