import React, { useCallback, useState, useEffect } from 'react';
import { styled, alpha } from '@mui/material/styles';
import {
  InputBase,
  AppBar,
  Box,
  Toolbar,
  IconButton,
  Badge,
  Menu,
  MenuItem,
} from '@mui/material';
import Image from 'next/image';
import SearchIcon from '@mui/icons-material/Search';
import NotificationsIcon from '@mui/icons-material/Notifications';
import AccountCircle from '@mui/icons-material/AccountCircle';
import Link from 'next/link';
import { useCookies } from 'react-cookie';
import AddIcon from '@mui/icons-material/Add';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import { useRouter } from 'next/router';
import useAuth from '../../hooks/useAuth';
import axios from '../../utils/axios';
import { useRecoilState } from 'recoil';
import { profileState } from '../../state/profileState';
import { postDetailState } from '../../state/postDetailState';
import { notificationState } from '../../state/notificationState';
import Nofication from '../Notification';
import Notification from '../Notification';

const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}));
const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha('#9e9e9e', 0.15),
  marginRight: theme.spacing(2),
  marginLeft: 0,
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(3),
    width: 'auto',
  },
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: 'gray',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1, 1, 1, 0),
    // vertical padding + font size from searchIcon
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('md')]: {
      width: '20ch',
    },
  },
  '&: focus-within': {
    color: 'black',
  },
}));

