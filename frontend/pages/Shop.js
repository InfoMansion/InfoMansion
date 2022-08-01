import { Paper, Typography, Box } from "@mui/material";
import { useState } from "react";
import ShowWindow from "../components/ShopPage/ShowWindow";

// furniture.json안에 stuff종류와 스터프들이 다 들어가야 함.
import furnitures from '../components/ShopPage/furnitures.json'
import furnitureTypes from '../components/ShopPage/furnitureTypes.json'
import { Canvas } from "@react-three/fiber";
import { OrbitControls, OrthographicCamera } from "@react-three/drei";

export default function Shop() {
    // 실제로는 db에서 카테고리 받아오기.
    const [scrollTarget] = useState(0);

    return (
        <Box>
            <Box>
                크레딧이랑, 그런거 보여주기. navbar가 될 예정.
            </Box>
            <Box>
                Stuff 클릭시 상세 정보 보여줄 위치
            </Box>


            { furnitureTypes.map( type => 
                    <Box
                        key={type}
                        sx={{
                            mx : 2
                        }}
                    >
                        {
                            (furnitures[type]) ? 
                            <Box>
                                <Typography variant='h5'>{type}</Typography>
                                {/* <Canvas orthographic camera={{ position: [0, 0, 5], fov: 50 }}>
                                    <ShowWindowt scrollTarget={scrollTarget} />
                                </Canvas> */}
                                <Canvas
                                    style={{
                                        height : '200px',
                                        backgroundColor : '#aaaaaa'
                                    }}
                                >
                                    <ShowWindow
                                        ScrollTarget={scrollTarget}
                                        type={type}
                                        furnitures={furnitures[type]}
                                    />

                                <OrthographicCamera 
                                    makeDefault
                                    position={[0,0, 4]}
                                    zoom={50}   
                                />
                                </Canvas>
                            </Box>
                            : <></>
                        }
                    </Box>
                )}
        </Box>
    )
}