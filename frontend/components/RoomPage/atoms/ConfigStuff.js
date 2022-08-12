import { useGLTF } from "@react-three/drei";
import { useFrame } from "@react-three/fiber";
import { useEffect, useRef, useState } from "react";
import { animated, config, useSpring } from "@react-spring/three";

export default function ConfigAsset({data, pos, Click, iniscale, color, inicolor, speed, isFollow = false}) {
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.material);
    const [glbpath] = useState(data.stuffGlbPath);

    // component가 하나라도 잘못되었을 때 렌더링이 고장나는 것을 방지.
    if(!glbpath) return null;

    // glb 임포트
    const { nodes, materials } = useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glbpath)
    if(!nodes[geometry[0]]) return
    const [hovered, setHovered] = useState(false);

    useEffect(() => {
        if (hovered) document.body.style.cursor = 'pointer'
        return () => (document.body.style.cursor = 'auto')
    }, [hovered])

    function onClick() { Click(); }

    const {scale} = useSpring({
        scale : hovered ? 1.2 : 1,
        config : config.wobbly
    })
    
    const group = useRef();
    useFrame(() => {
        group.current.rotation.y += speed;
    })

    return (
        <animated.group 
            ref={group} 
            onPointerEnter={() => setHovered(true)}
            onPointerLeave={() => setHovered(false)}
            onClick={onClick}
            position={pos}
            scale={scale}
        >
            {geometry.map((geo, i) => ( 
                <mesh
                    geometry={nodes[geo].geometry} material={materials[material[i]]} castShadow
                    scale={iniscale}
                >
                    {data.stuffName != "shop" ? 
                        <meshStandardMaterial
                            attach="material"
                            color={ isFollow ? '#FF3B78' : hovered ? color : inicolor}
                            roughness={0.1}
                            metalness={0.1}
                        />
                        : <></>
                    }
                </mesh>
            ))}
        </animated.group>
    )
}