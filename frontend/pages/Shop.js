import {
  Typography,
  Box,
  Divider,
  Container,
  Grid,
  MenuItem,
  IconButton,
  Icon,
} from '@mui/material';
import CopyrightIcon from '@mui/icons-material/Copyright';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { useEffect, useState } from 'react';
import ShowWindow from '../components/ShopPage/ShowWindow';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';

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
  const setPageLoading = useSetRecoilState(pageLoading);
  const [stuffBundles, setStuffBundles] = useState([]);

  const [nowStuffBundle, setNowStuffBundle] = useState();
  const [bundleIndex, setBundleIndex] = useState(28);
  const [pages, setPages] = useState([0,0,0,0,0,0,0,0,0,0,
                                      0,0,0,0,0,0,0,0,0,0,
                                      0,0,0,0,0,0,0,0,0,0,
                                      0,0
                                    ])

  const [viewStuffs, setViewStuffs] = useState([]);
  const [nowStuff, setNowStuff] = useState({});

  const [credit, setCredit] = useState(0);
  const [open, setOpen] = useState(false);
  const [scale, setScale] = useState(1);

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
          
          setNowStuffBundle(res.data.data[bundleIndex]);
          setViewStuffs(res.data.data[bundleIndex].slice.content.slice(0, 6))
        });
        // 크레딧 가져오기. 
        getCreditFromServer();
    } catch (e) {
      console.log(e);
    }
    
    setTimeout(() => {
      setPageLoading(false);
    }, 2000)

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
    let st = (pages[i])*6;

    setBundleIndex(i);
    setNowStuffBundle(stuffBundles[i]);
    setViewStuffs(stuffBundles[i].slice.content.slice(st, st + 6));
  }

  function click(e, stuff, scale) {
    setNowStuff(stuff);
    setScale(scale);
    setOpen(true);
  }

  function handlePrev() {
    if(pages[bundleIndex] == 0) return;
    
    let copypages = [...pages];
    copypages[bundleIndex] -= 1;
    
    let st = (copypages[bundleIndex])*6;
    setViewStuffs(stuffBundles[bundleIndex].slice.content.slice(st, st + 6));
    setPages(copypages);
  }
  function handleNext() {
    let copypages = [...pages];
    copypages[bundleIndex] += 1;
    
    let st = (copypages[bundleIndex])*6;
    if(stuffBundles[bundleIndex].slice.content[st]){
      setViewStuffs(stuffBundles[bundleIndex].slice.content.slice(st, st+6));
      setPages(copypages);
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
      
        setPages(copypages);
        setStuffBundles(copyStuffBundles);
        setViewStuffs(res.data.data.content);
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
          <Box
            style={{
              display : 'flex',
              alinItems : 'center',
            }}
            sx={{ml : 2}}
          >
            <ShoppingCartIcon style={{ fontSize : '2rem', color : 'white',}} sx={{mr : 1, mb : 1}}/>
            <Typography variant="h5" color="white">
              상점
            </Typography>
          </Box>

        </Grid>
        <Grid item sm={9}
          style={{
            display : 'flex',
            justifyContent : 'end',
            alignItems : 'center',
            color : 'white'
          }}
        >
        <CopyrightIcon sx={{mx : 1}}/>
            {credit}
        </Grid>
      </Grid>
      
      <Divider 
        style={{backgroundColor : 'rgba(255,255,255,0.8)'}} 
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
                  backgroundColor : 'rgba(255,255,255,0.5)'
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
                  stuffs={viewStuffs}
                  click={(e, stuff) => click(e, stuff, stuffTypes[nowStuffBundle.stuffType].scale)}
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
                  {pages[bundleIndex] + 1}
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
        scale={scale}
        getCreditFromServer={getCreditFromServer}
      />
    </Container>
  );
}
