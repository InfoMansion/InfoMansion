import { Typography, Box, Card, Divider } from "@mui/material";
import { useEffect, useState } from "react";
import ShowWindow from "../components/ShopPage/ShowWindow";

import { Canvas } from "@react-three/fiber";
import { OrthographicCamera } from "@react-three/drei";
import { useCookies } from "react-cookie";
import axios from "../utils/axios";
import { pageLoading } from '../state/pageLoading';
import { useSetRecoilState } from "recoil";

export default function Shop() {
    const [cookies] = useCookies(['cookie-name']);
    // 실제로는 db에서 카테고리 받아오기.
    const [scrollTarget] = useState(0);
    const setPageLoading = useSetRecoilState(pageLoading);
    const [stuffBundles, setStuffBundles] = useState([]);

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
                            />

                        <OrthographicCamera 
                            makeDefault
                            position={[0,0, 4]}
                            zoom={50}   
                        />
                        </Canvas>
                </Card>
                )}
        </Box>
    )
}