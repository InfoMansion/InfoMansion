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

export default function Profile({ post }) {
  console.log(post);
  return (
    <>
      <Card
        sx={{ marginBottom: '10px' }}
        onClick={() => Router.push(`/${post.username}`)}
      >
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: '1fr 3fr',
            alignItems: 'center',
          }}
        >
          <img
            src={post.profileImage}
            style={{ height: '80px', width: '80px', borderRadius: '50%' }}
          ></img>
          <div>{post.username}</div>
        </div>
      </Card>
    </>
  );
}
