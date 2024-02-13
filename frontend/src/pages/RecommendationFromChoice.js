import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import { recommend } from '../api/recommendation-axios';
import { useEffect, useState } from 'react';
import LoadingWrapper from '../components/LoadingWrapper';

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

  useEffect(() => {
    setLoading(true);
    recommend(recommendingType, recommendingByType, recommendingBy)
      .then((result) => {
        setRecommendation(result);
      })
      .catch((error) => {
        console.error(error);
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

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
                {recommendingType === 'Song' && recommendation.id.length > 22 ? (
                  <RecommendationText>{recommendation.id}</RecommendationText>
                ) : (
                  recommendingType === 'Song' && (
                    <RecommendationText
                      onClick={() => {
                        const spotifyUri = `spotify:track:${recommendation.id}`;
                        window.location.href = spotifyUri;
                      }}>
                      Click here!
                    </RecommendationText>
                  )
                )}
                {recommendingType === 'Movie' && recommendation.id.length > 9 ? (
                  <RecommendationText>{recommendation.id}</RecommendationText>
                ) : (
                  recommendingType === 'Movie' && (
                    <RecommendationText
                      onClick={() =>
                        window.open(`https://www.imdb.com/title/${recommendation.id}`, '_blank')
                      }>
                      Click here!
                    </RecommendationText>
                  )
                )}
                {recommendingType === 'Game' && (
                  <RecommendationText>{recommendation.id}</RecommendationText>
                )}
              </>
            )}
          </LoadingWrapper>
        </RecommendationBox>
      </Section>
    </StyledContainer>
  );
};

export default RecommendationFromChoice;
