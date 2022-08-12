import { Avatar, Box, Divider, Grid, Typography } from '@mui/material';
import { useEffect, useState, useCallback } from 'react';
import RecentPost from './RecentPosts';
import SettingsIcon from '@mui/icons-material/Settings';
import Link from 'next/link';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { useRouter } from 'next/router';
import Follow from '../Follow';
import FollowList from './atoms/FollowList';

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

  return (
    <Box
      style={{
        background: 'transparent',
        width: '400px',
      }}
    >
      {modalInfo !== undefined && (
        <FollowList
          modalInfo={modalInfo}
          handleModalClose={handleModalClose}
        ></FollowList>
      )}

      <Grid container sx={{ p: 2 }}>
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
              <Link href={userInfo.username + '/dashboard'}>
                <SettingsIcon sx={{ mx: 2 }} style={textStyle} />
              </Link>
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
              style={{ display: 'flex', ...textStyle }}
            >
              팔로우
              <Typography style={textStyle} sx={{ ml: 1, mr: 2 }}>
                {userInfo.following}
              </Typography>
            </Typography>

            <Typography
              variant="body2"
              onClick={getFollowerInfo}
              style={{ display: 'flex' }}
            >
              팔로잉
              <Typography style={textStyle} sx={{ ml: 1 }}>
                {nowFollow}
              </Typography>
            </Typography>
          </Box>
        </Grid>
        <Box
          style={{
            height: '30px',
          }}
          sx={{
            display: 'flex',
            flexWrap: 'wrap',
            my: 1,
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
      </Grid>
      <Divider color={'white'} />
      <Typography sx={{ m: 2 }}>{userInfo.introduce}</Typography>

      <RecentPost posts={posts} />
    </Box>
  );
}
