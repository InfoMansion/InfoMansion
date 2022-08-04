import { Circle, useGLTF } from "@react-three/drei";
import { useState } from "react";

export default function EditStuff({data, Click}) {
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.material);
    const [glbpath] = useState(data.stuffGlbPath);

    function onClick(e) {
        Click(e, data);
    }
    const { nodes, materials } = useGLTF(`https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/stuff-assets/${glbpath}.glb`)

    if(!nodes[geometry]) return

    return (
        <group 
            onPointerDown={(e) => onClick(e)}
            rotation={[0.6, -0.775, 0]}
        >
            <mesh
                castShadow
                geometry={nodes[geometry].geometry}
                material={materials[material]}
                scale={50}
            />
            <pointLight position={[0, 3, 1]} intensity={0.1}/>
        </group>
    )
}