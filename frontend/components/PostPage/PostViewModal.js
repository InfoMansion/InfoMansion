import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import StarBorderIcon from '@mui/icons-material/StarBorder';
import StarIcon from '@mui/icons-material/Star';
import MoveToInboxIcon from '@mui/icons-material/MoveToInbox';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import Follow from '../Follow';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import Router from 'next/router';
import { postDetailState } from '../../state/postDetailState';
import { useRecoilState } from 'recoil';

export default function PostViewModal({ post, showModal, setShowModal }) {
  const [cookies] = useCookies(['cookie-name']);
  const handleClose = () => setShowModal(false);
  const [star, setStar] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const isMenuOpen = Boolean(anchorEl);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [profileImageUrl, setProfileImageUrl] = useState(
    'https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png',
  );

  const handleMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };
  const menuId = 'primary-search-account-menu';

  const postDelete = async () => {
    const postInfo = {
      postId: post.id,
    };
    try {
      const { data } = await axios.patch(`/api/v1/posts/${post.id}`, postInfo, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      console.log(data);
    } catch (e) {
      console.log(e);
    }
  };

  const loadDetail = async () => {
    try {
      const { data } = await axios.get(`/api/v1/posts/detail/${post.id}`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      console.log(data);
      setPostDetail(data.data);
    } catch (e) {
      console.log(e);
    }
  };

  const loadProfileImage = async () => {
    try {
      const { data } = await axios.get(`/api/v1/users/info/simple`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      setProfileImageUrl(data.data.profileImage);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    loadDetail();
    loadProfileImage();
    console.log('a');
  }, [post.id]);

  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: 'bottom',
        horizontal: 'left',
      }}
      id={menuId}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'left',
      }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem
        onClick={() => {
          Router.push({
            pathname: '/post/CreatePost',
          });
        }}
      >
        글수정
      </MenuItem>

      <MenuItem onClick={postDelete}>글삭제</MenuItem>
    </Menu>
  );

  console.log(post);
  return (
    <>
      <Dialog
        open={showModal}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        maxWidth="xl"
        sx={{
          '.MuiPaper-root': {
            maxWidth: undefined,
            maxHeight: undefined,
            width: '80vw',
            height: '80vh',
            minHeight: '400px',
            display: 'grid',
            gridTemplateRows: '70px 2fr 1fr',
            borderRadius: '6px',
          },
        }}
      >
        <div
          style={{
            padding: '8px',
            borderBottom: '1px solid rgba(0, 0, 0, .2)',
          }}
        >
          <div
            style={{
              display: 'flex',
              minHeight: '0',
              height: '100%',
              alignItems: 'center',
              justifyContent: 'space-between',
            }}
          >
            <div
              style={{
                minHeight: '0',
                height: '100%',
                alignItems: 'center',
                display: 'flex',
              }}
            >
              <img
                src={profileImageUrl}
                style={{ minHeight: '0', height: '100%', marginRight: '8px' }}
              ></img>
              <div
                style={{
                  fontSize: '20px',
                  fontWeight: 'medium',
                  marginRight: '8px',
                }}
              >
                {postDetail.userName}
              </div>
              <Follow></Follow>
            </div>
            <IconButton onClick={handleClose}>
              <CloseIcon />
            </IconButton>
          </div>
        </div>

        <div
          style={{
            display: 'grid',
            minHeight: '0',
            height: '100%',
            gridTemplateColumns: '2fr 3fr',
          }}
        >
          <img
            src={`${post.defaultPostThumbnail}`}
            style={{
              display: 'block',
              minWidth: '0',
              width: '100%',
              minHeight: '0',
              height: '100%',
              objectFit: 'contain',
              background: 'black',
            }}
          ></img>
          <div
            style={{
              display: 'grid',
              gridTemplateRows: '1fr 4fr 50px',

              minHeight: '0',
              height: '100%',
            }}
          >
            <DialogTitle
              sx={{
                '&.MuiDialogTitle-root.MuiTypography-root': {
                  fontSize: 40,
                  fontWeight: 'bold',
                },
              }}
            >
              {post.title}
            </DialogTitle>
            <DialogContentText
              sx={{
                '&.MuiDialogContentText-root.MuiTypography-root': {
                  fontSize: 20,
                  color: 'black',
                  padding: 3,
                  overflow: 'auto',
                },
              }}
            >
              <div dangerouslySetInnerHTML={{ __html: post.content }}></div>{' '}
            </DialogContentText>
            <div
              style={{
                marginLeft: 'auto',
                marginRight: '6px',
              }}
            >
              <IconButton
                onClick={() => {
                  setStar(prev => {
                    prev ? alert('좋아요 취소') : alert('좋아요');
                    return !prev;
                  });
                }}
              >
                {!star ? (
                  <StarBorderIcon />
                ) : (
                  <StarIcon
                    sx={{
                      color: 'hotPink',
                    }}
                  />
                )}
              </IconButton>
              <IconButton>
                <MoveToInboxIcon />
              </IconButton>
              <IconButton
                aria-controls={menuId}
                aria-haspopup="false"
                onClick={handleMenuOpen}
              >
                <MoreVertIcon />
              </IconButton>
            </div>
          </div>
        </div>
        <div style={{ borderTop: '1px solid rgba(0, 0, 0, .2)' }}>
          <DialogContent>
            <DialogContentText>댓글기능 추후 추가 예정</DialogContentText>
          </DialogContent>
        </div>
        {renderMenu}
      </Dialog>
    </>
  );
}
