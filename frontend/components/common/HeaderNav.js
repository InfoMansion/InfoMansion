import React, { useState } from 'react';
import { styled, alpha } from '@mui/material/styles';
import InputBase from '@mui/material/InputBase';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Image from 'next/image';
import SearchIcon from '@mui/icons-material/Search';
import IconButton from '@mui/material/IconButton';
import Badge from '@mui/material/Badge';
import Menu from '@mui/material/Menu';
import NotificationsIcon from '@mui/icons-material/Notifications';
import MenuItem from '@mui/material/MenuItem';
import AccountCircle from '@mui/icons-material/AccountCircle';
import Link from 'next/link';
import { useCookies } from 'react-cookie';
import AddIcon from '@mui/icons-material/Add';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import { useRouter } from 'next/router';
import useAuth from '../../hooks/useAuth';
import axios from '../../utils/axios';

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
  const [focus, setFocus] = useState(false);

  const { setAuth } = useAuth();
  const [cookies, , removeCookies] = useCookies(['cookie-name']);
  const isMenuOpen = Boolean(anchorEl);

  const [keyword, setKeyword] = useState('');

  const handleProfileMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
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
      setAuth({ isAuthorized: false, accessToken: undefined });
      window.localStorage.removeItem('expiresAt');
      if (pathname !== '/') window.location.replace('/');
    } catch (e) {
      console.log(e);
    }
  };

  const handleChange = e => {
    setKeyword(e.target.value);
  };

  const menuId = 'primary-search-account-menu';
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: 'bottom',
        horizontal: 'left',
      }}
      id={menuId}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'left',
      }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <Link href="/1">
        <MenuItem onClick={handleMenuClose}>마이룸</MenuItem>
      </Link>

      <MenuItem
        onClick={() => {
          handleMenuClose();
          handleLogout();
        }}
      >
        로그아웃
      </MenuItem>
    </Menu>
  );
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" style={{ background: '#FFFFFF' }}>
        <Toolbar
          variant="dense"
          style={{
            justifyContent: 'space-between',
            maxWidth: '1280px',
            width: '100%',
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
              <Image src="/vectorLogo.svg" alt="" width={30} height={30} />
              <div
                style={{ color: 'black', fontSize: '20px', padding: '10px' }}
              >
                InfoMansion
              </div>
            </div>
          </Link>
          <Search style={{ postiion: 'relative' }}>
            <SearchIconWrapper style={{ color: '#9e9e9e' }}>
              <SearchIcon />
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
              style={{ paddingRight: '25px' }}
            />
            {keyword.length > 0 && (
              <HighlightOffIcon
                style={{
                  top: '50%',
                  transform: 'translateY(-50%)',
                  right: '5',
                  position: 'absolute',
                  color: '#9e9e9e',
                  cursor: 'pointer',
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
              >
                <AddIcon />
              </IconButton>
            </Link>
            <IconButton
              size="large"
              aria-label="show 17 new notifications"
              color="inherit"
              style={{ color: '#9e9e9e' }}
            >
              <Badge badgeContent={3} color="error">
                <NotificationsIcon />
              </Badge>
            </IconButton>
            <IconButton
              size="large"
              edge="end"
              aria-label="account of current user"
              aria-controls={menuId}
              aria-haspopup="false"
              onClick={handleProfileMenuOpen}
              color="inherit"
              style={{ color: '#9e9e9e' }}
            >
              <AccountCircle />
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
      {renderMenu}
    </Box>
  );
}
