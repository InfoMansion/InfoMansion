import React from 'react';
import Router from 'next/router';

import {
  Avatar,
  Box,
  Card,
  Divider,
  formControlLabelClasses,
  Grid,
  Typography,
} from '@mui/material';

export default function Profile({ user }) {
  console.log(user);
  return (
    <>
      <div
        onClick={() => window.location.replace(`/${user.username}`)}
        style={{
          height: '100px',
          display: 'grid',
          gridTemplateColumns: '1fr 3fr',
          alignItems: 'center',
          margin: '0 10px',
          cursor: 'pointer',
          borderRadius: '6px',
          backgroundColor: 'rgba(255,255,255,0.8)',
          width: '100%',
        }}
      >
        <img
          src={user.profileImage}
          style={{
            height: '80px',
            width: '80px',
            borderRadius: '50%',
            marginLeft: '10px',
          }}
        ></img>
        <div
          style={{ fontWeight: '700', fontSize: '25px', marginLeft: '20px' }}
        >
          {user.username}
        </div>
      </div>
    </>
  );
}
