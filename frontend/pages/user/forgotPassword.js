import React, { useEffect, useState } from 'react';
import {
  TextField,
  Grid,
  Box,
  Paper,
  CardMedia,
  Button,
  Typography,
} from '@mui/material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useRouter } from 'next/router';
import axios from '../../utils/axios';
import { pageLoading } from '../../state/pageLoading';
import { useSetRecoilState } from 'recoil';
import { useCookies } from 'react-cookie';
const theme = createTheme({
  palette: {
    primary: {
      light: '#ff7961',
      main: '#ffffff',
      dark: '#ffffff',
      contrastText: '#000',
    },
  },
});

export default function forgotPassword() {
  const router = useRouter();
  const setPageLoading = useSetRecoilState(pageLoading);
  const [inputEmail, setInputEmail] = useState('');
  const [inputUsername, setInputUsername] = useState('');
  const confirmUsername = /^[a-zA-Zㄱ-힣0-9_]{3,15}$/.test(inputUsername);
  const cookies = useCookies(['cookie-name']);

  const confirmEmail = /^[\w+_]\w+@\w+\.\w+/.test(inputEmail);

  function handleInput(event) {
    const { name, value } = event.target;

    name === 'email' ? setInputEmail(value) : setInputUsername(value);
  }

  const handleSubmit = async event => {
    event.preventDefault();
    const credentials = {
      email: inputEmail,
      username: inputUsername,
    };

    try {
      console.log(credentials);
      setPageLoading(true);
      const res = await axios.post(
        'api/v1/auth/reset-password',
        credentials,
        {},
      );
      console.log(res);
      setPageLoading(false);
      alert('비밀번호가 이메일로 발송됐습니다.');
      router.push('/');
    } catch (e) {
      alert(e.response.data.message);
      setPageLoading(false);
      console.log(e);
    }
  };

  return (
    <div
      style={{
        height: window.innerHeight,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <ThemeProvider theme={theme}>
        <Box
          sx={{
            width: '50%',
            my: 8,
            mx: 4,
            display: 'flex',
            flexDirection: 'column',
            margin: '40px auto',
            alignItems: 'center',
          }}
          // boxShadow={3}
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
                style={{ width: '25%', height: '25%' }}
              ></CardMedia>
            </div>
            <div
              style={{
                display: 'flex',
                justifyContent: 'center',
                marginTop: '10px',
              }}
            >
              <Typography variant="h3" style={{ color: 'white' }}>
                비밀번호 찾기
              </Typography>
            </div>
            <Box
              component="form"
              onSubmit={handleSubmit}
              noValidate
              sx={{
                mt: 3,
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
                  value={inputEmail}
                  onChange={handleInput}
                  variant="outlined"
                  color="primary"
                  placeholder="등록된 email주소"
                  focused
                />
                <TextField
                  margin="normal"
                  required
                  name="Username"
                  label="Username"
                  type="Username"
                  id="Username"
                  autoComplete="current-password"
                  value={inputUsername}
                  onChange={handleInput}
                  variant="outlined"
                  color="primary"
                  placeholder="등록된 닉네임"
                  focused
                />
              </div>
              <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div
                  style={{ display: 'flex', justifyContent: 'space-between' }}
                >
                  <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    sx={{ mt: 3, mb: 2, mr: 3, color: 'grey' }}
                    disabled={!confirmEmail || !confirmUsername}
                    onClick={handleSubmit}
                  >
                    임시PW 발급
                  </Button>
                  <Button
                    variant="contained"
                    color="primary"
                    sx={{ mt: 3, mb: 2, color: 'grey' }}
                    onClick={() => {
                      router.push('/');
                    }}
                  >
                    돌아가기
                  </Button>
                </div>
              </div>
            </Box>
          </Box>
        </Box>
      </ThemeProvider>
    </div>
  );
}
