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
      dark: '#fc0000',
    },
    textfield: {
      light: '#ff7961',
      main: '#ffffff',
      dark: '#fc7a71',
      contrastText: 'white',
    },
  },
});

export default function Confirm(props) {
  const [cookies] = useCookies(['cookie-name']);
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
      <Container 
        component="main"
        style={{ 
          height : window.innerHeight - 80,
          display : 'flex',
          flexDirection : 'column',
          justifyContent : 'center',
          alignItems : 'center'
        }}
      >
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            width : '400px'
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography 
            sx={{color : 'white'}}
            variant="h5"
          >
            Confirm Password
          </Typography>
          <TextField
            required
            name="password"
            label="Password"
            type="password"
            autoComplete="current-password"
            variant="outlined"
            color="textfield"
            onChange={handleInput}
            sx={{
              m : 1,
              width : '70%'
            }}
            focused
          />
          <Button
            type="submit"
            variant="contained"
            sx={{ 
              mt : 1, 
              width : '65%',
              color : 'white'
            }}
            disabled={inputUnFinish}
          >
            Confirm Password
          </Button>
        </Box>
      <Box style={{width : 100, height : 400 }}>
      </Box>
      </Container>
    </ThemeProvider>
  );
}
