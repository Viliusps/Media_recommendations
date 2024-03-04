import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import { rateRecommendation, recommend } from '../api/recommendation-axios';
import RatingModal from '../components/RatingModal';
import { useEffect, useState } from 'react';
import LoadingWrapper from '../components/LoadingWrapper';
import { Button } from '@mui/material';
import RecommendationResultDisplay from '../components/RecommendationResultDisplay';

const StyledContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
`;

const Section = styled.div`
  margin: 20px 0;
`;

const RecommendationBox = styled.div`
  min-width: 80%;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 20px;
  margin: 0px 40px 0px 40px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  }
`;

const StyledH1 = styled.h1`
  margin-bottom: 10px;
  color: #333333;
`;

const StyledH2 = styled.h2`
  margin-bottom: 10px;
  color: #333333;
`;

const RecommendingBy = styled.p`
  font-size: 14px;
  color: #777777;
`;

const Recommendations = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
`;

const RecommendationResults = () => {
  const params = useParams();
  const { recommendingType, recommendingBy, recommendingByType } = params;
  const [recommendation, setRecommendation] = useState(null);
  const [originalRequest, setOriginalRequest] = useState();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const [openRating, setOpenRating] = useState(false);

  useEffect(() => {
    setLoading(true);
    recommend(recommendingType, recommendingByType, recommendingBy)
      .then((result) => {
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
        setLoading(false);
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
    <StyledContainer>
      <Section>
        <StyledH1>Recommending a: {recommendingType}</StyledH1>
        <RecommendingBy>
          Recommending by: {recommendingBy} which is a {recommendingByType}
        </RecommendingBy>
      </Section>
      <Section>
        <Recommendations>
          <RecommendationBox>
            <StyledH2>ChatGPT recommendation</StyledH2>
            <LoadingWrapper loading={loading} error={error}>
              <RecommendationResultDisplay
                recommendation={recommendation}
                recommendingType={recommendingType}
              />
            </LoadingWrapper>
          </RecommendationBox>
          <Button onClick={() => handleOpenRating()}>Rate the recommendation.</Button>
          <RecommendationBox>
            <StyledH2>Neural model recommendation</StyledH2>
            <LoadingWrapper loading={loading} error={error}>
              <RecommendationResultDisplay
                recommendation={recommendation}
                recommendingType={recommendingType}
              />
            </LoadingWrapper>
          </RecommendationBox>
        </Recommendations>
      </Section>
      <RatingModal
        handleClose={handleCloseRating}
        open={openRating}
        handleClick={handleRatingClick}
      />
    </StyledContainer>
  );
};

export default RecommendationResults;
