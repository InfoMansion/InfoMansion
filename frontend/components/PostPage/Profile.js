import React from 'react';
import Router from 'next/router';

import { Card } from '@mui/material';

export default function Profile({ user, width }) {
  console.log('profile: ', user);
  return (
    <>
      <Card
        sx={{ width: width, paddingLeft: '15px' }}
        onClick={() => window.location.replace(`/${user.username}`)}
      >
        <div
          style={{
            minHeight: '0px',
            height: '80px',
            maxHeight: '100px',
            display: 'grid',
            gridTemplateColumns: '1fr 3fr',
            alignItems: 'center',
          }}
        >
          <img
            src={user.profileImage}
            style={{ height: '80px', width: '80px', borderRadius: '50%' }}
          ></img>
          <div style={{ fontWeight: '600', fontSize: '25px' }}>
            {user.username}
          </div>
        </div>
      </Card>
    </>
  );
}
