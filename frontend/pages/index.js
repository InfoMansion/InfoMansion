import { Box, Card, Link, styled } from '@mui/material';
import styles from '../styles/Hex.module.css';
import { useCallback, useEffect, useState } from 'react';
import useAuth from '../hooks/useAuth';
import LoginComponent from '../components/login';
import Shop from './Shop_tmp';
import { useCookies } from 'react-cookie';
import axios from '../utils/axios';
import { useRouter } from 'next/router';
import { IMG_S3_URL } from '../constants';

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
  const [roomImgs, setRoomImgs] = useState([{ useName: '', roomImg: '' }]);

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
        const { data } = await axios.get('/api/v1/rooms/recommend', {
          headers: {
            Authorization: `Bearer ${token}`,
            withCredentials: true,
          },
        });
        console.log(data);
        console.log(data.data.roomResponseDtos);
        setRoomImgs(data.data.roomResponseDtos);
      } catch (e) {
        console.log(e);
      }
    },
    [cookies],
  );

  useEffect(() => {
    if (!auth.isAuthorized || !cookies.InfoMansionAccessToken) {
      return;
    }
    init(cookies.InfoMansionAccessToken);
  }, [init, auth.isAuthorized, cookies]);

  return (
    <>
      {auth.isAuthorized ? (
        <Box>
          <div
            style={{
              width: '100%',
              height: '100%',
              minHeight: 'calc(100vh - 70px)',
              paddingBottom: '49px',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <div style={{ height: '100%', paddingTop: '1%' }}>
              <ul className={styles.container}>
                {roomImgs.map(v => (
                  <Item
                    className={styles.item}
                    backgroundImage={`url(${IMG_S3_URL}${v.roomImg})`}
                  >
                    <Link href={`/${v.userName}`} style={{ zIndex: 2 }}></Link>
                  </Item>
                ))}
              </ul>
            </div>
            <div
              style={{ width: '1205px', height: '340px', marginTop: '65px' }}
            >
              <Shop />
            </div>
          </div>
        </Box>
      ) : (
        <LoginComponent onSignIn={token => init(token)} />
      )}
    </>
  );
}
