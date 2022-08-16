import { OrthographicCamera, Scroll, ScrollControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";

import EditStuff from "./atoms/EditStuff";
import { useRecoilState } from "recoil";
import { categoryState, editingState, editStuffState, fromState, positionState, rotationState } from "../../state/editRoomState";
import { Box, IconButton, Typography } from "@mui/material";

import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { useEffect, useState } from "react";

const multicon = 3;
const yoff = 1.2;

// 스터프 하나씩 반환.
function RenderStuff({stuff, index, click}) {
    let x = (index%3 -1) * multicon;
    let y = parseInt(index/3) * -1 * multicon + yoff
    
    return (
        <group
            position={[x, y, 0]}
            scale={2}

            onClick={click}
        >
            <EditStuff data={stuff} />
        </group>
    )
}

export default function StuffList({stuffs}) {
    const [, setEditStuff] = useRecoilState(editStuffState);
    const [, setEditing] = useRecoilState(editingState);
    const [, setEditPosition] = useRecoilState(positionState);
    const [, setEditRotation] = useRecoilState(rotationState);
    const [, setFrom] = useRecoilState(fromState);
    const [, setEditCategory] = useRecoilState(categoryState);

    function click(stuff) {
        setEditStuff(stuff);
        setEditing(true);

        setEditPosition([stuff.posX, stuff.posY, stuff.posZ]);
        setEditRotation([stuff.rotX, stuff.rotY, stuff.rotZ]);
        setEditCategory("NONE");
        setFrom('unlocated');
    }

    const [page, setPage] = useState(0);
    const [viewStuffs, setViewStuffs] = useState([]);

    function handlePrev() {
        if(page == 0) return;
        
        const tmppage = page-1;
        let st = tmppage * 6;
    
        setPage(tmppage);
        makeStuffList(st);
    }
    function handleNext() {
        const tmppage = page + 1;
        let st = tmppage * 6;

        if(!stuffs[st]) return;

        setPage(tmppage);
        makeStuffList(st);
    }

    function makeStuffList(st) {
        setViewStuffs(stuffs.slice(st, st + 6));
    }

    useEffect(() => {
        makeStuffList(0);
    }, [stuffs])

    return (
        <Box>
            <Canvas
                style={{
                    width : '100%',
                    height : '300px',
                }}
            >
                {viewStuffs.map((stuff, index) => (
                    <RenderStuff 
                        index={index} key={index}
                        stuff={stuff} 
                        click={() => click(stuff)}
                    />
                ))}
                <OrthographicCamera 
                    makeDefault
                    position={[0,0, 4]}
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