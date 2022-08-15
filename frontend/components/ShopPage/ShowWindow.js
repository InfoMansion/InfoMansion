import { Scroll, ScrollControls } from '@react-three/drei';
import { Canvas, useFrame, useThree } from '@react-three/fiber'
import { Suspense, useEffect, useState } from 'react'
import ShopStuff from './atoms/ShopStuff'

export default function ShowWindow({stuffs, type, click, scale}) {
    const offx = 5;
    const offy = 5;
    
    console.log(scale);

    const offset = 2.5;
    
    useEffect(() => {
        if(type=="floor") setPos([0,1,0]);
        else if(type=="wall") setPos([0, -0.5, 0]);
    })

    function Click(e, stuff) {
        click(e, stuff);
    }
    return (
        <group>
            <ambientLight />
            {stuffs.map( (stuff, i) => 
                <group key={stuff.id}
                    position={[(i%3 -1) * offx, parseInt(i/3) * -1 * offy + offset, 0]}
                >
                    <ShopStuff
                        Click={Click}
                        data={stuff}
                        index={i}
                        stuffscale={scale}
                    />
                </group>
            )}
        </group>
    )
}