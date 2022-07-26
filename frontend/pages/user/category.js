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
    url: 'https://images.unsplash.com/photo-1551963831-b3b1ca40c98e?w=164&h=164&fit=crop&auto=format',
    title: 'IT',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1551782450-a2132b4ba21d?w=164&h=164&fit=crop&auto=format',
    title: 'COOK',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1522770179533-24471fcdba45?w=164&h=164&fit=crop&auto=format',
    title: 'MUSIC',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1444418776041-9c7e33cc5a9c?w=164&h=164&fit=crop&auto=format',
    title: 'GAME',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1533827432537-70133748f5c8?w=164&h=164&fit=crop&auto=format',
    title: 'Hats',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?w=164&h=164&fit=crop&auto=format',
    title: 'Honey',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1516802273409-68526ee1bdd6?w=164&h=164&fit=crop&auto=format',
    title: 'SPORTS',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1518756131217-31eb79b20e8f?w=164&h=164&fit=crop&auto=format',
    title: 'Fern',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1597645587822-e99fa5d45d25?w=164&h=164&fit=crop&auto=format',
    title: 'Mushrooms',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1567306301408-9b74779a11af?w=164&h=164&fit=crop&auto=format',
    title: 'Tomato basil',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1471357674240-e1a485acb3e1?w=164&h=164&fit=crop&auto=format',
    title: 'TRAVEL',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1589118949245-7d38baf380d6?w=164&h=164&fit=crop&auto=format',
    title: 'Bike',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1597645587822-e99fa5d45d25?w=164&h=164&fit=crop&auto=format',
    title: 'Mushrooms',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1567306301408-9b74779a11af?w=164&h=164&fit=crop&auto=format',
    title: 'Tomato basil',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1471357674240-e1a485acb3e1?w=164&h=164&fit=crop&auto=format',
    title: 'TRAVEL',
    width: '25%',
  },
  {
    url: 'https://images.unsplash.com/photo-1589118949245-7d38baf380d6?w=164&h=164&fit=crop&auto=format',
    title: 'Bike',
    width: '25%',
  },
];

const ImageButton = styled(ButtonBase)(({ theme }) => ({
  position: 'relative',
  height: 200,
  [theme.breakpoints.down('md')]: {
    width: '100% !important', // Overrides inline-style
    height: 100,
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
      main: '#ff7961',
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

  function clickCategory(event) {
    const selected = event.target.getAttribute('value') + ',';
    const count = likeCate.split(',').length - 1;
    if (likeCate.includes(selected)) {
      const updateCateState = likeCate.replace(selected, '');
      setlikeCate(updateCateState);
      event.target.style.removeProperty('opacity');
    } else {
      if (count >= 100) {
        alert('100개 이상은 선택이 불가능 합니다');
      } else {
        const updateCateState = likeCate + selected;
        setlikeCate(updateCateState);
        event.target.style.opacity = 0.4;
      }
    }
    console.log(likeCate);
  }

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
          <Typography component="h1" variant="h4">
            Select Your Interest
          </Typography>
          <Typography component="h3" variant="h5">
            하나 이상 선택해주세요!
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
                <ImageSrc style={{ backgroundImage: `url(${image.url})` }} />
                <ImageBackdrop className="MuiImageBackdrop-root" />
                <Image value={image.title}>
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
                    {image.title}
                    <ImageMarked className="MuiImageMarked-root" />
                  </Typography>
                </Image>
              </ImageButton>
            ))}
          </Box>
          <Button
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
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
