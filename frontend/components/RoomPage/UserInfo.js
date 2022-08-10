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

export default function UserInfo({ userName }) {
  const router = useRouter();
  const [cookies] = useCookies(['cookie-name']);
  const [loginUser, setLoginUser] = useRecoilState(loginUserState);
  const [posts, setPosts] = useState([]);
  // username 가지고 userinfo 가져와야 함.
  const [userinfo, setUserinfo] = useState({
    userEmail: 'infomansion@google.co.kr',
    profileImage:
      '/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png',
    username: 'infomansion',
    introduce: ``,
    followcount: 10,
    followingcount: 20,
    categories: [],
  });

  useEffect(() => {
    if (!router.isReady) return;
    try {
      axios
        .get(`/api/v1/users/${router.query.userName}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          console.log('user', res.data);
          setLoginUser(res.data.data.loginUser);
          setUserinfo(res.data.data);
        });
    } catch (e) {
      console.log(e);
    }
  }, [router.isReady]);

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

  return (
    <Card>
      <Grid
        sx={{
          p: 2,
        }}
        container
      >
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
            src={userinfo.profileImage}
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
            <Typography variant="h4">{userinfo.username}</Typography>

            {loginUser ? (
              <Link href={userName + '/dashboard'}>
                <SettingsIcon sx={{ mx: 2 }} style={{ color: '#777777' }} />
              </Link>
            ) : (
              <></>
            )}
          </Box>
          <Typography variant="body2" color="text.secondary">
            {userinfo.userEmail}
          </Typography>
          <Box
            sx={{
              display: 'flex',
              my: 1,
            }}
          >
            {userinfo.categories.map((category, index) => (
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
        <Typography sx={{ m: 2 }}>{userinfo.introduce}</Typography>

        {/* 여기 브레이크포인트에 따라 더보기 버튼과 recentpost 바꿔 사용할 것. */}
        <RecentPost posts={posts} />
      </Grid>
    </Card>
  );
}
