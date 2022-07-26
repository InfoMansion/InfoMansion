import HeaderNav from './common/HeaderNav';
import { styled } from '@mui/material/styles';
import { Box, Paper } from '@mui/material';
import { useCookies } from 'react-cookie';
import axios from 'axios';

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

export default function Mainframe({ children }) {
  const [cookies] = useCookies(['cookie-name']);
  const handleSubmit = async () => {
    try {
      console.log(cookies['InfoMansionAccessToken']);
      const { data } = await axios.post(
        'http://localhost:8080/api/v1/auth/reissue',
        { accessToken: cookies['InfoMansionAccessToken'] },
        { withCredentials: true },
      );
      console.log('res : ', data);
    } catch (e) {
      console.log('error', e);
    }
  };
  return (
    <Box
      style={{
        backgroundColor: '#eeeeee',
      }}
      sx={{
        minHeight: '1500px',
      }}
    >
      <button onClick={handleSubmit}>test</button>
      <Paper elevation={2}>
        <HeaderNav />
      </Paper>

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
