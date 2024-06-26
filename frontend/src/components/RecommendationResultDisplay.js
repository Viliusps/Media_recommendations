import styled from 'styled-components';
import { Text } from '@chakra-ui/react';

const RecommendationText = styled.p`
  font-size: 18px;
  font-weight: bold;
  color: #27ae60;
  cursor: pointer;
`;

export default function RecommendationResultDisplay({ recommendation, recommendingType }) {
  return (
    <>
      {recommendation ? (
        <>
          {recommendingType === 'Song' && (
            <>
              <h3>{recommendation.title}</h3>
              <h4>By: {recommendation.singer}</h4>
              {recommendation.spotifyId != null && (
                <RecommendationText
                  onClick={() => {
                    const spotifyUri = `spotify:track:${recommendation.spotifyId}`;
                    window.location.href = spotifyUri;
                  }}>
                  Click here!
                </RecommendationText>
              )}
            </>
          )}
          {recommendingType === 'Movie' &&
          recommendation.imdbID != null &&
          recommendation.imdbID.length > 9 ? (
            <RecommendationText>{recommendation.imdbID}</RecommendationText>
          ) : (
            recommendingType === 'Movie' && (
              <>
                <h3>{recommendation.Title}</h3>
                {recommendation.Director != null && <h4>By: {recommendation.Director}</h4>}
                {recommendation.imdbID != null && (
                  <RecommendationText
                    onClick={() =>
                      window.open(`https://www.imdb.com/title/${recommendation.imdbID}`, '_blank')
                    }>
                    Click here!
                  </RecommendationText>
                )}
              </>
            )
          )}
          {recommendingType === 'Game' && (
            <RecommendationText>{recommendation.name}</RecommendationText>
          )}
        </>
      ) : (
        <Text>Recommendation could not be provided.</Text>
      )}
    </>
  );
}
