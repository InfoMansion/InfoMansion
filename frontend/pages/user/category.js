import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import CssBaseline from '@mui/material/CssBaseline';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import ButtonBase from '@mui/material/ButtonBase';
import Typography from '@mui/material/Typography';
import { useRecoilState } from 'recoil';
import { likeCateState } from '../../state/likeCate';
import { Button, Grid } from '@mui/material';
import { useRouter } from 'next/router';

const images = [
  {
    url: 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=164&h=164&fit=crop&auto=format',
    title: 'IT',
    name: 'IT',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1528712306091-ed0763094c98?w=164&h=164&fit=crop&auto=format',
    title: 'COOK',
    name: '쿠킹',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=164&h=164&fit=crop&auto=format',
    title: 'MUSIC',
    name: '음악',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1605899435973-ca2d1a8861cf?w=164&h=164&fit=crop&auto=format',
    title: 'GAME',
    name: '게임',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1471295253337-3ceaaedca402?w=164&h=164&fit=crop&auto=format',
    title: 'SPORTS',
    name: '스포츠',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1533827432537-70133748f5c8?w=164&h=164&fit=crop&auto=format',
    title: 'FASHION',
    name: '패션',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1488085061387-422e29b40080?w=164&h=164&fit=crop&auto=format',
    title: 'TRAVEL',
    name: '여행',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1511871893393-82e9c16b81e3?w=164&h=164&fit=crop&auto=format',
    title: 'DAILY',
    name: '데일리',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=164&h=164&fit=crop&auto=format',
    title: 'NATURE',
    name: '자연',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=164&h=164&fit=crop&auto=format',
    title: 'ART',
    name: '미술',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1600210492493-0946911123ea?w=164&h=164&fit=crop&auto=format',
    title: 'INTERIOR',
    name: '인테리어',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=164&h=164&fit=crop&auto=format',
    title: 'BEAUTY',
    name: '뷰티',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1527515637462-cff94eecc1ac?w=164&h=164&fit=crop&auto=format',
    title: 'CLEANING',
    name: '청소',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=164&h=164&fit=crop&auto=format',
    title: 'STUDY',
    name: '공부',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1588473158757-afdb399558d6?w=164&h=164&fit=crop&auto=format',
    title: 'HOMEAPPLIANCES',
    name: '가전제품',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=164&h=164&fit=crop&auto=format',
    title: 'CULTURE',
    name: '시사/문화',
    width: '25%',
  },
];

const ImageButton = styled(ButtonBase)(({ theme }) => ({
  position: 'relative',
  height: 200,
  [theme.breakpoints.down('lg')]: {
    width: '50% !important', // Overrides inline-style
    height: 200,
  },
  '&:hover, &.Mui-focusVisible': {
    zIndex: 1,
    '& .MuiImageBackdrop-root': {
      opacity: 0.15,
    },
    '& .MuiImageMarked-root': {
      opacity: 0,
    },
    '& .MuiTypography-root': {
      border: '4px solid currentColor',
    },
  },
}));

const ImageSrc = styled('span')({
  position: 'absolute',
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  backgroundSize: 'cover',
  backgroundPosition: 'center 40%',
});

const Image = styled('span')(({ theme }) => ({
  position: 'absolute',
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: theme.palette.common.white,
}));

const ImageBackdrop = styled('span')(({ theme }) => ({
  position: 'absolute',
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  backgroundColor: theme.palette.common.black,
  opacity: 0.4,
  transition: theme.transitions.create('opacity'),
}));

const theme = createTheme({
  palette: {
    primary: {
      light: '#ff7961',
      main: '#fc7a71',
      dark: '#ba000d',
      contrastText: '#000',
    },
  },
});

const ImageMarked = styled('span')(({ theme }) => ({
  height: 3,
  width: 18,
  backgroundColor: theme.palette.common.white,
  position: 'absolute',
  bottom: -2,
  left: 'calc(50% - 9px)',
  transition: theme.transitions.create('opacity'),
}));

export default function Category() {
  const router = useRouter();
  const [likeCate, setlikeCate] = useRecoilState(likeCateState);

  const clickCategory = event => {
    const selected = event.target.getAttribute('value') + ',';
    const count = likeCate.split(',').length - 1;
    const opacityTarget = event.currentTarget;
    if (selected === 'null,') {
      return;
    }
    if (likeCate.includes(selected)) {
      const updateCateState = likeCate.replace(selected, '');
      setlikeCate(updateCateState);
      opacityTarget.style.removeProperty('opacity');
    } else {
      if (count >= 5) {
        alert('5개 까지만 선택이 가능 합니다');
      } else {
        const updateCateState = likeCate + selected;
        setlikeCate(updateCateState);
        opacityTarget.style.opacity = 0.4;
      }
    }
    console.log(likeCate);
  };

  function nextPage() {
    router.push('/user/signup');
  }
  return (
    <ThemeProvider theme={theme}>
      <Grid container component="main" justifyContent="center">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
          className="page"
        >
          <Avatar src="/logo.png">
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h4" style={{ color: 'white' }}>
            Select Your Interest
          </Typography>
          <Typography component="h3" variant="h5" style={{ color: 'white' }}>
            1개 이상 5개 이하를 선택해주세요!
          </Typography>
          <Box
            sx={{
              display: 'flex',
              flexWrap: 'wrap',
              minWidth: 300,
              width: '100%',
              marginTop: 5,
            }}
            className="wrapper"
          >
            {images.map(image => (
              <ImageButton
                focusRipple
                key={image.title}
                style={{
                  width: image.width,
                  objectFit: 'cover',
                  objectPosition: 'center',
                }}
                onClick={clickCategory}
              >
                <ImageSrc
                  style={{ backgroundImage: `url(${image.url})` }}
                  value={image.title}
                />
                <ImageBackdrop
                  className="MuiImageBackdrop-root"
                  value={image.title}
                />
                <Image value={image.title} ido="child">
                  <Typography
                    component="span"
                    variant="subtitle1"
                    color="inherit"
                    sx={{
                      position: 'relative',
                      p: 4,
                      pt: 2,
                      pb: theme => `calc(${theme.spacing(1)} + 6px)`,
                    }}
                    value={image.title}
                  >
                    {image.name}
                    <ImageMarked
                      className="MuiImageMarked-root"
                      value={image.title}
                    />
                  </Typography>
                </Image>
              </ImageButton>
            ))}
          </Box>
          <Button
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2, color: 'white' }}
            disabled={!likeCate.includes(',')}
            onClick={nextPage}
            color="primary"
          >
            NEXT
          </Button>
        </Box>
      </Grid>
    </ThemeProvider>
  );
}
