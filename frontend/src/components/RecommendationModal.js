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
  ModalCloseButton
} from '@chakra-ui/react';

export default function RecommendationModal({
  handleClose,
  handleClick,
  open,
  setSelection,
  type,
  recommendBy,
  errorLabel
}) {
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
            onChange={(event) => {
              setSelection(event.target.value);
            }}
          />
          <Text style={{ color: 'red' }}>{errorLabel}</Text>
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
        </ModalBody>
      </ModalContent>
    </Modal>
  );
}
