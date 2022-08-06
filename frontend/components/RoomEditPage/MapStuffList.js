import { OrthographicCamera, Scroll, ScrollControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";

import EditStuff from "./atoms/EditStuff";

const multicon = 3;
const xoff = 1;
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

    return (
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
            {/* <ScrollControls
                horizontal
                damping={10}
                pages={3}
            >
                <Scroll> */}
                    {stuffs.map((stuff, index) => 
                        <RenderStuff 
                            stuff={stuff} 
                            index={index} 
                            posy={posy}
                            key={index}
                            click={ () => click(stuff, tag)}
                        />
                    )}
                {/* </Scroll>
            </ScrollControls> */}

            <OrthographicCamera 
                makeDefault
                position={[0,0,4]}
                zoom={50}
            />
        </Canvas>
    )
}