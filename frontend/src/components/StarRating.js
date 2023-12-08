import React from 'react';

const StarRating = ({ rating, onRatingChange }) => {
  const handleStarClick = (newRating) => {
    onRatingChange(newRating);
  };

  return (
    <div>
      {[1, 2, 3, 4, 5].map((star) => (
        <svg
          key={star}
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          width="24"
          height="24"
          onClick={() => handleStarClick(star)}
          style={{ cursor: 'pointer', fill: star <= rating ? '#FFD700' : '#BDBDBD' }}>
          <path
            d="M12 2l2.4 7.2h7.6l-6 4.8 2.4 7.2-6-4.8-6 4.8 2.4-7.2-6-4.8h7.6z"
            fillRule="evenodd"
          />
        </svg>
      ))}
    </div>
  );
};

export default StarRating;
