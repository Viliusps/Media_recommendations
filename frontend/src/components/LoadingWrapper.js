import React from 'react';
import { IconAlertCircle } from '@tabler/icons-react';
import { CircularProgress, Alert, styled } from '@mui/material';

const StyledLoading = styled(CircularProgress)`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: auto;
`;

const StyledAlert = styled(Alert)`
  min-width: 200px;
  max-width: 500px;
  margin: auto;
`;

export default function LoadingWrapper(children) {
  if (children.loading) return <StyledLoading visible />;
  else if (children.error)
    return (
      <StyledAlert icon={<IconAlertCircle size="1rem" />} severity="error">
        Server error. Try again later.
      </StyledAlert>
    );
  return <>{children.children}</>;
}
