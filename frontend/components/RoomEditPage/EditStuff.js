import { Circle, useGLTF } from "@react-three/drei";
import { useState } from "react";

export default function EditStuff({data, Click}) {
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.material);
    const [glbpath] = useState(data.stuffGlbPath);

    // 컴포넌트 오류 해결.
    if(!glbpath) return null;
    function onClick(e) {
        Click(e, data);
    }
    const { nodes, materials } = useGLTF(`/stuffAssets/${glbpath}.glb`)
    
    // geometry가 터지는 게 있어서 임시로 우회시킵니다.
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