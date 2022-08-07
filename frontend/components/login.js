import React from 'react';
import { useState, useCallback, useEffect } from 'react';
import {
  Button,
  Grid,
  Link,
  TextField,
  Typography,
  Card,
  CardMedia,
} from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Paper from '@mui/material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from '../utils/axios';
import { useRouter } from 'next/router';
import { useRecoilState } from 'recoil';
import { likeCateState } from '../state/likeCate';
import { useCookies } from 'react-cookie';
import useAuth from '../hooks/useAuth';

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

export default function LogIn({ onSignIn }) {
  const [, setCookie] = useCookies(['cookie-name']);
  const [likeCate, setlikeCate] = useRecoilState(likeCateState);
  const [inputId, setInputId] = useState('');
  const [inputPw, setInputPw] = useState('');
  const { setAuth } = useAuth();

  const confirmId = /^[\w+_]\w+@\w+\.\w+/.test(inputId);
  const confirmPw = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(inputPw);

  const router = useRouter();

  const [windowSize, setWindowSize] = useState();

  const handleResize = useCallback(() => {
    setWindowSize({
      width: window.innerWidth,
      height: window.innerHeight,
    });
  }, []);

  useEffect(() => {
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, [handleResize]);

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
    <>
      {windowSize &&
        (windowSize.width >= 1200 ? (
          <ThemeProvider theme={theme}>
            <Grid
              container
              component="main"
              sx={{ height: '100%' }}
              spacing={1}
              alignItems="center"
            >
              <CssBaseline />
              <Grid item xs={7} lg={7}>
                <Box
                  sx={{
                    my: 8,
                    mx: 4,
                  }}
                >
                  <CardMedia
                    component="img"
                    image="/LoginTemp.jpg"
                    style={{ width: '80%', height: '80%' }}
                  ></CardMedia>
                </Box>
              </Grid>
              <Grid item xs={5} lg={5} component={Paper} elevation={6} square>
                <Box
                  sx={{
                    my: 8,
                    mx: 4,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                  }}
                  boxShadow={3}
                >
                  <Box
                    sx={{
                      my: 4,
                    }}
                  >
                    <div
                      style={{
                        display: 'flex',
                        justifyContent: 'center',
                      }}
                    >
                      <CardMedia
                        component="img"
                        image="/logo.png"
                        style={{ width: '33%', height: '35%' }}
                      ></CardMedia>
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
                          margin="normal"
                          required
                          id="email"
                          label="Email Address"
                          name="email"
                          autoComplete="email"
                          autoFocus
                          autoCapitalize="off"
                          value={inputId}
                          onChange={handleInput}
                          variant="outlined"
                          color="primary"
                          focused
                        />
                        <TextField
                          margin="normal"
                          required
                          name="password"
                          label="Password"
                          type="password"
                          id="password"
                          autoComplete="current-password"
                          value={inputPw}
                          onChange={handleInput}
                          variant="outlined"
                          color="primary"
                          focused
                        />
                      </div>
                      <div
                        style={{ display: 'flex', justifyContent: 'center' }}
                      >
                        <Button
                          type="submit"
                          variant="contained"
                          color="primary"
                          sx={{ mt: 3, mb: 2, color: 'white' }}
                          disabled={!(confirmId && confirmPw)}
                          onClick={handleSubmit}
                        >
                          LOGIN
                        </Button>
                      </div>
                      <Grid container justifyContent="space-around">
                        <Link href="/user/forgotPassword" variant="body2">
                          Forgot Password?
                        </Link>
                        <Link href="#" variant="body2" onClick={goSignUp}>
                          {'Sign Up'}
                        </Link>
                      </Grid>
                    </Box>
                  </Box>
                </Box>
              </Grid>
            </Grid>
          </ThemeProvider>
        ) : (
          <>
            {
              /*이곳에 줄었을 때 css 작성해주세요  사진은 제외하고 로그인 카드만 넣으시면 됩니다*/
              <ThemeProvider theme={theme}>
                <Grid item component={Paper} elevation={6} square>
                  <Box
                    sx={{
                      my: 8,
                      mx: 4,
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'center',
                    }}
                    boxShadow={3}
                  >
                    <Box
                      sx={{
                        my: 4,
                      }}
                    >
                      <div
                        style={{
                          display: 'flex',
                          justifyContent: 'center',
                        }}
                      >
                        <CardMedia
                          component="img"
                          image="/logo.png"
                          style={{ width: '33%', height: '35%' }}
                        ></CardMedia>
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
                            margin="normal"
                            required
                            id="email"
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            autoFocus
                            autoCapitalize="off"
                            value={inputId}
                            onChange={handleInput}
                            variant="outlined"
                            color="primary"
                            focused
                          />
                          <TextField
                            margin="normal"
                            required
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            value={inputPw}
                            onChange={handleInput}
                            variant="outlined"
                            color="primary"
                            focused
                          />
                        </div>
                        <div
                          style={{ display: 'flex', justifyContent: 'center' }}
                        >
                          <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            sx={{ mt: 3, mb: 2, color: 'white' }}
                            disabled={!(confirmId && confirmPw)}
                            onClick={handleSubmit}
                          >
                            LOGIN
                          </Button>
                        </div>
                        <Grid container justifyContent="space-around">
                          <Link href="/user/forgotPassword" variant="body2">
                            Forgot password?
                          </Link>
                          <Link href="#" variant="body2" onClick={goSignUp}>
                            {'Sign Up'}
                          </Link>
                        </Grid>
                      </Box>
                    </Box>
                  </Box>
                </Grid>
              </ThemeProvider>
            }
          </>
        ))}
    </>
  );
}