export default function HeaderNav() {
  const { push, pathname } = useRouter();
  const [anchorEl, setAnchorEl] = useState(null);
  const [cookies, , removeCookies] = useCookies(['cookie-name']);
  const isMenuOpen = Boolean(anchorEl);
  const [simpleUser, setSimpleUser] = useState({
    username: '',
    profileImage: '',
  });
  const { auth, setAuth } = useAuth();
  const [keyword, setKeyword] = useState('');
  const [profile, setProfileState] = useRecoilState(profileState);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [notification, setNotification] = useRecoilState(notificationState);
  const [showModal, setShowModal] = useState(false);

  //const [auth, setAuth] = useAuth();

  const handleProfileMenuOpen = event => {
    console.log(event.currentTarget);
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = async button => {
    setAnchorEl(null);
    if (button === 'notification') {
      try {
        const { data } = await axios.post(
          '/api/v1/notifications',
          {},
          {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            },
          },
        );
        setNotification([]);
        console.log(data);
      } catch (e) {
        console.log(e);
      }
    }
  };

  const openModal = () => {
    setShowModal(true);
  };

  const handleLogout = async () => {
    try {
      console.log('cookie : ', cookies['InfoMansionAccessToken']);
      const { data } = await axios.get('/api/v1/auth/logout', {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      });
      removeCookies('InfoMansionAccessToken', { path: '/' });
      setAuth({
        isAuthorized: false,
        accessToken: undefined,
        username: undefined,
      });
      window.localStorage.removeItem('expiresAt');
      if (pathname !== '/') window.location.replace('/');
    } catch (e) {
      console.log(e);
    }
  };

  const handleChange = e => {
    setKeyword(e.target.value);
  };

  const init = useCallback(async () => {
    try {
      const { data } = await axios.get('/api/v1/users/info/simple', {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });

      setSimpleUser(data.data);
      setProfileState(false);
    } catch (e) {
      console.log(e);
    }
  }, [cookies]);

  useEffect(() => {
    setAuth({
      isAuthorized: true,
      accessToken: cookies.InfoMansionAccessToken,
      username: simpleUser.username,
    });
  }, [simpleUser]);

  useEffect(() => {
    if (!auth.isAuthorized || !cookies.InfoMansionAccessToken) {
      return;
    }
    init();
  }, [init, auth.isAuthorized, profile]);

  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: 'bottom',
        horizontal: 'left',
      }}
      //id={menuId}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'left',
      }}
      open={isMenuOpen}
      onClose={() => handleMenuClose(anchorEl?.id)}
    >
      {anchorEl?.id === 'profile' ? (
        <>
          <Link href={`/${simpleUser.username}`}>
            <MenuItem onClick={handleMenuClose}>마이룸</MenuItem>
          </Link>
          <MenuItem
            onClick={() => {
              handleMenuClose();
              handleLogout();
            }}
          >
            {anchorEl?.id}
            로그아웃
          </MenuItem>
        </>
      ) : (
        <>
          {notification.length === 0 ? (
            <div>알림이 없습니다.</div>
          ) : (
            <>
              {notification.map(noti => (
                <Notification notification={noti} />
              ))}
            </>
          )}
        </>
      )}
    </Menu>
  );
  return (
    <>
      <Box sx={{ flexGrow: 1 }}>
        <AppBar
          elevation={0}
          position="static"
          style={{ background: 'transParent' }}
        >
          <Toolbar
            variant="dense"
            style={{
              justifyContent: 'space-between',
              maxWidth: '1280px',
              width: '100%',
              height: '80px',
              margin: '0 auto',
            }}
          >
            <Link href="/">
              <div
                style={{
                  display: 'flex',
                  height: '30px',
                  alignItems: 'center',
                  cursor: 'pointer',
                }}
              >
                <Image src="/vectorLogo.svg" alt="" width={40} height={40} />
                <div
                  style={{ color: 'white', fontSize: '30px', padding: '10px' }}
                >
                  InfoMansion
                </div>
              </div>
            </Link>
            <Search
              style={{
                postiion: 'relative',
                fontSize: '40px',
                border: 'solid',
                borderColor: 'white',
                backgroundColor: 'transparent',
                display: 'flex',
                alignItems: 'center',
              }}
            >
              <SearchIconWrapper style={{ color: '#9e9e9e' }}>
                <SearchIcon style={{ color: 'white', fontSize: '30px' }} />
              </SearchIconWrapper>
              <StyledInputBase
                placeholder="검색"
                onChange={handleChange}
                value={keyword}
                onKeyPress={event => {
                  event.key === 'Enter'
                    ? push({
                        pathname: '/post/SearchPost',
                        query: { keyword },
                      })
                    : '';
                }}
                style={{
                  paddingRight: '25px',
                  color: 'white',
                  paddingLeft: '7px',
                }}
              />
              {keyword.length > 0 && (
                <HighlightOffIcon
                  style={{
                    top: '50%',
                    transform: 'translateY(-50%)',
                    right: '5',
                    position: 'absolute',
                    color: 'white',
                    cursor: 'pointer',
                    fontSize: '20px',
                  }}
                  onClick={() => {
                    setKeyword('');
                  }}
                ></HighlightOffIcon>
              )}
            </Search>
            <div
              style={{ display: 'flex', height: '30px', alignItems: 'center' }}
            >
              <Link href="/post/CreatePost">
                <IconButton
                  baseClassName="fas"
                  className="fa-plus-circle"
                  size="large"
                  color="inherit"
                  style={{ color: '#9e9e9e' }}
                  onClick={() => {
                    setPostDetail('');
                  }}
                >
                  <AddIcon style={{ color: 'white', fontSize: '50px' }} />
                </IconButton>
              </Link>
              <IconButton
                size="large"
                aria-label="show 17 new notifications"
                id="notification"
                aria-controls="notification"
                color="inherit"
                style={{ color: '#FFFFFF' }}
                onClick={handleProfileMenuOpen}
              >
                <Badge badgeContent={notification.length} color="error">
                  <NotificationsIcon
                    style={{ color: 'white', fontSize: '40px' }}
                  />
                </Badge>
              </IconButton>
              <IconButton
                size="large"
                edge="end"
                aria-label="account of current user"
                id="profile"
                aria-controls="profile"
                aria-haspopup="false"
                onClick={handleProfileMenuOpen}
                color="inherit"
                style={{ color: '#9e9e9e' }}
              >
                <img
                  src={`${simpleUser.profileImage}`}
                  style={{ height: '50px', width: '50px', borderRadius: '50%' }}
                />
              </IconButton>
            </div>
          </Toolbar>
        </AppBar>
        {renderMenu}
      </Box>
    </>
  );
}
