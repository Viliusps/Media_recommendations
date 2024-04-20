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
import { useState } from 'react';
import { getSongSuggestions } from '../api/songs-axios';

export default function RecommendationModal({
  handleClose,
  handleClick,
  open,
  setSelection,
  type,
  recommendBy,
  errorLabel
}) {
  const [suggestions, setSuggestions] = useState([]);
  const [enteredName, setEnteredName] = useState('');
  const [songErrorLabel, setSongErrorLabel] = useState('');
  const getSuggestions = () => {
    if (recommendBy === 'Song') {
      getSongSuggestions(enteredName).then((response) => {
        if (response.length === 0) setSongErrorLabel('No songs found.');
        else {
          setSongErrorLabel('');
          setSuggestions(response);
        }
      });
    }
  };
  return (
    <Modal isOpen={open} onClose={handleClose}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>
          Recommend me a <b>{type.toLowerCase()}</b> based on a <b>{recommendBy.toLowerCase()}.</b>
        </ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Text>Please enter the name of the {recommendBy.toLowerCase()}.</Text>
          <Input
            onChange={(event) =>
              recommendBy === 'Song'
                ? setEnteredName(event.target.value)
                : setSelection(event.target.value)
            }
          />
          <Text style={{ color: 'red' }}>{songErrorLabel || errorLabel}</Text>
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
            onClick={() => (recommendBy === 'Song' ? getSuggestions() : handleClick())}>
            Continue
          </Button>
          {suggestions.length > 0 && (
            <>
              <Text marginTop={5}>Please specify your {recommendBy.toLowerCase()}.</Text>
              <Select>
                {suggestions.map((suggestion, index) => (
                  <option key={index} value={suggestion} onClick={() => setSelection(suggestion)}>
                    &quot;{suggestion.title}&quot; by {suggestion.singer}
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
                onClick={() => handleClick()}>
                Recommend!
              </Button>
            </>
          )}
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
