import { useGLTF } from "@react-three/drei";
import { useFrame } from "@react-three/fiber";
import { useEffect, useRef, useState } from "react";
import { animated, config, useSpring } from "@react-spring/three";

export default function ConfigAsset({data, pos, Click, iniscale, color, inicolor, speed, isFollow = false}) {
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.material);
    const [glbpath] = useState(data.stuffGlbPath);

    if(!glbpath) return null;

    // glb 임포트
    const { nodes, materials } = useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glbpath)
    if(!nodes[geometry[0]]) return
    const [hovered, setHovered] = useState(false);
    
    useEffect(() => {
        if (hovered) document.body.style.cursor = 'pointer'
        return () => (document.body.style.cursor = 'auto')
    }, [hovered])

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
            onClick={e => Click(e)}
            position={pos}
            rotation={[0, -1.5, 0]}
            scale={scale}
        >
            {geometry.map((geo, i) => ( 
                <mesh
                    geometry={nodes[geo].geometry} material={materials[material[i]]} castShadow
                    scale={iniscale}
                >
                    {data.stuffName == "setting" || data.stuffName == "heart" || data.stuffName == "postbox" ? 
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