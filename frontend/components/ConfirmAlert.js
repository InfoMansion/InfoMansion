import React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';

export default function ConfirmAlert({ open, setOpen, content, setAgree }) {
  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
      >
        <DialogTitle id="alert-dialog-title">{content}</DialogTitle>
        <DialogActions>
          <Button onClick={handleClose} color="error">
            취소
          </Button>
          <Button
            onClick={() => {
              setAgree(true);
              setOpen(false);
            }}
            color="error"
          >
            구매
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
