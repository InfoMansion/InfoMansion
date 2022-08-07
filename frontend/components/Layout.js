import HeaderNav from './common/HeaderNav';
import { styled } from '@mui/material/styles';
import { Box, Paper } from '@mui/material';
import useAuth from '../hooks/useAuth';
import { useRecoilState } from 'recoil';
import { pageLoading } from '../state/pageLoading';
import Loading from './Loading';
import { useRouter } from 'next/router';

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
  const { auth } = useAuth();
  const [loading] = useRecoilState(pageLoading);
  const { pathname } = useRouter();

  return (
    <Box
      style={{
        backgroundColor: '#eeeeee',
        position: 'relative',
      }}
      sx={{
        minHeight: '1200px',
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
          width: pathname === '/' ? '100%' : undefined,
          margin: pathname === '/' ? '1px 0 0 0' : undefined,
        }}
        sx={{
          height: '100%',
        }}
      >
        {children}
      </Root>
      {loading && <Loading></Loading>}
    </Box>
  );
}
