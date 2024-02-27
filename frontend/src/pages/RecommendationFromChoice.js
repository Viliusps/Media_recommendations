import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import { recommend } from '../api/recommendation-axios';
import RatingModal from '../components/RatingModal';
import { useEffect, useState } from 'react';
import LoadingWrapper from '../components/LoadingWrapper';
import { Button } from '@mui/material';

const StyledContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
`;

const Section = styled.div`
  margin: 20px 0;
`;

const RecommendationBox = styled.div`
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  }
`;

const RecommendationText = styled.p`
  font-size: 18px;
  font-weight: bold;
  color: #27ae60;
  cursor: pointer;
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

const RecommendationFromChoice = () => {
  const params = useParams();
  const { recommendingType, recommendingBy, recommendingByType } = params;
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const [openRating, setOpenRating] = useState(false);

  useEffect(() => {
    setLoading(true);
    recommend(recommendingType, recommendingByType, recommendingBy)
      .then((result) => {
        console.log(result);
        if (result.type == 'Movie') setRecommendation(result.movie);
        else if (result.type == 'Song') setRecommendation(result.song);
        else if (result.type == 'Game') setRecommendation(result.game);
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
    console.log(rating);
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
        <RecommendationBox>
          <StyledH2>Your Recommendation</StyledH2>
          <LoadingWrapper loading={loading} error={error}>
            {recommendation && (
              <>
                {recommendingType === 'Song' && recommendation.spotifyId.length > 22 ? (
                  <RecommendationText>{recommendation.spotifyId}</RecommendationText>
                ) : (
                  recommendingType === 'Song' && (
                    <>
                      <h3>{recommendation.title}</h3>
                      <h4>By: {recommendation.singer}</h4>
                      <RecommendationText
                        onClick={() => {
                          const spotifyUri = `spotify:track:${recommendation.spotifyId}`;
                          window.location.href = spotifyUri;
                        }}>
                        Click here!
                      </RecommendationText>
                    </>
                  )
                )}
                {recommendingType === 'Movie' && recommendation.imdbID.length > 9 ? (
                  <RecommendationText>{recommendation.imdbID}</RecommendationText>
                ) : (
                  recommendingType === 'Movie' && (
                    <>
                      <h3>{recommendation.Title}</h3>
                      <h4>By: {recommendation.Director}</h4>
                      <RecommendationText
                        onClick={() =>
                          window.open(
                            `https://www.imdb.com/title/${recommendation.imdbID}`,
                            '_blank'
                          )
                        }>
                        Click here!
                      </RecommendationText>
                    </>
                  )
                )}
                {recommendingType === 'Game' && (
                  <RecommendationText>{recommendation.name}</RecommendationText>
                )}
              </>
            )}
          </LoadingWrapper>
          <Button onClick={() => handleOpenRating()}>Rate the recommendation.</Button>
        </RecommendationBox>
      </Section>
      <RatingModal
        handleClose={handleCloseRating}
        open={openRating}
        handleClick={handleRatingClick}
      />
    </StyledContainer>
  );
};

export default RecommendationFromChoice;
