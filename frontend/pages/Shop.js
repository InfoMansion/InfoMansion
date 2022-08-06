import { Typography, Box, Card, Divider, Modal } from "@mui/material";
import { useEffect, useState } from "react";
import ShowWindow from "../components/ShopPage/ShowWindow";

import { Canvas } from "@react-three/fiber";
import { Circle, OrbitControls, OrthographicCamera } from "@react-three/drei";
import { useCookies } from "react-cookie";
import axios from "../utils/axios";
import { pageLoading } from '../state/pageLoading';
import { useSetRecoilState } from "recoil";
import ShopStuff from "../components/ShopPage/atoms/ShopStuff";

export default function Shop() {
    const [cookies] = useCookies(['cookie-name']);
    const [scrollTarget] = useState(0);
    const setPageLoading = useSetRecoilState(pageLoading);
    const [stuffBundles, setStuffBundles] = useState([]);

    const [nowStuff, setNowStuff] = useState({});
    const [open, setOpen] = useState(false);

    useEffect(() => {
        try {
          setPageLoading(true);
          axios.get(`/api/v1/stores?pageSize=10`, {
              headers: {
                Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              },
            })
            .then(res => {
            //   setStuffBundles(res.data.data);
              setStuffBundles(res.data.data.slice(5, 10));
              setPageLoading(false);
            });
        } catch (e) {
            setPageLoading(false);
            console.log(e);
        }
    }, []);

    function click(e, stuff) {
        setNowStuff(stuff);
        console.log(stuff);
        setOpen(true);
    }
    function close() {
        setOpen(false);
    }

    function BuyModal() {
        return (
            <Modal
                open={open}
                onClose={close}
                aria-labelledby="parent-modal-title"
                aria-describedby="parent-modal-description"
                style={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    bgcolor: 'background.paper',
                    border: '2px solid #000',
                    boxShadow: 24,
                    p: 4,
                }}
            >
                <Box sx={{  width: 400 }}>
                    <Canvas
                        style={{
                            height : '200px',
                            backgroundColor : '#eeeeee'
                        }}
                    >

                        <ShopStuff 
                            data={nowStuff}
                            pos={[0, 0, 0]}
                            dist={0}
                        />
                        <ambientLight />
                        {/* <OrthographicCamera
                            makeDefault
                            postion={[8, 8, 9]}
                            zoom={50}
                        /> */}
                    </Canvas>
                </Box>
            </Modal>
        )
    }

    return (
        <Box>
            <Typography variant="h4">
                Shop
            </Typography>
            <Divider />
            {/* <Box>
                크레딧이랑, 그런거 보여주기. navbar가 될 예정.
            </Box> */}
            { stuffBundles.map( stuffBundle => 
                    <Card
                        key={stuffBundle.stuffType}
                        sx={{
                            m : 2
                        }}
                    >
                        <Typography variant='h5'
                            sx={{
                                m : 1
                            }}
                        >
                            {stuffBundle.stuffTypeName}
                        </Typography>

                        <Canvas
                            style={{
                                height : '200px',
                                backgroundColor : '#eeeeee'
                            }}
                            sx={{
                                m : 1
                            }}
                        >
                            <ShowWindow
                                ScrollTarget={scrollTarget}
                                stuffs={stuffBundle.slice.content}
                                click={click}
                            />

                        <OrthographicCamera 
                            makeDefault
                            position={[0,0, 4]}
                            zoom={50}   
                        />
                        </Canvas>
                </Card>
                )}
            <BuyModal />
        </Box>
    )
}