import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import FormLabel from '@mui/material/FormLabel';
import FormControl from '@mui/material/FormControl';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormHelperText from '@mui/material/FormHelperText';
import Checkbox from '@mui/material/Checkbox';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useState, useRef, useEffect } from 'react';
import useAuth from '../../hooks/useAuth';
import { profileState } from '../../state/profileState';
import CustomAlert from '../CustomAlert';
import axios from '../../utils/axios';
import categories from '../jsonData/category.json';

import { Avatar, Box, Card, Grid, Typography, TextField } from '@mui/material';
import { useCookies } from 'react-cookie';
import { useSetRecoilState } from 'recoil';
import { pageLoading } from '../../state/pageLoading';

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

export default function Profile({ ...props }) {
  const [cookies] = useCookies(['cookie-name']);
  const { auth, setAuth } = useAuth();
  const setPageLoading = useSetRecoilState(pageLoading);
  const userInfo = props.userInfo;
  const setUserInfo = props.setUserInfo;
  const [profileImageUrl, setProfileImageUrl] = useState(
    userInfo.profileImageUrl,
  );
  const [profileImage, setProfileImage] = useState('');
  const fileInput = useRef(null);
  const onChange = e => {
    if (e.target.files[0]) {
      setProfileImage(e.target.files[0]);
    } else {
      //업로드 취소할 시
      setProfileImageUrl(userInfo.profileImageUrl);
      return;
    }
    //화면에 프로필 사진 표시
    const reader = new FileReader();
    reader.onload = () => {
      if (reader.readyState === 2) {
        setProfileImageUrl(reader.result);
      }
    };
    reader.readAsDataURL(e.target.files[0]);
  };
  const [open, setOpen] = useState(false);
  const [severity, setSeverity] = useState('');
  const [message, setMessage] = useState('');

  const allCategories = {
    IT: false,
    COOK: false,
    MUSIC: false,
    GAME: false,
    SPORTS: false,
    FASHION: false,
    DAILY: false,
    TRAVEL: false,
    NATURE: false,
    ART: false,
    INTERIOR: false,
    CULTURE: false,
    BEAUTY: false,
    CLEANING: false,
    HOMEAPPLIANCES: false,
    STUDY: false,
  };
  const [categoryArray] = useState([
    'IT',
    'COOK',
    'MUSIC',
    'GAME',
    'SPORTS',
    'FASHION',
    'DAILY',
    'TRAVEL',
    'NATURE',
    'ART',
    'INTERIOR',
    'CULTURE',
    'BEAUTY',
    'CLEANING',
    'HOMEAPPLIANCES',
    'STUDY',
  ]);

  let selectedCate = '';
  const [state, setState] = useState(allCategories);
  const setProfileState = useSetRecoilState(profileState);
  const [changeCate, setChangeCate] = useState(selectedCate);

  useEffect(() => {
    userInfo.categories.forEach(function (category) {
      allCategories[category] = true;
      selectedCate = selectedCate + category + ',';
    });
    setState(allCategories);
    setChangeCate(selectedCate);
    setInputUsername(props.userInfo.username);
    setInputIntroduce(props.userInfo.introduce);
    setProfileImageUrl(props.userInfo.profileImageUrl);
  }, [props.userInfo]);

  const cateCount = changeCate.split(',').length - 1;
  const handleChange = event => {
    setState({
      ...state,
      [event.target.name]: event.target.checked,
    });
    if (event.target.checked) {
      const addCate = event.target.name;
      const updateCate = changeCate + addCate + ',';
      setChangeCate(updateCate);
    } else {
      const removeCate = event.target.name + ',';
      const updateCate = changeCate.replace(removeCate, '');
      setChangeCate(updateCate);
    }
  };

  const handleSubmit = async event => {
    event.preventDefault();
    const profileInfoJson = {
      username: inputUsername,
      categories: changeCate,
      introduce: inputIntroduce,
    };
    const profileInfo = JSON.stringify(profileInfoJson);
    const formData = new FormData();
    formData.append('profileImage', profileImage);
    formData.append(
      'profileInfo',
      new Blob([profileInfo], {
        type: 'application/json',
      }),
    );
    try {
      setPageLoading(true);
      const { data } = await axios.post('/api/v1/users/profile', formData, {
        headers: {
          ContentType: 'multipart/form-data',
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      setPageLoading(false);
      setUserInfo(data.data);
      setProfileState(true);
      setAuth({
        isAuthorized: true,
        accessToken: cookies.InfoMansionAccessToken,
        username: userInfo.username,
      });
      setSeverity('success');
      setMessage('프로필 수정이 완료됐습니다.');
      setOpen(true);
      // alert('프로필 수정이 완료됐습니다.');
    } catch (e) {
      setSeverity('error');
      setMessage(e.response.data.message);
      setOpen(true);
      // alert(e.response.data.message);
    }
    
    setTimeout( () => {
      setPageLoading(false);
    }, 1000)
  };

  const [inputUsername, setInputUsername] = useState(userInfo.username);
  const confirmUsername = /^[a-zA-Zㄱ-힣0-9_]{3,15}$/.test(inputUsername);
  const inputFinish = confirmUsername && cateCount >= 1 && cateCount <= 5;
  const [inputIntroduce, setInputIntroduce] = useState(userInfo.introduce);
  function handleInput(event) {
    event.preventDefault();
    const { name, value } = event.target;
    if (name === 'username') {
      setInputUsername(value);
    } else {
      setInputIntroduce(value);
    }
  }
  return (
    <ThemeProvider theme={theme}>
      <Container component="main">
        <CssBaseline />

        <Box
          component="form"
          encType="multipart/form-data"
          onSubmit={handleSubmit}
        >
          <CustomAlert
            open={open}
            setOpen={setOpen}
            severity={severity}
            message={message}
          ></CustomAlert>
          <Card
            sx={{
              py: 2,
            }}
          >
            <Box
              sx={{
                display: 'flex',
                alignItems: 'start',
              }}
              container
            >
              <Box
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  m: 2,
                }}
              >
                <Avatar
                  alt="profile"
                  src={profileImageUrl}
                  sx={{
                    width: '80px',
                    height: '80px',
                    mb: 1,
                  }}
                />
                <input
                  type="file"
                  style={{ display: 'none' }}
                  accept="image/jpg,image/png,image/jpeg"
                  name="profile_img"
                  onChange={onChange}
                  ref={fileInput}
                />
                <Button
                  variant="outlined"
                  onClick={() => {
                    fileInput.current.click();
                  }}
                >
                  사진 변경
                </Button>
              </Box>

              <Box sx={{ m: 2 }}>
                <Typography>userName</Typography>
                <TextField
                  value={inputUsername}
                  onChange={handleInput}
                  name="username"
                  helperText="3~15 글자가 가능하며(한글, 영어, 숫자 및 ' _ '로 구성 가능합니다)"
                />
              </Box>
            </Box>

            <Box sx={{ mx: 2 }}>
              <Typography> Introduce</Typography>
              <TextField
                multiline
                maxRows={5}
                name="introduce"
                value={inputIntroduce}
                onChange={handleInput}
                fullWidth
              />
            </Box>
          </Card>

          <ThemeProvider theme={theme}>
            <CssBaseline />
            <Card
              sx={{
                mt: 8,
                display: 'flex',
                backgroundColor: 'white',
              }}
            >
              <Box component="form">
                <Box sx={{ display: 'flex' }}>
                  <FormControl
                    sx={{ m: 3 }}
                    component="fieldset"
                    variant="standard"
                  >
                    <FormLabel component="legend">
                      Select Your Interest
                    </FormLabel>

                    <FormGroup>
                      <Grid container>
                        {categoryArray.map(category => (
                          <Grid item xs={3}>
                            <FormControlLabel
                              control={
                                <Checkbox
                                  checked={state[category]}
                                  onChange={handleChange}
                                  name={category}
                                />
                              }
                              label={categories[category].alias}
                            />
                          </Grid>
                        ))}
                      </Grid>
                    </FormGroup>
                    <FormHelperText>
                      1개 이상, 5개 이하를 골라주세요
                    </FormHelperText>
                  </FormControl>
                </Box>
              </Box>
            </Card>
          </ThemeProvider>

          <Box
            sx={{
              display: 'flex',
              justifyContent: 'end',
            }}
          >
            <Button
              type="submit"
              variant="contained"
              sx={{ mt: 3, mb: 2, color: 'white' }}
              disabled={!inputFinish}
            >
              UPDATE
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
