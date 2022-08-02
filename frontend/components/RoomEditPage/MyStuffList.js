import { Box, Typography } from "@mui/material";
import { Circle, OrbitControls, OrthographicCamera, Scroll, ScrollControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import EditStuff from "./EditStuff";

const multicon = 2.5;
const yoff = 2.5;

function RenderStuff({stuff, index}) {
    let x = (index%3 -1) * multicon;
    let y = parseInt(index/3) * -1 * multicon + yoff
    
    return (
        <group
            position={[x, y, 0]}
            scale={2}
        >
            <EditStuff 
                data={stuff}
            >

            </EditStuff>
        </group>
    )
}

export default function MyStuffList({stuffs}) { 
    const arr = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    return (
        <Canvas
            style={{
                width : '100%',
                height : '400px',
                backgroundColor : '#aaaaaa'
            }}
        >
            <ScrollControls
                vertical
                damping={2}
                pages={2}
            >
                <Scroll>
                    {stuffs.map((stuff, index) => (
                        <RenderStuff stuff={stuff} index={index} />
                    ))}
                </Scroll>
            </ScrollControls>
            
            <OrthographicCamera 
                makeDefault
                position={[0,0, 4]}
                zoom={50}   
            />
        {/* <OrbitControls></OrbitControls> */}
        </Canvas>
    )
}