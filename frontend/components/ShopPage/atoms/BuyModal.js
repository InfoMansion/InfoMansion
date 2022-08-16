import { Box, Button, Card, CardActions, CardContent, CardHeader, Modal, Typography } from "@mui/material";
import { OrthographicCamera } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import axios from "../../../utils/axios";
import ShopStuff from "./ShopStuff";

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

export default function BuyModal({ open, setOpen, nowStuff, cookies, getCreditFromServer }) {
    const BuyStuffSubmit = async event => {
        event.preventDefault();
        if (window.confirm('가구를 구매하시겠습니까?')) {
          const buyStuff = {
            stuffIds: [nowStuff.id],
          };
          try {
            axios.post('/api/v2/user-stuff', buyStuff, {
              headers: {
                Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              },
            })
            .then(res => {
              alert('구매되었습니다!');
              getCreditFromServer();
            })
          } catch (e) {
            console.log(e);
            alert("구매에 실패하였습니다");
            // alert(e.response.data.message);
          }
        }
    };

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
              width : '332px',
              backgroundColor: '#eeeeee',
            }}
          >
            <ShopStuff 
                data={nowStuff}
                stuffscale={2}    
            />
            <OrthographicCamera 
                makeDefault 
                position={[0,0,4]}  
                zoom={30}
            />
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
              <Button fullWidth variant="outlined" color="error" onClick={BuyStuffSubmit}>
                Buy Stuff
              </Button>
            </CardActions>
          </Card>
        </Box>
      </Modal>
    );
  }