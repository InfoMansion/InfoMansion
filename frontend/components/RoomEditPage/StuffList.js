import { OrthographicCamera, Scroll, ScrollControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";

import EditStuff from "./atoms/EditStuff";

const multicon = 2.5;
const yoff = 1;

// 스터프 하나씩 반환.
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

export default function StuffList({stuffs}) {
    return (
        <Canvas
            style={{
                width : '100%',
                height : '300px',
            }}
        >
            <ScrollControls
                vertical
                damping={10}
                pages={3}
            >
                <Scroll>
                    {stuffs.map((stuff, index) => (
                        <RenderStuff stuff={stuff} index={index} key={index}/>
                    ))}
                </Scroll>
            </ScrollControls>
            
            <OrthographicCamera 
                makeDefault
                position={[0,0, 4]}
                zoom={50}   
            />
        </Canvas>
    )
}