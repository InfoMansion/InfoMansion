import React, { useState, useEffect } from 'react';
import { useCookies } from 'react-cookie';
import axios from '../utils/axios';

import { IconButton } from '@mui/material';
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';
import PersonRemoveAlt1Icon from '@mui/icons-material/PersonRemoveAlt1';

export default function Follow({ isFollow, username }) {
  const [follow, setFollow] = useState(isFollow);
  const [cookies] = useCookies(['cookie-name']);

  useEffect(() => {
    setFollow(isFollow);
  }, [isFollow]);

  const postFollow = async () => {
    try {
      await axios.post(
        `/api/v1/follow/${username}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        },
      );
      setFollow(prev => !prev);
      alert('팔로우');
    } catch (e) {
      console.log('error', e);
    }
  };

  const postUnFollow = async () => {
    try {
      await axios.delete(`/api/v1/follow/${username}`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      });
      setFollow(prev => !prev);
      alert('팔로우 취소');
    } catch (e) {
      console.log('error', e);
    }
  };

  return follow !== undefined ? (
    <div>
      <IconButton
        onClick={() => {
          follow ? postUnFollow() : postFollow();
        }}
      >
        {!follow ? (
          <PersonAddAlt1Icon color="primary" />
        ) : (
          <PersonRemoveAlt1Icon />
        )}
      </IconButton>
    </div>
  ) : null;
}
