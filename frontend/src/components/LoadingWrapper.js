import React from 'react';
import { CircularProgress, Alert, AlertIcon, AlertTitle, AlertDescription } from '@chakra-ui/react';

export default function LoadingWrapper(children) {
  if (children.loading) return <CircularProgress isIndeterminate />;
  else if (children.error)
    return (
      <Alert status="error">
        <AlertIcon />
        <AlertTitle>Server error!</AlertTitle>
        <AlertDescription>Please try again later.</AlertDescription>
      </Alert>
    );
  return <>{children.children}</>;
}
