import { Typography, Box, Card, Divider } from '@mui/material';
import { useEffect, useState } from 'react';
import ShowWindow from '../components/ShopPage/ShowWindow_t';

import { Canvas } from '@react-three/fiber';
import { OrthographicCamera } from '@react-three/drei';
import { useCookies } from 'react-cookie';
import axios from '../utils/axios';
import { pageLoading } from '../state/pageLoading';
import { useSetRecoilState } from 'recoil';

export default function Shop() {
  const [cookies] = useCookies(['cookie-name']);
  const [scrollTarget] = useState(0);
  const setPageLoading = useSetRecoilState(pageLoading);
  const [stuffBundles, setStuffBundles] = useState([]);

  useEffect(() => {
    try {
      setPageLoading(true);
      axios
        .get(`/api/v1/stores?pageSize=10`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          //   setStuffBundles(res.data.data);
          setStuffBundles(res.data.data.slice(5, 6));
          setPageLoading(false);
        });
      console.log(stuffBundles);
    } catch (e) {
      setPageLoading(false);
      console.log(e);
    }
  }, []);
  return (
    <Box sx={{ height: '500px' }}>
      <Typography variant="h6">최신 가구들</Typography>
      <Divider />
      {/* <Box>
                크레딧이랑, 그런거 보여주기. navbar가 될 예정.
            </Box> */}
      {stuffBundles.map(stuffBundle => (
        <Canvas
          style={{
            height: '100%',
            backgroundColor: '#FFFFFF',
          }}
          sx={{
            m: 1,
          }}
        >
          <ShowWindow
            ScrollTarget={scrollTarget}
            stuffs={stuffBundle.slice.content}
          />

          <OrthographicCamera makeDefault position={[0, 0, 4]} zoom={50} />
        </Canvas>
      ))}
    </Box>
  );
}
