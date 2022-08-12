import {
  Avatar,
  Box,
  Card,
  Divider,
  formControlLabelClasses,
  Grid,
  Typography,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import { useEffect, useState, useCallback } from 'react';
import RecentPost from './RecentPosts';
import SettingsIcon from '@mui/icons-material/Settings';
import Link from 'next/link';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { useRouter } from 'next/router';
import { loginUserState } from '../../state/roomState';
import { useRecoilState } from 'recoil';
import Follow from '../Follow';
import FollowList from './atoms/FollowList';

export default function UserInfo({
  loginUser,
  userInfo,
  nowFollow,
  setNowFollow,
}) {
  const router = useRouter();
  const [posts, setPosts] = useState([]);
  const [modalInfo, setModalInfo] = useState(undefined);

  const [cookies] = useCookies(['cookie-name']);

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
    <Box style={{ background: 'transparent' }}>
      {modalInfo !== undefined && (
        <FollowList
          modalInfo={modalInfo}
          handleModalClose={handleModalClose}
        ></FollowList>
      )}
      <Grid sx={{ p: 2 }} container>
        <Grid
          item
          xs={3}
          sx={{
            display: 'flex',
            justifyContent: 'center',
          }}
        >
          <Avatar
            alt="profile"
            src={userInfo.profileImage}
            sx={{
              width: '100%',
              maxWidth: '80px',
              height: '100%',
              maxHeight: '80px',
            }}
            style={{ objectFit: 'fill' }}
          />
        </Grid>
        <Grid item xs={9}>
          <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
            }}
          >
            <Typography variant="h4">{userInfo.username}</Typography>
            <div onClick={getFollowingInfo}>팔로우 {userInfo.following}</div>
            <div onClick={getFollowerInfo}>팔로워 {nowFollow}</div>
            {loginUser ? (
              <Link href={userInfo.username + '/dashboard'}>
                <SettingsIcon sx={{ mx: 2 }} style={{ color: '#777777' }} />
              </Link>
            ) : (
              <Follow
                isFollow={userInfo.follow}
                username={userInfo.username}
                setNowFollow={setNowFollow}
              ></Follow>
            )}
          </Box>
          <Typography variant="body2" color="text.secondary">
            {userInfo.userEmail}
          </Typography>
          <Box
            sx={{
              display: 'flex',
              my: 1,
            }}
          >
            {userInfo.categories.map((category, index) => (
              <Typography
                variant="body2"
                style={{
                  backgroundColor: '#fc7a71',
                  color: 'white',
                }}
                sx={{
                  px: 2,
                  mr: 1,
                  borderRadius: 4,
                }}
              >
                {category}
              </Typography>
            ))}
          </Box>
        </Grid>

        <Divider />
        <Typography sx={{ m: 2 }}>{userInfo.introduce}</Typography>

        {/* 여기 브레이크포인트에 따라 더보기 버튼과 recentpost 바꿔 사용할 것. */}
        <RecentPost posts={posts} />
      </Grid>
    </Box>
  );
}
