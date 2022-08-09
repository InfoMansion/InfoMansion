import { Box, Button, Card, TextField } from "@mui/material";
import { OrbitControls, OrthographicCamera, Scroll, ScrollControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber"
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import DebugStuff from "../components/Debug/DebugStuff";
import axiosApiInstance from "../utils/axios";

export default function devView() {
    const [search, setSearch] = useState('WALL');
    const [stuffs, setStuffs] = useState([]);
    const [cookies] = useCookies(['cookie-name']);

    const multiconx = 4;
    const multicony = 6;
    const yoff = 7;

    useEffect(() => {
        // stuff 가져오기
        updatedata();
    }, []);

    function updatedata() {
        try {
            axiosApiInstance.get(`/api/v1/stuffs`, {
                headers: {
                    Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                },
            })
            .then(res => {
                console.log(res.data);
                
                setStuffs(res.data.data.filter( stuff => stuff.stuffType == search));
            });
        } catch (e) {
            console.log(e);
        }
    }

    function RenderStuff({stuff, index}) {
        let x = (index%3 -1) * multiconx;
        let y = parseInt(index/3) * -1 * multicony + yoff
        
        return (
            <group
                position={[x, y, 0]}
                scale={2}
            >
                <DebugStuff
                    data={stuff}
                >
                    
                </DebugStuff>
            </group>
        )
    }

    const onChange = (e) => {
        setSearch(e.target.value);
    }
    return (
        <Box>
            <Card>
                <input onChange={onChange} value={search}/>
                <Button 
                    variant="outlined"
                    onClick={updatedata}
                >불러오기</Button>
            </Card>
            <Canvas
                style={{
                    height : 700,
                    backgroundColor : '#aaaaaa'
                }}
            >
                <ScrollControls
                    vertical
                    damping={100}
                    pages={10}
                    >
                    <Scroll>
                        {stuffs.map( (stuff, i) => (
                            <RenderStuff stuff={stuff} index={i}/>
                        ))}
                    </Scroll>
                </ScrollControls>
                <OrthographicCamera position={[0,6,6]} makeDefault zoom={50}/>
                
            </Canvas>
            
        </Box>
    )
}