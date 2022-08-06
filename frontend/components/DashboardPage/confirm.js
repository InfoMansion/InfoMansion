import React from 'react';
import { useState } from 'react';
import { Button, TextField, Typography } from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import CssBaseline from '@mui/material/CssBaseline';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from '../../utils/axios';
import { useRouter } from 'next/router';
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

export default function Confirm(props) {
  const [cookies] = useCookies(['cookie-name']);
  const router = useRouter();
  const [inputPassword, setInputPassword] = useState('');
  const confirmPassword = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(
    inputPassword,
  );

  const inputUnFinish = !confirmPassword;

  function handleInput(event) {
    setInputPassword(event.target.value);
  }

  const handleSubmit = async event => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const credentials = {
      currentPassword: formData.get('password'),
    };
    try {
      console.log(credentials);
      console.log(`Bearer ${cookies.InfoMansionAccessToken}`);
      const { data } = await axios.post('/api/v1/users/password', credentials, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      props.setUserInfo(data.data);
      props.setIsConfirmed(true);
    } catch (e) {
      console.log(e);
      alert(e.response.data.message);
    }
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
          <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Confirm Password
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
              sx={{ mt: 3, mb: 2, color: 'white' }}
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
