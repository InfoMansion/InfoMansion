import { Box, IconButton, Typography } from "@mui/material";
import { OrthographicCamera, Scroll, ScrollControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";

import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';

import EditStuff from "./atoms/EditStuff";
import { useEffect, useState } from "react";

const multicon = 3;
const xoff = 0;
// 스터프 하나씩 반환.
function RenderStuff({stuff, index, posy, click}) {
    let x = (index-1) * multicon + xoff
    return (
        <group
            position={[x, posy, 0]}
            scale={0.8}
            onClick={click}
        >
            <EditStuff data={stuff} />
        </group>
    )
}

export default function MapStuffList({stuffs, posy, tag, click}) {
    const [page, setPage] = useState(0);
    const [viewStuffs, setViewStuffs] = useState([]);

    function handlePrev() {
        if(page == 0) return;
        
        const tmppage = page-1;
        let st = tmppage * 3;
    
        setPage(tmppage);
        makeStuffList(st);
    }
    function handleNext() {
        const tmppage = page + 1;
        let st = tmppage * 3;

        if(!stuffs[st]) return;

        setPage(tmppage);
        makeStuffList(st);
    }

    function makeStuffList(st) {
        setViewStuffs(stuffs.slice(st, st + 3));
    }

    useEffect(() => {
        makeStuffList(0);
    }, [stuffs])

    return (
        <Box>
            <Canvas
                style={{
                    width : '100%',
                    height : '120px',
                }}
                gl={{
                    antialias : false,
                }}
                dpr={[1, 1.5]} 
            >
                {viewStuffs.map((stuff, index) => 
                    <RenderStuff 
                        stuff={stuff} 
                        index={index} 
                        posy={posy}
                        key={index}
                        click={ () => click(stuff, tag)}
                    />
                )}

                <OrthographicCamera 
                    makeDefault
                    position={[0,0,4]}
                    zoom={50}
                />
            </Canvas>
            <Box
                sx={{
                  display : 'flex',
                  justifyContent : 'space-evenly',
                  alignItems : 'center',
                }}
              >
                <IconButton onClick={handlePrev}>
                    <ArrowBackIosIcon  />
                </IconButton>
                <Typography variant="h5" >
                    {page + 1}
                </Typography>
                <IconButton onClick={handleNext}>
                    <ArrowForwardIosIcon />
                </IconButton>
            </Box>
        </Box>
    )
}