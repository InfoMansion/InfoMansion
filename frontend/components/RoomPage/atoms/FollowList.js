import React from 'react';
import { Dialog, IconButton } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';

import Profile from '../../PostPage/Profile';
export default function FollowList({ modalInfo, handleModalClose }) {
  console.log('modalinfo : ', modalInfo);
  return (
    <>
      <Dialog
        open={true}
        onClose={handleModalClose}
        sx={{
          maxWidth: undefined,
          maxHeight: undefined,
          left: '40%',
          top: '30%',
          width: '400px',
          height: '300px',
          minHeight: '200px',
          display: 'grid',
          gridTemplateRows: '50px 1fr',
          borderRadius: '6px',
        }}
      >
        <div
          style={{
            alignItems: 'center',
            borderBottom: '1px solid #E0E0E0',
            display: 'flex',
            justifyContent: 'space-between',
          }}
        >
          <div></div>
          <div style={{ fontWeight: '700', padding: 'auto' }}>
            {modalInfo.title}
          </div>
          <IconButton onClick={handleModalClose}>
            <CloseIcon />
          </IconButton>
        </div>
        <div>
          {modalInfo.data.map(user => (
            <Profile user={user} width={400} />
          ))}
        </div>
      </Dialog>
    </>
  );
}
