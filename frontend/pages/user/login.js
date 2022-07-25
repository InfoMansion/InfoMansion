import React from 'react';
import { useState, useCallback, useEffect } from 'react';
import {
  Button,
  Checkbox,
  FormControlLabel,
  Grid,
  Link,
  TextField,
  Typography,
  Card,
  CardMedia,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import CssBaseline from '@mui/material/CssBaseline';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Paper from '@mui/material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from 'axios';
import { useRouter } from 'next/router';
import { atom, useRecoilState } from 'recoil';
import { tokenState } from '../../state/token';
import { likeCateState } from '../../state/likeCate';
import { isLoginState } from '../../state/isLogin';
import { useCookies } from 'react-cookie';
import moment from 'moment';

// const function Logout(){
//   const [token, setToken] = useRecoilState(tokenState);
//   Axios.defaults.header.common['Authorization'] = 'Bearer' + token;
//   const handleSubmit = (event) => {
//     event.preventDefault()

// Axios({
//   url: 'http://localhost:8080/api/v1/logout',
//   method: 'post',
//   headers: ''
// })
// .then(res => {
//   console.log('logout')
//   setToken('')
// })
//   }
// }

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

export default function LogIn() {
  const [isLogin, setisLogin] = useRecoilState(isLoginState);
  const [likeCate, setlikeCate] = useRecoilState(likeCateState);
  const [token, setToken] = useRecoilState(tokenState);
  const [cookies, setCookie] = useCookies(['cookie-name']);
  const [inputId, setInputId] = useState('');
  const [inputPw, setInputPw] = useState('');

  const confirmId = /^[\w+_]\w+@\w+\.\w+/.test(inputId);
  const confirmPw = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(inputPw);

  const router = useRouter();

  const [windowSize, setWindowSize] = useState();

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
    //const data = new FormData(event.target);
    const credentials = {
      email: inputId,
      password: inputPw,
    };

    try {
      console.log(credentials);
      const { data } = await axios.post(
        'http://localhost:8080/api/v1/auth/login',
        credentials,
      );
      console.log('res : ', data);
      const accessToken = data.data.accessToken;
      const expiresAt = data.data.expirestAt;
      setCookie('cookie-name', accessToken, {
        path: '/',
        expires: expiresAt,
      });
      setisLogin(true);
      // userState 업데이트 할 axios 요청 추가로 보내기
      //axios.defaults.header.common['Authorization'] = 'Bearer' + accessToken;
      router.push('/');
    } catch (e) {
      console.log('error', e);
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <Grid
        container
        component="main"
        sx={{ height: '100vh' }}
        spacing={3}
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
            <CardMedia component="img" image="/LoginTemp.jpg"></CardMedia>
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
                <div style={{ display: 'flex', justifyContent: 'center' }}>
                  <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    sx={{ mt: 3, mb: 2 }}
                    disabled={!(confirmId && confirmPw)}
                    onClick={handleSubmit}
                  >
                    LOGIN
                  </Button>
                </div>
                <Grid container justifyContent="space-around">
                  <Link href="#" variant="body2">
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
      </Grid>
    </ThemeProvider>
  );
}
