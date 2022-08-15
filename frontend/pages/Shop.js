import {
  Typography,
  Box,
  Card,
  Divider,
  Modal,
  Button,
  CardActions,
  CardContent,
  CardHeader,
  Container,
  Grid,
  MenuItem,
} from '@mui/material';
import RadioButtonCheckedIcon from '@mui/icons-material/RadioButtonChecked';
import { useEffect, useState } from 'react';
import ShowWindow from '../components/ShopPage/ShowWindow';

import { Canvas } from '@react-three/fiber';
import { Circle, OrbitControls, OrthographicCamera } from '@react-three/drei';
import { useCookies } from 'react-cookie';
import axios from '../utils/axios';
import { pageLoading } from '../state/pageLoading';
import { useSetRecoilState } from 'recoil';
import ShopStuff from '../components/ShopPage/atoms/ShopStuff';

export default function Shop() {
  const [cookies] = useCookies(['cookie-name']);
  const [scrollTarget] = useState(0);
  const setPageLoading = useSetRecoilState(pageLoading);
  const [stuffBundles, setStuffBundles] = useState([]);

  const [nowStuffBundle, setNowStuffBunele] = useState();
  const [bundleIndex, setBundleIndex] = useState();

  const [nowStuff, setNowStuff] = useState({});
  const [open, setOpen] = useState(false);

  const [credit, setCredit] = useState(0);

  useEffect(() => {
    try {
      setPageLoading(true);
      
      // 기본 정보 가져오기.
      axios
        .get(`/api/v1/stores?pageSize=10`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          setStuffBundles(res.data.data);
          setPageLoading(false);
        });

        // 크레딧 가져오기.
        getCreditFromServer();
    } catch (e) {
      setPageLoading(false);
      console.log(e);
    }
  }, []);

  function getCreditFromServer() {
    axios.get(`/api/v1/users/credit`, {
      headers: {
        Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
      },
    })
    .then(res => {
      setCredit(res.data.data.credit);
    })
  }

  function changeBundle(i) {
    setNowStuffBunele(stuffBundles[i]);
    setBundleIndex(i);
  }

  function click(e, stuff) {
    setNowStuff(stuff);
    console.log(stuff);
    setOpen(true);
  }
  function close() {
    setOpen(false);
  }

  const BuyStuffSubmit = async event => {
    event.preventDefault();
    if (window.confirm('가구를 구매하시겠습니까?')) {
      const buyStuff = {
        stuffIds: [nowStuff.id],
      };
      try {
        const { data } = await axios.post('/api/v2/user-stuff', buyStuff, {
          headers: {
            ContentType: 'application/json',
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        })
        .then(res => {
          alert('구매되었습니다!');
          getCreditFromServer();
        })
      } catch (e) {
        console.log(e);
        alert("구매에 실패하였습니다");
        // alert(e.response.data.message);
      }
    }
  };

  function BuyModal() {
    return (
      <Modal
        open={open}
        onClose={close}
        aria-labelledby="parent-modal-title"
        aria-describedby="parent-modal-description"
        style={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          bgcolor: 'background.paper',
          border: '2px solid #000',
          boxShadow: 24,
          p: 4,
        }}
      >
        <Box sx={{ width: 400 }}>
          <Canvas
            style={{
              height: '200px',
              backgroundColor: '#eeeeee',
            }}
          >
            <ShopStuff data={nowStuff} pos={[0, 0, 0]} dist={0} />
            <ambientLight />
          </Canvas>
          <Card>
            <CardHeader
              title={nowStuff.stuffNameKor}
              titleTypographyProps={{ align: 'center' }}
              sx={{
                backgroundColor: theme =>
                  theme.palette.mode === 'light'
                    ? theme.palette.grey[200]
                    : theme.palette.grey[700],
              }}
            />
            <CardContent>
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'baseline',
                  mb: 2,
                }}
              >
                <Typography component="h2" variant="h3" color="text.primary">
                  {nowStuff.price}
                </Typography>
                <Typography variant="h6" color="text.secondary">
                  /Credit
                </Typography>
              </Box>
            </CardContent>
            <CardActions>
              <Button fullWidth variant="outlined" onClick={BuyStuffSubmit}>
                Buy Stuff
              </Button>
            </CardActions>
          </Card>
        </Box>
      </Modal>
    );
  }

  return (
    <Container>
      <Grid container
        style={{
          justifyContent : 'center',
          alignItems : 'center'
        }}
      >
        <Grid item sm={3}>
          <Typography variant="h4" color="white">Shop</Typography>
        </Grid>
        <Grid item sm={9}
          style={{
            display : 'flex',
            justifyContent : 'end',
            alignItems : 'center',
            color : 'white'
          }}
        >
        <RadioButtonCheckedIcon sx={{mx : 1}}/>
            {credit}
        </Grid>
      </Grid>
      
      <Divider 
        style={{backgroundColor : 'white'}} 
        color={'white'}
      />
      <Grid container spacing={0}>

        {/* 좌측 메뉴 */}
        <Grid item xs={2} 
          style={{
            height : '600px',
            overflowY : 'scroll',
            color : 'white',
            
          }}
          sx={{my : 2}}
        >
          <Divider sx={{my : 1}} style={{backgroundColor : 'white'}} />
            {stuffBundles.map((stuffBundle, i) => (
              <Box>
                <MenuItem
                  onClick={() => changeBundle(i)}  
                >
                  {stuffBundle.stuffTypeName}
                </MenuItem>
                <Divider style={{backgroundColor : 'white'}} />
              </Box>
            ))}
        </Grid>

        <Grid item xs={1} style={{display : 'flex', justifyContent : 'center'}}>
          <Divider color={'white'} sx={{my : 2}} orientation="vertical"/> 
        </Grid>
        
        {/* 상점 페이지 */}
        <Grid item xs={9}>
          {nowStuffBundle ?
            <Card
            key={nowStuffBundle.stuffType}
            sx={{ m: 2, }}
            >
              <Typography
                variant="h5"
                sx={{ m: 1, }}
              >
                {nowStuffBundle.stuffTypeName}
              </Typography>

              <Canvas
                style={{
                  height: '200px',
                  backgroundColor: '#eeeeee',
                }}
                sx={{
                  m: 1,
                }}
              >
                <ShowWindow
                  ScrollTarget={scrollTarget}
                  stuffs={nowStuffBundle.slice.content}
                  click={click}
                />

                <OrthographicCamera makeDefault position={[0, 0, 4]} zoom={50} />
              </Canvas>
            </Card>
            : <></> 
          }
        </Grid>
      </Grid>

      <Box>
      </Box>
      <BuyModal />
    </Container>
  );
}
