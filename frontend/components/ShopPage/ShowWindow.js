import { Circle, OrbitControls, OrthographicCamera, Scroll, ScrollControls } from '@react-three/drei';
import { Canvas, useFrame, useThree } from '@react-three/fiber'
import { Suspense, useEffect, useState } from 'react'
import ShopStuff from './atoms/ShopStuff'

export default function ShowWindow({furnitures, type}) {
    let count = -12;
    const [zoomscale] = useState(50);
    // stuff 간 거리.
    const [dist, setDist] = useState(4);

    const [pos, setPos] = useState([0, 0, 0]);
    useEffect(() => {
        if(type=="floor") setPos([0,1,0]);
        else if(type=="wall") setPos([0, -0.5, 0]);
    })

    function Click(e, stuff) {
        console.log("클릭")
    }

    return (
        <ScrollControls
            horizontal
            damping={4}
            pages={(2 - dist + 4 * dist)}
        >
            <Scroll>
            {/* 임시 빛 */}
                <ambientLight />
                <Suspense>
                    {furnitures.map( furniture => 
                        <group key={furniture.id}>
                            <ShopStuff
                                Click={Click}
                                data={furniture} 
                                pos={pos}
                                dist={count += dist}
                            />
                        </group>
                    )}
                </Suspense>
                
                {/* <OrbitControls /> */}
            </Scroll>
        </ScrollControls>
    )
}