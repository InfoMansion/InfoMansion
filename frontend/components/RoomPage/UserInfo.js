import {
  Avatar,
  Box,
  Dialog,
  Divider,
  Grid,
  IconButton,
  Typography,
} from '@mui/material';
import { useEffect, useState, useCallback } from 'react';
import RecentPost from './RecentPosts';
import SettingsIcon from '@mui/icons-material/Settings';
import SaveAsIcon from '@mui/icons-material/SaveAs';
import Link from 'next/link';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { useRouter } from 'next/router';
import Follow from '../Follow';
import FollowList from './atoms/FollowList';
import StarIcon from '@mui/icons-material/Star';
import Post from './atoms/Post';
import { useRecoilState } from 'recoil';
import { postDetailState } from '../../state/postDetailState';
import PostViewModal from '../PostPage/PostViewModal';
import useAuth from '../../hooks/useAuth';
import CloseIcon from '@mui/icons-material/Close';

export default function UserInfo({
  loginUser,
  userInfo,
  nowFollow,
  setNowFollow,
  focused,
}) {
  const router = useRouter();
  const [posts, setPosts] = useState([]);
  const [modalInfo, setModalInfo] = useState(undefined);
  const [cookies] = useCookies(['cookie-name']);
  const [likePosts, setLikePosts] = useState(undefined);
  const [openLikePostModal, setOpenLikePostModal] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const { auth } = useAuth();

  const textStyle = {
    color: focused ? 'black' : 'white',
  };

  const getRecentPost = useCallback(async () => {
    try {
      const { data } = await axios.get(
        `/api/v1/posts/recent?userName=${router.query.userName}`,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        },
      );
      console.log(data.data);
      setPosts(data.data);
    } catch (e) {
      console.log('recent error ', e);
    }
  }, [router.query]);

  useEffect(() => {
    getRecentPost();
  }, [getRecentPost]);

  const handleModalClose = () => {
    setModalInfo(undefined);
  };

  const getFollowingInfo = async () => {
    try {
      const { data } = await axios.get(
        `/api/v1/follow/following/${userInfo.username}`,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        },
      );
      setModalInfo({ title: '팔로우', data: data.data });
    } catch (e) {
      console.log(e);
    }
  };

  const getFollowerInfo = async () => {
    try {
      const { data } = await axios.get(
        `/api/v1/follow/follower/${userInfo.username}`,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        },
      );
      console.log(data);
      setModalInfo({ title: '팔로워', data: data.data });
    } catch (e) {
      console.log(e);
    }
  };

  const showLikePostList = async () => {
    try {
      const { data } = await axios.get(`/api/v2/posts/my-likes`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      });
      console.log(data);
      setLikePosts(data.data.slice(0, 10));
      setOpenLikePostModal(true);
    } catch (e) {
      console.log(e);
    }
  };

  const closeModal = () => {
    setLikePosts([]);
  };

  const openModal = post => {
    try {
      axios
        .get(`/api/v1/posts/detail/${post.id}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          console.log(res.data);
          setPostDetail(res.data.data);
          if (postDetail.userName === auth.username) {
            setLoginUser(true);
          }
        })
        .then(setShowModal(true));
    } catch (e) {
      console.log(e);
    }
  };
  const handleLikeModalClose = () => {
    setShowModal(false);
    setPostDetail('');
  };

  return (
    <Box
      style={{
        background: 'transparent',
        width: '400px',
      }}
    >
      <PostViewModal
        showModal={showModal}
        handleModalClose={handleLikeModalClose}
      ></PostViewModal>
      {modalInfo !== undefined && (
        <FollowList
          modalInfo={modalInfo}
          handleModalClose={handleModalClose}
        ></FollowList>
      )}

      {likePosts !== undefined && (
        <Dialog
          open={openLikePostModal}
          onClose={() => setOpenLikePostModal(prev => !prev)}
          sx={{
            '.MuiPaper-root': {
              maxWidth: undefined,
              maxHeight: '700px',
              width: '80vw',
              height: '80vh',
              minHeight: '0',
              borderRadius: '6px',
            },
          }}
        >
          <div
            style={{
              display: 'flex',
              minHeight: '0',
              height: '200px',
              alignItems: 'center',
              justifyContent: 'space-between',
              margin: '5px 10px',
            }}
          >
            <div style={{ fontSize: '25px' }}>좋아요 글 목록</div>
            <IconButton onClick={() => setOpenLikePostModal(prev => !prev)}>
              <CloseIcon />
            </IconButton>
          </div>
          <div
            style={{
              overflow: 'auto',
              borderTop: '1px solid rgba(0, 0, 0, .2)',
            }}
          >
            {likePosts.map(post => (
              <Box>
                <Post
                  post={post}
                  totheight={150}
                  picwidth={100}
                  maxcontent={50}
                  openModal={openModal}
                />
                <Divider sx={{ m: 0 }} />
              </Box>
            ))}
          </div>
        </Dialog>
      )}
      <Grid container sx={{ px: 2, pt: 2 }}>
        {/* 프로필 이미지 */}
        <Grid
          item
          sx={{
            display: 'flex',
            justifyContent: 'center',
          }}
        >
          <Avatar
            alt="profile"
            src={userInfo.profileImage}
            sx={{
              width: '75px',
              height: '75px',
              mr: 2,
            }}
            style={{ objectFit: 'fill' }}
          />
        </Grid>

        {/* 유저인포 */}
        <Grid item>
          <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
            }}
          >
            <Typography variant="h4" style={textStyle}>
              {userInfo.username}
            </Typography>

            {loginUser ? (
              <>
                <Link href={userInfo.username + '/dashboard'}>
                  <SettingsIcon
                    sx={{ mx: 2 }}
                    style={{ ...textStyle, cursor: 'pointer' }}
                  />
                </Link>
                <StarIcon
                  onClick={showLikePostList}
                  style={{ ...textStyle, cursor: 'pointer' }}
                />
                <Link href={'/post/TempPost'}>
                  <SaveAsIcon
                    sx={{ mx: 2 }}
                    style={{ ...textStyle, cursor: 'pointer' }}
                  />
                </Link>
              </>
            ) : (
              <></>
              // <Follow
              //   isFollow={userInfo.follow}
              //   username={userInfo.username}
              //   setNowFollow={setNowFollow}
              // ></Follow>
            )}
          </Box>

          <Box
            style={{
              display: 'flex',
              flexDirection: 'row',
              color: '#aaaaaa',
            }}
          >
            <Typography
              variant="body2"
              onClick={getFollowingInfo}
              style={{ ...textStyle, display: 'flex', cursor: 'pointer' }}
            >
              팔로우
              <Typography
                style={{ ...textStyle, cursor: 'pointer' }}
                sx={{ ml: 1, mr: 2 }}
              >
                {userInfo.following}
              </Typography>
            </Typography>

            <Typography
              variant="body2"
              onClick={getFollowerInfo}
              style={{ ...textStyle, display: 'flex', cursor: 'pointer' }}
            >
              팔로잉
              <Typography style={textStyle} sx={{ ml: 1 }}>
                {nowFollow}
              </Typography>
            </Typography>
          </Box>
        </Grid>
      </Grid>

      <Box
        style={{
          height: '50px',
        }}
        sx={{
          display: 'flex',
          flexWrap: 'wrap',
          m: 2,
        }}
      >
        {userInfo.categories.map((category, index) => (
          <Typography
            variant="body2"
            style={{
              backgroundColor: '#fc7a71',
              color: 'white',
              height: '20px',
            }}
            sx={{
              px: 2,
              mr: 1,
              mb: 1,
              borderRadius: 4,
            }}
          >
            {category}
          </Typography>
        ))}
      </Box>

      <Divider color={'white'} />
      <Typography sx={{ m: 2 }}>{userInfo.introduce}</Typography>

      <RecentPost posts={posts} />
    </Box>
  );
}
