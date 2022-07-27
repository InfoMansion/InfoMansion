import HeaderNav from './common/HeaderNav';
import { styled } from '@mui/material/styles';
import { Box, Paper } from '@mui/material';
import { useCookies } from 'react-cookie';
import axios from 'axios';
import { useCallback, useEffect, useState } from 'react';
import moment from 'moment';
import { useRouter } from 'next/router';
import { useRecoilState } from 'recoil';
import { likeCateState } from '../state/likeCate';

const Root = styled('div')(({ theme }) => ({
  padding: theme.spacing(1),
  margin: '30px auto',

  [theme.breakpoints.down('lg')]: {
    width: '700px',
  },
  [theme.breakpoints.up('lg')]: {
    width: '1280px',
  },
}));

const guestPages = ['/user/login', '/user/signup', '/user/category'];

export default function Layout({ children }) {
  const [cookies] = useCookies(['cookie-name']);
  const [isAuthorized, setIsAuthorized] = useState(false);
  const { pathname, push } = useRouter();

  const reissueToken = useCallback(async () => {
    try {
      const { data } = await axios.post(
        'http://localhost:8080/api/v1/auth/reissue',
        { accessToken: cookies['InfoMansionAccessToken'] },
        { withCredentials: true },
      );
      console.log('res : ', data);
      const expiresAt = data.data.expiresAt;
      localStorage.setItem('expiresAt', expiresAt);
      setIsAuthorized(true);
    } catch (e) {
      console.log('error : ', e);
      setIsAuthorized(false);
    }
  }, []);

  useEffect(() => {
    if (!cookies['InfoMansionAccessToken']) {
      setIsAuthorized(false);
      return;
    }
    const expiresAt = localStorage.getItem('expiresAt');
    if (moment(expiresAt).diff(moment(new Date()), 'minutes') < 5) {
      reissueToken();
      return;
    }
    setIsAuthorized(true);
  }, [pathname, cookies, reissueToken]);

  useEffect(() => {
    if (!isAuthorized && !guestPages.includes(pathname)) {
      push('/user/login');
    }
  }, [pathname, isAuthorized]);

  return (
    <Box
      style={{
        backgroundColor: '#eeeeee',
      }}
      sx={{
        minHeight: '1500px',
      }}
    >
      <Paper elevation={2}>
        <HeaderNav />
      </Paper>

      <Root
        style={{
          backgroundColor: '#ffffff',
        }}
        sx={{
          height: '100%',
        }}
      >
        {children}
      </Root>
    </Box>
  );
}
