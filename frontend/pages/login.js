import React from 'react';
import { useState } from 'react';
import { Button, Checkbox, FormControlLabel, Grid, Link, TextField, Typography} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import CssBaseline from '@mui/material/CssBaseline';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Axios from 'axios';
import { useRouter } from 'next/router';
import { atom, useRecoilState } from 'recoil'
import tokenState from '../state/token'




function TokenInput(){
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
  const [inputId, setInputId] = useState('')
  const [inputPw, setInputPw] = useState('')

  const router = useRouter()

  function handleInput (event) {
    const { name, value } = event.target;
    if (name === 'email') {
      setInputId(value)
    } else {
      setInputPw(value)
    }
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const credentials = {
      email: data.get('email'),
      password: data.get('password'),
    }

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
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
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
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={!(inputPw.length > 5 && inputId.includes('@'))}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href={`/change`} variant="body2">
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link href={`/signup`} variant="body2">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
