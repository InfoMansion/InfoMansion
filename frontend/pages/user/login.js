import React from 'react';
import { useState } from 'react';
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
import Axios from 'axios';
import { useRouter } from 'next/router';
import { atom, useRecoilState } from 'recoil';
import tokenState from '../../state/token';

function TokenInput() {
  const [token, setToken] = useRecoilState(tokenState);
}

// const function Logout(){
//   const handleSubmit = (event) => {
//     event.preventDefault()
// Axios({
//   url: 'http://localhost:8080/accounts/login',
//   method: 'post',
//   data: credentials
// })
// .then(res => {
//   const token = ''
//   setToken(token)
// })
//   }
// }

const theme = createTheme();

export default function LogIn() {
  const [inputId, setInputId] = useState('');
  const [inputPw, setInputPw] = useState('');

  const confirmId = /^[\w+_]\w+@\w+\.\w+/.test(inputId);
  const confirmPw = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(inputPw);

  const router = useRouter();

  function handleInput(event) {
    const { name, value } = event.target;
    if (name === 'email') {
      setInputId(value);
    } else {
      setInputPw(value);
    }
  }

  const handleSubmit = event => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const credentials = {
      email: data.get('email'),
      password: data.get('password'),
    };

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
      <Grid container component="main" sx={{ height: '100vh' }}>
        <CssBaseline />
        <Grid
          item
          xs={false}
          sm={4}
          md={7}
          sx={{
            backgroundImage: 'url(/LoginTemp.jpg)',
            backgroundRepeat: 'no-repeat',
            backgroundColor: t =>
              t.palette.mode === 'light'
                ? t.palette.grey[50]
                : t.palette.grey[900],
            backgroundSize: 'contain',
            backgroundPosition: 'center',
          }}
        />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Card>
            <Box
              sx={{
                my: 8,
                mx: 4,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
              }}
            >
              <CardMedia component="img" image="/logo.png"></CardMedia>
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
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  autoCapitalize="off"
                  onChange={handleInput}
                />
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  onChange={handleInput}
                />
                <FormControlLabel
                  control={<Checkbox value="remember" color="secondary" />}
                  label="Remember me"
                />
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  color="secondary"
                  sx={{ mt: 3, mb: 2 }}
                  disabled={!(confirmId && confirmPw)}
                >
                  LOGIN
                </Button>
                <Grid container>
                  <Grid item xs>
                    <Link href="#" variant="body2">
                      Forgot password?
                    </Link>
                  </Grid>
                  <Grid item>
                    <Link href="/user/category" variant="body2">
                      {'Sign Up'}
                    </Link>
                  </Grid>
                </Grid>
              </Box>
            </Box>
          </Card>
        </Grid>
      </Grid>
    </ThemeProvider>
  );
}
