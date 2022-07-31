import HeaderNav from './common/HeaderNav';
import { styled } from '@mui/material/styles';
import { Box, Paper } from '@mui/material';
import { useRouter } from 'next/router';
import useAuth from '../hooks/useAuth';
import { useCookies } from 'react-cookie';

const Root = styled('div')(({ theme }) => ({
  padding: theme.spacing(1),
  margin: '30px auto',

  [theme.breakpoints.down('lg')]: {
    width: '700px',
  },
  [theme.breakpoints.up('lg')]: {
    width: '1280px',
  },
}));

export default function Layout({ children }) {
  const [cookies] = useCookies(['cookie-name']);
  const { pathname, push } = useRouter();
  const { auth } = useAuth();

  return (
    <Box
      style={{
        backgroundColor: '#eeeeee',
      }}
      sx={{
        minHeight: '1500px',
      }}
    >
      {auth.isAuthorized && (
        <Paper elevation={2}>
          {' '}
          <HeaderNav />
        </Paper>
      )}
      <Root
        style={{
          backgroundColor: '#ffffff',
        }}
        sx={{
          height: '100%',
        }}
      >
        {children}
      </Root>
    </Box>
  );
}
