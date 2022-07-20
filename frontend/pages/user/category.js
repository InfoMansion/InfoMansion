import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import ImageList from '@mui/material/ImageList';
import ImageListItem from '@mui/material/ImageListItem';
import { useRecoilState } from 'recoil';
import { likeCateState } from '../../state/likeCate';
import { Button } from '@mui/material';
import { useRouter } from 'next/router';

const theme = createTheme();

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
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar src="/logo.png">
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Select Your Interest
          </Typography>
          <Box noValidate sx={{ mt: 3 }}>
            <ImageList
              sx={{ width: 500, height: 450 }}
              cols={3}
              rowHeight={164}
            >
              {itemData.map(item => (
                <ImageListItem
                  key={item.img}
                  style={{
                    backgroundImage: `url(${item.img}?w=164&h=164&fit=crop&auto=format)`,
                  }}
                  className="category"
                  onClick={clickCategory}
                  value={item.title}
                >
                  <p className="categoryName">
                    <b>{item.title}</b>
                  </p>
                </ImageListItem>
              ))}
            </ImageList>
            <Button
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={likeCate.includes(',')}
              onClick={nextPage}
            >
              NEXT
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}

const itemData = [
  {
    img: 'https://images.unsplash.com/photo-1551963831-b3b1ca40c98e',
    title: 'Breakfast',
  },
  {
    img: 'https://images.unsplash.com/photo-1551782450-a2132b4ba21d',
    title: 'Burger',
  },
  {
    img: 'https://images.unsplash.com/photo-1522770179533-24471fcdba45',
    title: 'Camera',
  },
  {
    img: 'https://images.unsplash.com/photo-1444418776041-9c7e33cc5a9c',
    title: 'Coffee',
  },
  {
    img: 'https://images.unsplash.com/photo-1533827432537-70133748f5c8',
    title: 'Hats',
  },
  {
    img: 'https://images.unsplash.com/photo-1558642452-9d2a7deb7f62',
    title: 'Honey',
  },
  {
    img: 'https://images.unsplash.com/photo-1516802273409-68526ee1bdd6',
    title: 'Basketball',
  },
  {
    img: 'https://images.unsplash.com/photo-1518756131217-31eb79b20e8f',
    title: 'Fern',
  },
  {
    img: 'https://images.unsplash.com/photo-1597645587822-e99fa5d45d25',
    title: 'Mushrooms',
  },
  {
    img: 'https://images.unsplash.com/photo-1567306301408-9b74779a11af',
    title: 'Tomato basil',
  },
  {
    img: 'https://images.unsplash.com/photo-1471357674240-e1a485acb3e1',
    title: 'Sea star',
  },
  {
    img: 'https://images.unsplash.com/photo-1589118949245-7d38baf380d6',
    title: 'Bike',
  },
];
