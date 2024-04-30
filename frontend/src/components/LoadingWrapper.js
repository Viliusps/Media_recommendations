import React from 'react';
import { CircularProgress, Alert, AlertIcon, AlertTitle, AlertDescription } from '@chakra-ui/react';

export default function LoadingWrapper(children) {
  if (children.loading)
    return (
      <div
        style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
        <CircularProgress isIndeterminate />
      </div>
    );
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
