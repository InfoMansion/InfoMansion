import React from 'react';
import { useState, useEffect } from 'react';
import { Button, Card, TextField, Typography } from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import CssBaseline from '@mui/material/CssBaseline';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from '../../utils/axios';
import CustomAlert from '../CustomAlert';
import { useCookies } from 'react-cookie';

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

export default function Change() {
  const [cookies] = useCookies(['cookie-name']);
  const [open, setOpen] = useState(false);
  const [inputPassword, setInputPassword] = useState('');
  const [inputPassword2, setInputPassword2] = useState('');
  const confirmPassword = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(
    inputPassword,
  );
  const confirmPwSame = !!(inputPassword === inputPassword2);
  const inputUnFinish = !(confirmPassword && confirmPwSame);

  function handleInput(event) {
    const { name, value } = event.target;
    if (name === 'password') {
      setInputPassword(value);
    } else if (name === 'password2') {
      setInputPassword2(value);
    }
  }

  const handleSubmit = async event => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const credentials = {
      newPassword: data.get('password'),
    };
    try {
      const { data } = await axios.patch(
        '/api/v1/users/password',
        credentials,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        },
      );
      // alert('비밀번호 변경이 완료되었습니다.');
      setOpen(true);
      setInputPassword('');
      setInputPassword2('');
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main">
        <CustomAlert
          open={open}
          setOpen={setOpen}
          severity="success"
          message="비밀번호 변경이 완료되었습니다."
        ></CustomAlert>
        <Card
          sx={{
            marginTop: 2,
            p: 2,
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: 'white',
          }}
          component="form"
          onSubmit={handleSubmit}
        >
          <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
            <LockOutlinedIcon />
          </Avatar>

          <Typography variant="h5">Change Password</Typography>

          <TextField
            required
            name="password"
            label="New Password"
            type="password"
            id="password1"
            autoComplete="current-password"
            value={inputPassword}
            onChange={handleInput}
            color="primary"
            sx={{ m: 1 }}
          />
          <TextField
            required
            name="password2"
            label="Confirm Password"
            type="password"
            id="password2"
            autoComplete="new-password"
            value={inputPassword2}
            onChange={handleInput}
            color="primary"
            sx={{ m: 1 }}
          />

          <Button
            type="submit"
            variant="contained"
            sx={{ m: 1, color: 'white' }}
            disabled={inputUnFinish}
            color="primary"
          >
            Change Password
          </Button>
        </Card>
      </Container>
    </ThemeProvider>
  );
}
