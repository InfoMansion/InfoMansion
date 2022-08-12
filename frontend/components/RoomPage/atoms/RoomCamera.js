import { OrthographicCamera } from "@react-three/drei";
import { useFrame } from "@react-three/fiber";
import { useState } from "react";
import { config, useSpring } from "react-spring";
import { Vector3 } from "three";

export default function RoomCamera({camloc, clicked, zoomscale}) {
    const v = new Vector3;
    const [clicky, setClicky] = useState(0);

    useFrame(({mouse, camera}) => {
        const distance = 40;
        const con = 0.5;
        const xoff = mouse.x*con;
        const yoff = mouse.y*con;
        
        // if(clicked && clicky < 5) setClicky(clicky + 0.1);
        // if(!clicked && clicky > 0) setClicky(clicky - 0.1);

        camera.position.lerp(
            v.set(
                distance - xoff + camloc[0],
                // distance - yoff + camloc[1] + ( clicked ? 5 : 0),
                distance - yoff + camloc[1],
                distance + camloc[2]
            ), 0.05);
        camera.updateProjectionMatrix();

        camera.lookAt(0, clicky, 0);
    })
    return <OrthographicCamera makeDefault zoom={zoomscale} />
}