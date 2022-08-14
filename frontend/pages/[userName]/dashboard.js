import { useState, useEffect } from 'react';
import { styled, createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import MuiDrawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import Container from '@mui/material/Container';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import { useRecoilState } from 'recoil';

import { dashNumState } from '../../state/dashNum';

import MainListItems from '../../components/DashboardPage/mainlist.js';
import Profile from '../../components/DashboardPage/profile';
import Change from '../../components/DashboardPage/change';
import Alarm from '../../components/DashboardPage/alarm';
import Privacy from '../../components/DashboardPage/privacy.js';
import Confirm from '../../components/DashboardPage/confirm.js';

const drawerWidth = 240;

const Drawer = styled(MuiDrawer, {
  shouldForwardProp: prop => prop !== 'open',
})(({ theme, open }) => ({
  '& .MuiDrawer-paper': {
    position: 'relative',
    whiteSpace: 'nowrap',
    backgroundColor : 'transparent',
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
    boxSizing: 'border-box',
    ...(!open && {
      overflowX: 'hidden',
      transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      width: theme.spacing(7),
      [theme.breakpoints.up('sm')]: {
        width: theme.spacing(9),
      },
    }),
  },
}));

const mdTheme = createTheme({
  palette: {
    primary: {
      light: '#ff7961',
      main: '#ffffff',
      dark: '#fc7a71',
      contrastText: 'white',
    }
  }
});

function DashboardContent(props) {
  const [dashNum, setDashNum] = useRecoilState(dashNumState);
  const [open, setOpen] = useState(true);
  const toggleDrawer = () => {
    setOpen(!open);
  };
  let content;
  if (dashNum === 0) {
    content = (
      <Profile
        userInfo={props.userInfo}
        setUserInfo={props.setUserInfo}
      />
    );} 
  else if (dashNum === 1) { content = <Change />; } 
  else if (dashNum === 2) { content = <Alarm />; } 
  else if (dashNum === 3) { content = <Privacy />; }

  return (
    <ThemeProvider theme={mdTheme}>
      <Container sx={{ display : 'flex' }}>

        {/* navbar */}
        <Drawer
          variant="permanent" 
          open={open}
          sx={{color : 'primary.main'}}
        >
          <Toolbar
            sx={{
              display: 'flex',
              justifyContent: 'flex-end',
              px: [1],
            }}
          >
            <IconButton onClick={toggleDrawer}>
              <ChevronLeftIcon color="primary" />
            </IconButton>
          </Toolbar>
          
          <Divider color="white"/>

          <List component="nav"
            sx={{color : 'primary.main'}}
          >
            <MainListItems/>
            <Divider sx={{ my: 1 }} color="white" />
          </List>
        </Drawer>
        
        <Divider color={'white'} sx={{my : 2}} orientation="vertical"/>

        {/* dashboard Contents */}
        <Box
          component="main"
          sx={{
            backgroundColor: 'rgba(255,255,255,0)',
            flexGrow: 1,
            height: window.innerHeight,
          }}
        >
          <Box sx={{ mt: 4, mb: 4 }}>
              {content}
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}

export default function Dashboard() {
  const [isConfirmed, setIsConfirmed] = useState(false);
  let dashContent;
  const [userInfo, setUserInfo] = useState({
    username: '',
    email: '',
    categories: [],
    introduce: '',
    profileImageUrl: '',
  });

  if (isConfirmed) {
    dashContent = (
      <DashboardContent
        userInfo={userInfo}
        setUserInfo={setUserInfo}
      ></DashboardContent>
    );
  } else {
    dashContent = (
      <Confirm
        setUserInfo={setUserInfo}
        setIsConfirmed={setIsConfirmed}
      ></Confirm>
    );
  }

  return (
    <Box
      sx={{
        height : window.innerHeight - 80,
      }}
    >
      {dashContent}
    </Box>
  )
}
