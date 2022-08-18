import { Alert, Dialog } from '@mui/material';
import React from 'react';

export default function CustomAlert({ open, message, setOpen, severity }) {
  const handleClick = () => {
    setOpen(!open);
  };

  return (
    <>
      <Dialog open={open} onClose={handleClick}>
        <Alert severity={severity}>{message}</Alert>
      </Dialog>
    </>
  );
}
