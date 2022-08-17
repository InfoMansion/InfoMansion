import HeaderNav from './common/HeaderNav';
import { styled } from '@mui/material/styles';
import { useEffect, useState, useCallback } from 'react';
import useAuth from '../hooks/useAuth';
import { useRecoilState, useSetRecoilState } from 'recoil';
import { pageLoading } from '../state/pageLoading';
import Loading from './Loading';
import { useRouter } from 'next/router';
import axios from '../utils/axios';
import { useCookies } from 'react-cookie';
import { notificationState } from '../state/notificationState';

const backgroundColor = [
  'linear-gradient(to bottom, #17223b, #1a2640, #1d2a46, #1f2e4b, #223251, #293958, #30405f, #374766, #445370, #515f79, #5e6b83, #6b778d)',
  'linear-gradient(to bottom, #94b49f, #9fbda8, #a9c6b1, #b4d0ba, #bfd9c3, #c8dfc8, #d2e4ce, #dbead4, #e4edd8, #edf1dd, #f5f4e2, #fcf8e8)',
  'linear-gradient(to bottom, #93b5c6, #9fb9ca, #aabdcd, #b4c1d0, #bec6d2, #c4c9d4, #caccd6, #d0cfd8, #d5d1da, #dbd3db, #e0d6db, #e4d8dc)',
  'linear-gradient(to bottom, #dbd0c0, #e1d5c6, #e6dbcc, #ece0d2, #f2e6d8, #f5e8d9, #f7e9d9, #faebda, #fae9d6, #fae7d1, #f9e6cd, #f9e4c8)',
  'linear-gradient(to bottom, #fdd9d9, #ffdcd7, #ffded4, #ffe2d3, #ffe5d2, #ffe7d2, #fee8d2, #fdead2, #fcead2, #fbead3, #fae9d3, #f9e9d4)',
  'linear-gradient(to top, #fbf0f0, #f6eaea, #f0e5e5, #ebdfdf, #e6dada, #e0d5d5, #dbcfcf, #d5caca, #cec3c3, #c6bdbd, #bfb6b6, #b8b0b0)',
  'linear-gradient(to bottom, #bbbfca, #c4c6cf, #ccced5, #d5d5da, #dddde0, #e2e2e4, #e6e6e7, #ebebeb, #eeeded, #f1efef, #f3f1f0, #f4f4f2)',
  'linear-gradient(to bottom, #8e7f7f, #968a8c, #9e9598, #a5a1a4, #aeacae, #b5b3b5, #bcbbbd, #c3c2c4, #cac7ca, #d2cbcf, #dad0d2, #e2d5d5)',
  'linear-gradient(to bottom, #716f81, #7b7289, #887590, #967694, #a57896)',
  'linear-gradient(to bottom, #314243, #34494c, #365156, #3d5b61, #44656c, #4b7078, #598087, #689196, #78a3a6, #88b4b5)',
  'linear-gradient(to bottom, #a6b1e1, #b1b8e5, #bbbee9, #c5c5ed, #cfccf1, #d6d1f4, #dcd7f6, #e2dcf9, #e7e0fa, #ebe5fc, #f0e9fd, #f4eeff)',
  'linear-gradient(to bottom, #112d4e, #1c3d65, #284e7d, #336096, #3f72af)',
  'linear-gradient(to bottom, #93b5c6, #a2bacb, #b1c0cf, #bec6d2, #c9ccd5)',
  'linear-gradient(to bottom, #343536, #3d3e3f, #464748, #4f5051, #595a5b)',
];

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

export default function Layout({ children }) {
  const { auth } = useAuth();
  const [loading] = useRecoilState(pageLoading);
  const { pathname } = useRouter();
  const [cookies] = useCookies(['cookie-name']);
  const setNotification = useSetRecoilState(notificationState);

  const [colorIdx, setColorIdx] = useState(0);
  useEffect(() => {
    if (!auth.isAuthorized) setColorIdx(Math.floor(Math.random() * 13));
  }, [auth.isAuthorized]);

  const init = useCallback(async () => {
    try {
      const { data } = await axios.get('/api/v1/notifications', {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      });
      setNotification(data.data);
    } catch (e) {
      console.log('initError : ', e);
    }
  }, [pathname]);
  
  useEffect(() => {
    if (!auth.isAuthorized) return;
    init();
  }, [pathname, notificationState]);

  return (
    <div>
      <div
        style={{
          minHeight: '100vh',
          width : '100vw',
          background: backgroundColor[colorIdx],
          position : 'absolute',
          zIndex : -5,
        }}
      >
        {auth.isAuthorized && <HeaderNav />}
        {children}
      </div>
      {loading && <Loading></Loading>}
    </div>
  );
}
