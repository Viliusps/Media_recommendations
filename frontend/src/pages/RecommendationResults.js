import { useParams } from 'react-router-dom';
import { rateRecommendation, recommend, neuralRecommend } from '../api/recommendation-axios';
import RatingModal from '../components/RatingModal';
import { useEffect, useState } from 'react';
import LoadingWrapper from '../components/LoadingWrapper';
import RecommendationResultDisplay from '../components/RecommendationResultDisplay';
import ObjectFeatures from '../components/ObjectFeatures';
import { Button, Card, Grid, GridItem, Heading, Stack, useColorModeValue } from '@chakra-ui/react';

const RecommendationResults = () => {
  const params = useParams();
  const { recommendingType, recommendingBy, recommendingByType, recommendingByID } = params;
  const [recommendation, setRecommendation] = useState(null);
  const [neuralRecommendation, setNeuralRecommendation] = useState(null);
  const [originalRequest, setOriginalRequest] = useState(null);
  const [loadingGPT, setLoadingGPT] = useState(false);
  const [loadingNeural, setLoadingNeural] = useState(false);
  const [error, setError] = useState(false);
  const [openRating, setOpenRating] = useState(false);

  useEffect(() => {
    setLoadingGPT(true);
    setLoadingNeural(true);
    console.log(recommendingType, recommendingByType, recommendingBy, recommendingByID);
    recommend(recommendingType, recommendingByType, recommendingBy, recommendingByID)
      .then((result) => {
        console.log(result);
        if (result.type == 'Movie') setRecommendation(result.movie);
        else if (result.type == 'Song') setRecommendation(result.song);
        else if (result.type == 'Game') setRecommendation(result.game);

        if (result.originalType == 'Movie') setOriginalRequest(result.originalMovie);
        else if (result.originalType == 'Song') setOriginalRequest(result.originalSong);
        else if (result.originalType == 'Game') setOriginalRequest(result.originalGame);
      })
      .catch((error) => {
        console.error(error);
        setError(true);
      })
      .finally(() => {
        setLoadingGPT(false);
      });
    neuralRecommend(recommendingType, recommendingByType, recommendingBy, recommendingByID)
      .then((result) => {
        console.log(result);
        if (result.type == 'Movie') setNeuralRecommendation(result.movie);
        else if (result.type == 'Song') setNeuralRecommendation(result.song);
        else if (result.type == 'Game') setNeuralRecommendation(result.game);
      })
      .catch((error) => {
        console.error(error);
        setError(true);
      })
      .finally(() => {
        setLoadingNeural(false);
      });
  }, []);

  const handleOpenRating = () => setOpenRating(true);
  const handleCloseRating = () => setOpenRating(false);

  const handleRatingClick = (rating) => {
    rateRecommendation(
      recommendingType,
      recommendingByType,
      recommendation,
      originalRequest,
      rating
    ).then(() => {
      console.log('DONE');
    });
  };

  return (
    <>
      <Stack>
        {recommendingByType === 'Spotify' && (
          <Heading>
            Recommending a {recommendingType.toLowerCase()} based on your Spotify playlist.
          </Heading>
        )}
        {recommendingByType === 'Steam' && (
          <Heading>
            Recommending a {recommendingType.toLowerCase()} based on your Steam playlist.
          </Heading>
        )}
        {(recommendingByType === 'Game' ||
          recommendingByType === 'Movie' ||
          recommendingByType === 'Song') && (
          <Heading>
            Recommending a {recommendingType.toLowerCase()} based on a{' '}
            {recommendingByType.toLowerCase()}.
          </Heading>
        )}
      </Stack>
      <Grid marginTop={2} templateColumns="repeat(2, 1fr)" gap={6}>
        <GridItem colSpan={2}>
          <Card>
            <Heading as="h3" size="md">
              Details analyzed by the model:
            </Heading>
            <ObjectFeatures object={originalRequest} type={recommendingByType} />
          </Card>
        </GridItem>

        <GridItem colSpan={1}>
          <Card>
            <Heading as="h3" size="md">
              ChatGPT recommendation
            </Heading>
            <LoadingWrapper loading={loadingGPT} error={error}>
              <RecommendationResultDisplay
                recommendation={recommendation}
                recommendingType={recommendingType}
              />
            </LoadingWrapper>
          </Card>
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
            onClick={() => handleOpenRating()}>
            Rate the recommendation.
          </Button>
        </GridItem>
        <GridItem colSpan={1}>
          <Card>
            <Heading as="h3" size="md">
              Neural model recommendation
            </Heading>
            <LoadingWrapper loading={loadingNeural} error={error}>
              <RecommendationResultDisplay
                recommendation={neuralRecommendation}
                recommendingType={recommendingType}
              />
            </LoadingWrapper>
          </Card>
        </GridItem>
      </Grid>
      <RatingModal
        handleClose={handleCloseRating}
        open={openRating}
        handleClick={handleRatingClick}
      />
    </>
  );
};

export default RecommendationResults;
