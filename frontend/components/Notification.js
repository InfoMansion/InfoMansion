import React from 'react';
import {
  Dialog,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';

export default function Notification({ notification }) {
  const TOSTRING = {
    LP: '님이 회원님의 게시글을 좋아합니다.',
    FU: '님이 회원님을 팔로우하기 시작했습니다.',
    GB: '님이 회원님에게 방명록을 남겼습니다.',
  };
  return (
    <>
      <div
        onClick={() => window.location.replace(`/${notification.fromUsername}`)}
        style={{
          display: 'grid',
          alignItems: 'center',
          margin: '0 10px',
          cursor: 'pointer',
        }}
      >
        {/* <img
          src={user.profileImage}
          style={{ height: '80px', width: '80px', borderRadius: '50%' }}
        ></img> */}
        <div style={{ fontWeight: '400', fontSize: '15px' }}>
          {notification.fromUsername}
          {TOSTRING[notification.ntype]}
        </div>
      </div>
    </>
  );
}
