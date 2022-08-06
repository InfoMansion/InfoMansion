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
import axios from '../../utils/axios';
import {
  Avatar,
  Box,
  Card,
  Divider,
  Grid,
  Typography,
  TextField,
} from '@mui/material';
import { styled } from '@mui/material/styles';
import { useCookies } from 'react-cookie';
import { useRecoilState, useSetRecoilState } from 'recoil';
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

const Root = styled('div')(({ theme }) => ({
  padding: theme.spacing(1),
  margin: '30px auto',

  [theme.breakpoints.down('lg')]: {
    width: '600px',
  },
  [theme.breakpoints.up('lg')]: {
    width: '1280px',
  },
}));

export default function Profile({ ...props }) {
  const [cookies] = useCookies(['cookie-name']);
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

  let selectedCate = '';

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

  const [state, setState] = useState(allCategories);

  const [changeCate, setChangeCate] = useState(selectedCate);
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
    } catch (e) {
      setPageLoading(false);
      alert(e.response.data.message);
    }
  };

  const {
    IT,
    COOK,
    MUSIC,
    GAME,
    DAILY,
    FASHION,
    NATURE,
    TRAVEL,
    ART,
    INTERIOR,
    CULTURE,
    BEAUTY,
    CLEANING,
    HOMEAPPLIANCES,
    STUDY,
    SPORTS,
  } = state;

  const [inputUsername, setInputUsername] = useState(userInfo.username);
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
          <Card>
            <Grid
              sx={{
                p: 2,
              }}
              container
            >
              <Grid item xs={3}>
                <Avatar
                  alt="profile"
                  src={profileImageUrl}
                  sx={{
                    width: '100%',
                    maxWidth: '80px',
                    height: '100%',
                    maxHeight: '80px',
                  }}
                  onClick={() => {
                    fileInput.current.click();
                  }}
                />
                <input
                  type="file"
                  style={{ display: 'none' }}
                  accept="image/jpg,impge/png,image/jpeg"
                  name="profile_img"
                  onChange={onChange}
                  ref={fileInput}
                />
              </Grid>
              <Grid item xs={9}>
                <TextField
                  value={inputUsername}
                  onChange={handleInput}
                  name="username"
                ></TextField>
              </Grid>

              <Divider />
              <TextField
                multiline
                maxRows={5}
                name="introduce"
                value={inputIntroduce}
                onChange={handleInput}
                fullWidth
              ></TextField>
            </Grid>
          </Card>
          <ThemeProvider theme={theme}>
            <Container component="main">
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
                <Box component="form" noValidate sx={{ mt: 3 }}>
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
                        <Grid>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={IT}
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
                                checked={COOK}
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
                                checked={MUSIC}
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
                                checked={GAME}
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
                                checked={SPORTS}
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
                                checked={FASHION}
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
                                checked={TRAVEL}
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
                                checked={DAILY}
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
                                checked={NATURE}
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
                                checked={ART}
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
                                checked={INTERIOR}
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
                                checked={BEAUTY}
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
                                checked={CLEANING}
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
                                checked={STUDY}
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
                                checked={HOMEAPPLIANCES}
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
                                checked={CULTURE}
                                onChange={handleChange}
                                name="CULTURE"
                              />
                            }
                            label="시사/문화"
                            xs={3}
                          />
                        </Grid>
                      </FormGroup>
                      <FormHelperText>
                        1개 이상, 5개 이하를 골라주세요
                      </FormHelperText>
                    </FormControl>
                  </Box>
                </Box>
              </Box>
            </Container>
          </ThemeProvider>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2, color: 'white' }}
            disabled={cateCount > 5 || cateCount < 1}
          >
            UPDATE
          </Button>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
