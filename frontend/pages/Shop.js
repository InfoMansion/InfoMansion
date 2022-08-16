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
  IconButton,
} from '@mui/material';
import RadioButtonCheckedIcon from '@mui/icons-material/RadioButtonChecked';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { useEffect, useState } from 'react';
import ShowWindow from '../components/ShopPage/ShowWindow';

import { Canvas } from '@react-three/fiber';
import { OrthographicCamera } from '@react-three/drei';
import { useCookies } from 'react-cookie';
import axios from '../utils/axios';
import { pageLoading } from '../state/pageLoading';
import { useSetRecoilState } from 'recoil';
import BuyModal from '../components/ShopPage/atoms/BuyModal'
import stuffTypes from '../components/jsonData/stuffTypes.json'

export default function Shop() {
  const [cookies] = useCookies(['cookie-name']);
  const [scrollTarget] = useState(0);
  const setPageLoading = useSetRecoilState(pageLoading);
  const [stuffBundles, setStuffBundles] = useState([]);

  const [nowStuffBundle, setNowStuffBundle] = useState();
  const [bundleIndex, setBundleIndex] = useState();
  const [pages, setPages] = useState([1,1,1,1,1,1,1,1,1,1,
                                      1,1,1,1,1,1,1,1,1,1,
                                      1,1,1,1,1,1,1,1,1,1,
                                    ])

  const [viewStuffs, setViewStuffs] = useState([]);
  const [nowStuff, setNowStuff] = useState({});

  const [credit, setCredit] = useState(0);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    try {
      setPageLoading(true);
      
      // 기본 정보 가져오기.
      axios
        .get(`/api/v1/stores?pageSize=6`, {
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
    let st = (pages[i]-1)*6;

    setBundleIndex(i);
    setNowStuffBundle(stuffBundles[i]);
    setViewStuffs(stuffBundles[i].slice.content.slice(st, st + 6));
  }

  function click(e, stuff) {
    setNowStuff(stuff);
    setOpen(true);
  }

  function handlePrev() {
    if(pages[bundleIndex] == 1) return;
    
    let copypages = [...pages];
    copypages[bundleIndex]--;
    setPages(copypages);

    let st = (copypages[bundleIndex]-1)*6;
    setViewStuffs(stuffBundles[bundleIndex].slice.content.slice(st, st + 6));

  }
  function handleNext() {
    let copypages = [...pages];
    copypages[bundleIndex]++;
    
    let st = (copypages[bundleIndex]-1)*6;
    if(stuffBundles[bundleIndex].slice.content[st]){
      setViewStuffs(stuffBundles[bundleIndex].slice.content.slice(st, st+6));
    }
    else{
      axios.get(`/api/v1/stores/${nowStuffBundle.stuffType}`, {
        params : {
          page : copypages[bundleIndex],
          size : 6
        },
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      })
      .then( res => {
        if(res.data.data.content.length == 0) return;
        
        let copyStuffBundles = [...stuffBundles];
        copyStuffBundles[bundleIndex].slice.content = copyStuffBundles[bundleIndex].slice.content.concat(res.data.data.content);
        
        setStuffBundles(copyStuffBundles);
        setViewStuffs(res.data.data.content);
        setPages(copypages);
      })
      .catch(e => {
        console.log(e)
      })
    }
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
            <Box
              key={nowStuffBundle.stuffType}
              sx={{ m: 2, }}
            >
              <Typography
                variant="h5"
                sx={{ m: 1, color : 'white' }}
              >
                {nowStuffBundle.stuffTypeName}
              </Typography>

              <Divider color="white" />

              <Box
                style={{
                  backgroundColor : 'rgba(255,255,255,0.8)'
                }}
                sx={{ 
                  my : 2,
                  borderRadius : 2
                }}
              >
              <Canvas
                style={{ height: '500px',}}
              >
                <ShowWindow
                  ScrollTarget={scrollTarget}
                  stuffs={viewStuffs}
                  click={click}
                  scale={stuffTypes[nowStuffBundle.stuffType].scale}
                />

                <OrthographicCamera makeDefault position={[0, 0, 4]} zoom={50} />
              </Canvas>
              </Box>
              
              <Box
                sx={{
                  display : 'flex',
                  justifyContent : 'space-evenly',
                  alignItems : 'center',
                  color : 'white'
                }}
              >
                <IconButton onClick={handlePrev}>
                 <ArrowBackIosIcon sx={{color : 'white'}} />
                </IconButton>
                <Typography variant="h5" >
                  {pages[bundleIndex]}
                </Typography>
                <IconButton onClick={handleNext}>
                 <ArrowForwardIosIcon sx={{color : 'white'}} />
                </IconButton>
              </Box>
            </Box>
            : <></> 
          }
        </Grid>
      </Grid>

      <Box>
      </Box>
      <BuyModal 
        open={open}
        setOpen={setOpen}
        nowStuff={nowStuff}
        cookies={cookies}
        getCreditFromServer={getCreditFromServer}
      />
    </Container>
  );
}
