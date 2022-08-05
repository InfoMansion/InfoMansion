import React from 'react';
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';
import { MAIN_COLOR } from '../constants';
export default function Loading() {
  return (
    <Box
      sx={{
        display: 'flex',
        position: 'absolute',
        width: '100%',
        height: '100%',
        top: '0',
        left: '0',
        background: 'rgba(255, 255, 255, 0.6)',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: '3',
        minHeight: '100vh',
      }}
    >
      <CircularProgress sx={{ color: MAIN_COLOR }} size="5rem" />
    </Box>
  );
}
