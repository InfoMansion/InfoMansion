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
import { loginUserState } from '../../state/roomState';
import { useRecoilState } from 'recoil';
import useAuth from '../../hooks/useAuth';

export default function PostViewModal({ showModal, handleModalClose }) {
  const [cookies] = useCookies(['cookie-name']);
  const handleClose = () => setShowModal(false);
  const [star, setStar] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const isMenuOpen = Boolean(anchorEl);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [postUserInfo, setPostUserInfo] = useState('');
  const { auth, setAuth } = useAuth();
  const [, setNowFollow] = useState();
  const [isUser, setIsUser] = useState(false);
  const [loginUser, setLoginUser] = useRecoilState(loginUserState);

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

  const getPostUserInfo = async () => {
    try {
      axios
        .get(`/api/v1/users/${postDetail.userName}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          if (res === undefined) return;

          setIsUser(res.data.data.loginUser);
          setPostUserInfo(res.data.data);
          setNowFollow(res.data.data.follower);
        });
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    getPostUserInfo();
  }, [postDetail]);

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

  return (
    <>
      <Dialog
        open={showModal}
        onClose={handleModalClose}
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
                src={postUserInfo.profileImage}
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
              {isUser ? (
                <div></div>
              ) : (
                <Follow
                  isFollow={postUserInfo.follow}
                  username={postUserInfo.username}
                  setNowFollow={setNowFollow}
                ></Follow>
              )}
            </div>
            <IconButton onClick={handleModalClose}>
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
            src={`${postDetail.defaultPostThumbnail}`}
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
              {postDetail.title}
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
              <div
                dangerouslySetInnerHTML={{ __html: postDetail.content }}
              ></div>{' '}
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
              {isUser ? (
                <IconButton
                  aria-controls={menuId}
                  aria-haspopup="false"
                  onClick={handleMenuOpen}
                >
                  <MoreVertIcon />
                </IconButton>
              ) : (
                <div></div>
              )}
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
