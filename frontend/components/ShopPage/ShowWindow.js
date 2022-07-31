import { Circle, OrbitControls, OrthographicCamera } from '@react-three/drei';
import { Canvas } from '@react-three/fiber'
import { useState } from 'react'
import ShopStuff from './atoms/ShopStuff'

export default function ShowWindow({furnitures}) {
    let count = -10;
    
    function Click(e, stuff) {
        console.log("클릭")
    }

    return (
        <Canvas
            style={{
                height : '200px',
                backgroundColor : '#aaaaaa'
            }}
        >
            {/* 임시 빛 */}
            <ambientLight />

            {furnitures.map( furniture => 
                <group key={furniture.id}>
                    <ShopStuff
                        Click={Click}
                        data={furniture} 
                        pos={count += 3}
                    />

                </group>
            )}
            
            {/* 임시 컨트롤
                차후에 좌우컨트롤만 되게 할 것.
            */}
            {/* <OrbitControls /> */}
            <OrthographicCamera />
        </Canvas>
    )
}