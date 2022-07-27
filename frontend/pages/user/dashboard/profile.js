import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormLabel from '@mui/material/FormLabel';
import FormControl from '@mui/material/FormControl';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormHelperText from '@mui/material/FormHelperText';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useRecoilState } from 'recoil';
import { likeCateState } from '../../../state/likeCate';
import { useState } from 'react';
import { useRouter } from 'next/router';
import axios from 'axios';

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

export default function Profile() {
  const [likeCate, setlikeCate] = useRecoilState(likeCateState);

  const [inputUsername, setInputUsername] = useState('');

  const inputUnFinish = !inputUsername;

  const handleChange = () => {};

  function handleInput(event) {
    setInputUsername(value);
  }

  const handleSubmit = async event => {
    event.preventDefault();
    const credentials = {
      email: 'inputEmail',
      password: 'inputPassword',
      username: inputUsername,
      categories: likeCate,
      tel: '01012345678',
    };

    try {
      console.log(credentials);

      const res = await axios.post(
        'http://localhost:8080/api/v1/auth/signup',
        credentials,
      );
      console.log('인증메일이 발송되었습니다.');
      router.push('user/login');
    } catch (e) {
      console.log(e);
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
          <Avatar src="/logo.png">
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Profile
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
                  id="username"
                  label="username"
                  name="username"
                  autoComplete="username"
                  onChange={handleInput}
                  color="primary"
                  focused
                />
              </Grid>
            </Grid>
            <Box sx={{ display: 'flex' }}>
              <FormControl
                sx={{ m: 3 }}
                component="fieldset"
                variant="standard"
              >
                <FormLabel component="legend">Select Your Interest</FormLabel>
                <FormGroup>
                  <Grid>
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={gilad}
                          onChange={handleChange}
                          name="IT"
                        />
                      }
                      label="IT"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={jason}
                          onChange={handleChange}
                          name="COOK"
                        />
                      }
                      label="쿠킹"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="MUSIC"
                        />
                      }
                      label="음악"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="GAME"
                        />
                      }
                      label="게임"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="SPORTS"
                        />
                      }
                      label="스포츠"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="FASHION"
                        />
                      }
                      label="패션"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="TRAVEL"
                        />
                      }
                      label="여행"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="DAILY"
                        />
                      }
                      label="데일리"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="NATURE"
                        />
                      }
                      label="자연"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="ART"
                        />
                      }
                      label="미술"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="INTERIOR"
                        />
                      }
                      label="인테리어"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="BEAUTY"
                        />
                      }
                      label="뷰티"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="CLEANING"
                        />
                      }
                      label="청소"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="STUDY"
                        />
                      }
                      label="공부"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="HOMEAPPLIANCES"
                        />
                      }
                      label="가전제품"
                      xs={3}
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          // checked={antoine}
                          onChange={handleChange}
                          name="CULTURE"
                        />
                      }
                      label="시사/문화"
                      xs={3}
                    />
                  </Grid>
                </FormGroup>
                <FormHelperText>하나 이상을 골라주세요</FormHelperText>
              </FormControl>
            </Box>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={inputUnFinish}
            >
              UPDATE
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
