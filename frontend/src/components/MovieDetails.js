import { Typography } from '@mui/material';

export default function MovieDetails({ label, value }) {
  return (
    <Typography variant="body2" color="textSecondary" gutterBottom>
      <strong>{label}:</strong>{' '}
      {Array.isArray(value) ? value.map((genre) => genre.name).join(', ') : value}
    </Typography>
  );
}
