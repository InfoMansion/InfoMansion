import React, { useEffect } from 'react';
import { useState } from 'react';
import {
  Button,
  Grid,
  Link,
  TextField,
  Typography,
  Card,
  CardMedia,
  Container,
} from '@mui/material';
import Box from '@mui/material/Box';
import { createTheme, styled, ThemeProvider } from '@mui/material/styles';
import axios from '../utils/axios';
import { useRecoilState } from 'recoil';
import { likeCateState } from '../state/likeCate';
import { useCookies } from 'react-cookie';
import useAuth from '../hooks/useAuth';
import styles from '../styles/Hex.module.css'
import loginImgLoc from './jsonData/loginImgLoc.json'

const theme = createTheme({
  palette : {
    primary : {
      light : '#ff7961',
      main : '#ffffff',
      dark : '#fc7a71',
      contrastText: 'white',
    },

    buttonColor : {
      light : '#ff7961',
      main : '#fc7a71',
      dark : '#fc0000',
      contrastText : 'white'
    }
  },
});

export default function LogIn({ onSignIn }) {
  const [, setCookie] = useCookies(['cookie-name']);
  const [likeCate, setlikeCate] = useRecoilState(likeCateState);
  const [inputId, setInputId] = useState('');
  const [inputPw, setInputPw] = useState('');
  const { setAuth } = useAuth();
  const [boxWidth, setBoxWidth] = useState(300);
  const [boxHeight, setBoxHeight] = useState(300);

  const confirmId = /^[\w+_]\w+@\w+\.\w+/.test(inputId);
  const confirmPw = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(inputPw);

  function handleBoxSize() {
    setBoxWidth(0.4 * window.innerWidth);
    if(window.innerWidth < 1000) {
      setBoxHeight(0.4 * window.innerWidth);
    }
    else {
      setBoxHeight(400);
    }
  }

  useEffect(() => {
    window.addEventListener("resize", handleBoxSize);
  }, [])

  function goSignUp(event) {
    event.preventDefault();
    setlikeCate('');
    router.push('/user/category');
  }

  function handleInput(event) {
    const { name, value } = event.target;
    if (name === 'email') {
      setInputId(value);
    } else {
      setInputPw(value);
    }
  }

  const handleSubmit = async event => {
    event.preventDefault();
    const credentials = {
      email: inputId,
      password: inputPw,
    };

    try {
      console.log(credentials);
      const { data } = await axios.post('/api/v1/auth/login', credentials, {
        withCredentials: true,
      });
      setAuth({ isAuthorized: true, accessToken: data.data.accessToken });
      localStorage.setItem('expiresAt', data.data.expiresAt);
      onSignIn(data.data.accessToken);
      console.log('data : ', data);
    } catch (e) {
      console.log('error', e);
    }
  };

  return (
      <ThemeProvider theme={theme}>
        {loginImgLoc.map( bundle => (
          <div
            style={{
              position : 'absolute',
              left : bundle.left + "%",
              right : bundle.right + "%",

              top : bundle.top + "%",
              bottom : bundle.bottom + "%",

              width : boxWidth,
              height : boxHeight,
            }}
          >
            {bundle.data.map( ({left, right, top, bottom, size, opacity}) => (
              <div
                style={{
                  position : 'absolute',
                  left : left + "%",
                  right : right + "%",
                  top : top + "%",
                  bottom : bottom + "%",
                  width : boxHeight * size * 0.01,
                }}
              >
                <img 
                  src="/test.png" 
                  className={styles.itemlogin}
                  style={{width : '100%'}}
                  />
                <img 
                  src="/image/whiteBox.png" 
                  className={styles.itemlogin}
                  style={{
                    width : '100%',
                    position : 'absolute',
                    opacity : opacity * 0.01
                  }}
                />
              </div>
            ))}
          </div>
        ))}

        <Container
          style={{
            height : window.innerHeight,
            display : 'flex',
            alignItems : 'center',
            justifyContent : 'center'
          }}
        >
          

          <Box
            sx={{
              mx: 4,
            }}
          >
            <Box >
              <div
                style={{
                  display: 'flex',
                  justifyContent: 'center',
                }}
              >
                <img
                  src="/logo.png"
                  style={{ width: '25%', height: '35%' }}
                />
              </div>
              <Box
                component="form"
                onSubmit={handleSubmit}
                noValidate
                sx={{
                  mt: 1,
                }}
              >
                <div
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                  }}
                >
                  <TextField
                    required
                    id="email"
                    label="Email Address"
                    name="email"
                    autoComplete="email"
                    autoFocus
                    value={inputId}
                    onChange={handleInput}
                    variant="outlined"
                    color="primary"
                    focused
                    sx={{
                      m : 1,
                      width : '50%'
                    }}
                  />
                  <TextField
                    required
                    name="password"
                    label="Password"
                    type="password"
                    autoComplete="current-password"
                    value={inputPw}
                    onChange={handleInput}
                    variant="outlined"
                    color="primary"
                    focused
                    sx={{
                      m : 1,
                      width : '50%'
                    }}
                  />
                </div>
                <div
                  style={{ display: 'flex', justifyContent: 'center' }}
                >
                  <Button
                    variant="contained"
                    color="buttonColor"
                    sx={{ 
                      m : 1, 
                      color: 'white' 
                    }}
                    disabled={!(confirmId && confirmPw)}
                    onClick={handleSubmit}
                  >
                    LOGIN
                  </Button>
                </div>
                  <Grid container justifyContent="center" >
                  <Link href="#" variant="body2" onClick={goSignUp}
                      sx={{
                        mx : 3
                      }}
                  >
                    Sign Up
                  </Link>
                  <Link href="/user/forgotPassword" variant="body2"
                    sx={{
                      mx : 3
                    }}
                  >
                    Forgot password?
                  </Link>
                </Grid>
              </Box>
            </Box>
          </Box>
        </Container>
      </ThemeProvider>
  );
}
