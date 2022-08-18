import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Modal,
  Typography,
} from '@mui/material';
import { OrthographicCamera } from '@react-three/drei';
import { Canvas } from '@react-three/fiber';
import { useEffect, useState } from 'react';
import ConfirmAlert from '../../ConfirmAlert';
import CustomAlert from '../../CustomAlert';
import axios from '../../../utils/axios';
import ShopStuff from './ShopStuff';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

export default function BuyModal({
  open,
  setOpen,
  nowStuff,
  cookies,
  getCreditFromServer,
}) {
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [agree, setAgree] = useState(false);
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertSeverity, setAlertSeverity] = useState('');
  const [alertMessage, setAlertMessage] = useState('');

  const BuyStuffSubmit = event => {
    event.preventDefault();
    setConfirmOpen(true);
    // if (!confirmOpen && agree) {
    //   const buyStuff = {
    //     stuffIds: [nowStuff.id],
    //   };
    //   try {
    //     axios
    //       .post('/api/v2/user-stuff', buyStuff, {
    //         headers: {
    //           Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
    //         },
    //       })
    //       .then(res => {
    //         alert('구매되었습니다!');
    //         getCreditFromServer();
    //       });
    //   } catch (e) {
    //     console.log(e);
    //     alert('구매에 실패하였습니다');
    // alert(e.response.data.message);
    // }
    // }
  };

  useEffect(() => {
    if (!confirmOpen && agree) {
      const buyStuff = {
        stuffIds: [nowStuff.id],
      };
      try {
        axios
          .post('/api/v2/user-stuff', buyStuff, {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            },
          })
          .then(res => {
            // alert('구매되었습니다!');
            setAlertMessage('구매되었습니다!');
            setAlertSeverity('success');
            setAlertOpen(true);
            getCreditFromServer();
            setAgree(false);
          })
          .then();
      } catch (e) {
        // console.log(e);
        // alert('구매에 실패하였습니다');
        setAlertMessage('구매에 실패했습니다!');
        setAlertSeverity('error');
        setAlertOpen(true);
        setAgree(false);
        // alert(e.response.data.message);
      }
    }
  }, [confirmOpen]);

  useEffect(() => {
    if (!alertOpen && alertMessage) {
      setOpen(false);
    }
  }, [alertOpen]);

  return (
    <Modal
      open={open}
      onClose={() => setOpen(false)}
      aria-labelledby="parent-modal-title"
      aria-describedby="parent-modal-description"
    >
      <Box sx={style}>
        <Canvas
          style={{
            height: '200px',
            width: '332px',
            backgroundColor: '#eeeeee',
          }}
        >
          <ShopStuff data={nowStuff} stuffscale={2} />
          <OrthographicCamera makeDefault position={[0, 0, 4]} zoom={30} />
          <ambientLight />
        </Canvas>

        <Card>
          <CardHeader
            title={nowStuff.stuffNameKor}
            titleTypographyProps={{ align: 'center' }}
            sx={{
              backgroundColor: theme =>
                theme.palette.mode === 'light'
                  ? theme.palette.grey[200]
                  : theme.palette.grey[700],
            }}
          />

          <CardContent>
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'baseline',
                mb: 2,
              }}
            >
              <Typography component="h2" variant="h3" color="text.primary">
                {nowStuff.price}
              </Typography>
              <Typography variant="h6" color="text.secondary">
                /Credit
              </Typography>
            </Box>
          </CardContent>

          <CardActions>
            <ConfirmAlert
              open={confirmOpen}
              setOpen={setConfirmOpen}
              content="가구를 구매하시겠습니까?"
              setAgree={setAgree}
              severity={alertSeverity}
              message={alertMessage}
            ></ConfirmAlert>
            <CustomAlert
              open={alertOpen}
              setOpen={setAlertOpen}
              severity={alertSeverity}
              message={alertMessage}
            ></CustomAlert>
            <Button
              fullWidth
              variant="outlined"
              color="error"
              onClick={BuyStuffSubmit}
            >
              Buy Stuff
            </Button>
          </CardActions>
        </Card>
      </Box>
    </Modal>
  );
}
