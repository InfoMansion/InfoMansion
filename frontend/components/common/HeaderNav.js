import * as React from 'react';
import { styled, alpha } from '@mui/material/styles';
import InputBase from '@mui/material/InputBase';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Image from 'next/image';
import Typography from '@mui/material/Typography';
import SearchIcon from '@mui/icons-material/Search';
import IconButton from '@mui/material/IconButton';
import MoreIcon from '@mui/icons-material/MoreVert';
import Badge from '@mui/material/Badge';
import Menu from '@mui/material/Menu';
import NotificationsIcon from '@mui/icons-material/Notifications';
import MenuItem from '@mui/material/MenuItem';
import AccountCircle from '@mui/icons-material/AccountCircle';
import Link from 'next/link';

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

const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: 'inherit',
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
}));

export default function DenseAppBar() {
  const [anchorEl, setAnchorEl] = React.useState(null);

  const isMenuOpen = Boolean(anchorEl);

  const handleProfileMenuOpen = event => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
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
      <MenuItem onClick={handleMenuClose}>프로필</MenuItem>
      <Link href="/RoomPage">
        <MenuItem onClick={handleMenuClose}>마이룸</MenuItem>
      </Link>
      <MenuItem onClick={handleMenuClose}>로그아웃</MenuItem>
    </Menu>
  );
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" style={{ background: '#FFFFFF' }}>
        <Toolbar variant="dense" style={{ justifyContent: 'space-evenly' }}>
          <Link href="/">
            <div
              style={{
                display: 'flex',
                height: '30px',
                alignItems: 'center',
                cursor: 'pointer',
              }}
            >
              <Image src="/logo.png" alt="" width={30} height={30} />
              <div
                style={{ color: 'black', fontSize: '20px', padding: '10px' }}
              >
                InfoMansion
              </div>
            </div>
          </Link>
          <Search>
            <SearchIconWrapper style={{ color: '#9e9e9e' }}>
              <SearchIcon />
            </SearchIconWrapper>
            <StyledInputBase
              placeholder="검색"
              inputProps={{ 'aria-label': 'search' }}
              style={{ color: 'black' }}
            />
          </Search>
          <div
            style={{ display: 'flex', height: '30px', alignItems: 'center' }}
          >
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
          <Typography variant="h6" color="inherit" component="div"></Typography>
        </Toolbar>
      </AppBar>
      {renderMenu}
    </Box>
  );
}
