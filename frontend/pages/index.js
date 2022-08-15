import { Box, Card, Link, styled } from '@mui/material';
import styles from '../styles/Hex.module.css';
import { useCallback, useEffect, useState } from 'react';
import useAuth from '../hooks/useAuth';
import LoginComponent from '../components/login';
import { useCookies } from 'react-cookie';
import axios from '../utils/axios';
import { useRouter } from 'next/router';
import { useInView } from 'react-intersection-observer';
import { pageLoading } from '../state/pageLoading';
import { useRecoilState } from 'recoil';

const Item = styled('li')(({ backgroundImage }) => ({
  ':before': {
    backgroundImage,
  },
}));

export default function Home() {
  const [windowSize, setWindowSize] = useState();
  const { auth } = useAuth();
  const [cookies] = useCookies(['cookie-name']);
  const router = useRouter();
  const [roomImgs, setRoomImgs] = useState([]);
  const [ref, inView] = useInView();
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useRecoilState(pageLoading);

  const handleResize = useCallback(() => {
    setWindowSize({
      width: window.innerWidth,
    });
  }, []);

  useEffect(() => {
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, [handleResize]);

  const init = useCallback(
    async token => {
      try {
        const { data } = await axios.get(
          `/api/v2/rooms/recommend?page=${page}&size=27`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              withCredentials: true,
            },
          },
        );
        console.log(data);
        console.log(data.data.roomResponseDtos);
        setRoomImgs(prev => [...prev, ...data.data.roomResponseDtos.content]);
      } catch (e) {
        console.log(e);
      }
    },
    [page],
  );

  useEffect(() => {
    if (!auth.isAuthorized || !cookies.InfoMansionAccessToken) {
      return;
    }
    init(cookies.InfoMansionAccessToken);
  }, [page, init, auth.isAuthorized, cookies]);

  useEffect(() => {
    // 사용자가 마지막 요소를 보고 있고, 로딩 중이 아니라면
    if (inView) {
      setPage(prev => prev + 1);
    }
  }, [inView]);

  return (
    <>
      {auth.isAuthorized ? (
        <Box>
          <div
            style={{
              width: '100%',
              //              height: '100%',
              minHeight: 'calc(100vh - 70px)',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              position: 'relative',
            }}
          >
            <div style={{ height: '100%', paddingTop: '1%' }}>
              <ul className={styles.container}>
                {roomImgs.map(v => (
                  <Item
                    key={v.userName}
                    className={styles.item}
                    backgroundImage={v.roomImg}
                  >
                    <Link href={`/${v.userName}`} style={{ zIndex: 2 }}></Link>
                  </Item>
                ))}
              </ul>
            </div>
            <div
              ref={ref}
              style={{
                position: 'absolute',
                zIndex: '1000',
                height: '30px',
                bottom: '300px',
                width: '100%',
              }}
            ></div>
          </div>
        </Box>
      ) : (
        <LoginComponent onSignIn={token => init(token)} />
      )}
    </>
  );
}
