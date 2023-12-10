import { useParams } from 'react-router-dom';
export default function RecommendationFromChoice() {
  const params = useParams();
  const { recommendingType, recommendingBy, recommendingByType } = params;
  return (
    <div>
      <h1>Recommending a: {recommendingType}</h1>
      <h1>
        Recommending by: {recommendingBy} which is a {recommendingByType}
      </h1>
    </div>
  );
}
