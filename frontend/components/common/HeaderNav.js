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
import Link from 'next/link';
import { useCookies } from 'react-cookie';
import AddIcon from '@mui/icons-material/Add';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import { router, useRouter } from 'next/router';
import useAuth from '../../hooks/useAuth';
import axios from '../../utils/axios';
import { useRecoilState } from 'recoil';
import { profileState } from '../../state/profileState';
import { postDetailState } from '../../state/postDetailState';
import { notificationState } from '../../state/notificationState';
import Notification from '../Notification';

const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  border: 'none',
  backgroundColor: alpha('#9e9e9e', 0.15),
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(3),
    width: 'auto',
  },
  marginTop: '2px',
  '.MuiInputBase-input': {
    paddingLeft: '0',
    width: '0px',
    transition: 'width 1s',
    visibility: 'hidden',
  },
  '#xIcon': {
    visibility: 'hidden',
  },
  '&:focus-within': {
    border: 'solid',
    borderRadius: '4px',
    borderColor: 'white',
    '#xIcon': {
      visibility: 'visible',
    },
    '.MuiInputBase-input': {
      width: '150px',
      visibility: 'visible',
      paddingRight: '35px',
    },
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
  const [isInput, setIsInput] = useState(false);

  const handleProfileMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = async button => {
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
        // console.log(data);
      } catch (e) {
        console.log(e);
      }
    }
    setAnchorEl(null);
  };

  const handleLogout = async () => {
    try {
      // console.log('cookie : ', cookies['InfoMansionAccessToken']);
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
      {anchorEl?.id === 'profile' && (
        <>
          <MenuItem
            onClick={() => {
              handleMenuClose();
              window.location.replace(`/${simpleUser.username}`);
            }}
          >
            마이룸
          </MenuItem>
          <MenuItem
            onClick={() => {
              handleMenuClose();
              handleLogout();
            }}
          >
            로그아웃
          </MenuItem>
        </>
      )}
      {anchorEl?.id === 'notification' && (
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
            <Link style={{position : 'absolute', zIndex : 10}} href="/">
              <div
                style={{
                  display: 'flex',
                  height: '30px',
                  alignItems: 'center',
                  cursor: 'pointer',
                }}
              >
                <Image src="/vectorLogo.svg" alt="" width={40} height={40} />
                {pathname != '/[userName]' ? (
                  <div
                    style={{
                      color: 'white',
                      fontSize: '30px',
                      padding: '10px',
                    }}
                  >
                    InfoMansion
                  </div>
                ) : (
                  <></>
                )}
              </div>
            </Link>

            <div
              style={{ display: 'flex', height: '30px', alignItems: 'center' }}
            >
              <Search
                style={{
                  postiion: 'relative',
                  fontSize: '40px',
                  backgroundColor: 'transparent',
                  display: 'flex',
                  alignItems: 'center',
                  marginLeft: 0,
                }}
              >
                <IconButton style={{ padding: '1px 2px' }}>
                  <SearchIcon
                    style={{
                      color: 'white',
                      fontSize: '45px',
                      margin: '0 4px',
                    }}
                  />
                </IconButton>
                <InputBase
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
                    color: 'white',
                    fontSize: '20px',
                  }}
                />
                {keyword.length > 0 && (
                  <IconButton style={{ padding: '0' }}>
                    <HighlightOffIcon
                      id="xIcon"
                      style={{
                        top: '50%',
                        transform: 'translateY(-50%)',
                        right: '5',
                        position: 'absolute',
                        color: 'white',
                        cursor: 'pointer',
                        fontSize: '30px',
                      }}
                      onClick={() => {
                        setKeyword('');
                      }}
                    ></HighlightOffIcon>
                  </IconButton>
                )}
              </Search>
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
