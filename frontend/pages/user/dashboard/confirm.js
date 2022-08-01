import React from 'react';
import { useState } from 'react';
import { Button, TextField, Typography } from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import CssBaseline from '@mui/material/CssBaseline';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Axios from 'axios';
import { useRouter } from 'next/router';
import { pwConfirmState } from '../../../state/pwConfirm';
import { useRecoilState } from 'recoil';

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

export default function Confirm() {
  const router = useRouter();
  const [inputPassword, setInputPassword] = useState('');
  const confirmPassword = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(
    inputPassword,
  );
  const [pwConfirm, setPwConfirmState] = useRecoilState(pwConfirmState);

  const inputUnFinish = !confirmPassword;

  function handleInput(event) {
    setInputPassword(event.target.value);
  }

  const handleSubmit = event => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const credentials = {
      email: data.get('email'),
      password: data.get('password'),
    };
    // test용 코드 실제로는  Axios요청 성공 한 이후에 바꿔야 함.
    setPwConfirmState(true);
    // Axios({
    //   url: 'http://localhost:8080/accounts/login',
    //   method: 'post',
    //   data: credentials
    // })
    // .then(res => {
    //   const token = res.data.key
    //   setToken(token)
    // })
  };

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
            backgroundColor: 'white',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Change Password
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password1"
              autoComplete="current-password"
              onChange={handleInput}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={inputUnFinish}
            >
              Confirm Password
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
