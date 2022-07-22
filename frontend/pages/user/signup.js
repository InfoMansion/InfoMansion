import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import tokenState from '../../state/token';
import { useRecoilState } from 'recoil';
import { likeCateState } from '../../state/likeCate';
import { useState } from 'react';
import { useRouter } from 'next/router';

const theme = createTheme();

export default function SignUp() {
  const router = useRouter();
  const [likeCate, setlikeCate] = useRecoilState(likeCateState);

  const [inputEmail, setInputEmail] = useState('');
  const [inputPassword, setInputPassword] = useState('');
  const [inputPassword2, setInputPassword2] = useState('');
  const [inputUsername, setInputUsername] = useState('');

  const confirmEmail = /^[\w+_]\w+@\w+\.\w+/.test(inputEmail);
  const confirmPassword = /^(?=.*[a-zA-Z])((?=.*\d)(?=.*\W)).{8,20}$/.test(
    inputPassword,
  );
  const confirmPwSame = !!(inputPassword === inputPassword2);
  const inputUnFinish = !(
    confirmEmail &&
    confirmPassword &&
    confirmPwSame &&
    inputUsername
  );

  function handleInput(event) {
    const { name, value } = event.target;
    if (name === 'email') {
      setInputEmail(value);
    } else if (name === 'password') {
      setInputPassword(value);
    } else if (name === 'password2') {
      setInputPassword2(value);
    } else if (name === 'username') {
      setInputUsername(value);
    }
  }

  const handleSubmit = event => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const credentials = {
      email: data.get('email'),
      password: data.get('password'),
      username: data.get('username'),
      tel: data.get('tel'),
    };
    // Axios({
    //   url: 'http://localhost:8080/accounts/signup',
    //   method: 'post',
    //   data: credentials
    // })
    // .then(res => {
    //   const token = res.data.key
    //   setToken(token)
    // })
  };

  function prevPage() {
    setlikeCate('');
    router.push('/user/category');
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
            Sign up
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  onChange={handleInput}
                  autoCapitalize="off"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                  onChange={handleInput}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password2"
                  label="Password 확인"
                  type="password"
                  id="password2"
                  autoComplete="new-password"
                  onChange={handleInput}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="username"
                  label="username"
                  name="username"
                  autoComplete="username"
                  onChange={handleInput}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={inputUnFinish}
            >
              Sign Up
            </Button>
            <Button
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onClick={prevPage}
            >
              PREV
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
