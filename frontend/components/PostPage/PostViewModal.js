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
import FollowList from '../RoomPage/atoms/FollowList';

export default function PostViewModal({ showModal, handleModalClose }) {
  const [cookies] = useCookies(['cookie-name']);
  const [star, setStar] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const isMenuOpen = Boolean(anchorEl);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [postUserInfo, setPostUserInfo] = useState('');
  const [, setNowFollow] = useState();
  const [isUser, setIsUser] = useState(false);
  const [isLike, setIsLike] = useState(false);
  const [modalInfo, setModalInfo] = useState(undefined);

  console.log(postDetail);
  const handleMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };
  const menuId = 'primary-search-account-menu';

  const postDelete = async () => {
    const postInfo = {
      postId: postDetail.id,
    };
    try {
      const { data } = await axios.patch(
        `/api/v1/posts/${postDetail.id}`,
        postInfo,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        },
      );
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
      setIsLike(postDetail.likeFlag);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    getPostUserInfo();
  }, [postDetail]);

  const handleLikeClick = async () => {
    try {
      await axios.post(
        `/api/v2/posts/likes/${postDetail.id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        },
      );
      setPostDetail(prev => ({
        ...prev,
        likeFlag: !prev.likeFlag,
        likes: prev.likes + 1,
      }));
      setIsLike(prev => !prev);
      alert('게시글 좋아요!');
    } catch (e) {
      console.log(e);
    }
  };

  const handleUnLikeClick = async () => {
    try {
      await axios.delete(`/api/v2/posts/likes/${postDetail.id}`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      });
      setPostDetail(prev => ({
        ...prev,
        likes: prev.likes - 1,
        likeFlag: !prev.likeFlag,
      }));
      setIsLike(prev => !prev);
      alert('게시글 좋아요 취소!');
    } catch (e) {
      console.log(e);
    }
  };

  const handleLikeList = async () => {
    try {
      const { data } = await axios.get(`/api/v2/posts/likes/${postDetail.id}`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      });
      console.log(data);
      setModalInfo({ title: '좋아요 누른 사람들', data: data.data });
    } catch (e) {
      console.log(e);
    }
  };

  const handleListClose = () => {
    setModalInfo(undefined);
  };

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
            pathname: '/post/UpdatePost',
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
      {modalInfo !== undefined && (
        <FollowList
          modalInfo={modalInfo}
          handleModalClose={handleListClose}
        ></FollowList>
      )}
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
            gridTemplateRows: '70px 1fr',
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
              gridTemplateRows: '0.5fr 4fr 50px',

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
                onClick={() =>
                  isLike ? handleUnLikeClick() : handleLikeClick()
                }
              >
                {!isLike ? (
                  <StarBorderIcon />
                ) : (
                  <StarIcon
                    sx={{
                      color: 'hotPink',
                    }}
                  />
                )}
              </IconButton>
              <IconButton onClick={handleLikeList}>
                {postDetail.likes}
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
        {renderMenu}
      </Dialog>
    </>
  );
}
