import { Circle, useGLTF } from "@react-three/drei";
import { useState } from "react";

export default function ShopStuff({data, Click, pos}) {
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.materials);
    const [glbpath] = useState(data.stuffGlbPath);

    // 컴포넌트 오류 해결.
    if(!glbpath) return null;

    function onClick(e) {
        Click(e, data);
    }
    const { nodes, materials } = useGLTF(`/stuffAssets/${glbpath}`)
    
    return (
        <group 
            position={[pos, 0, 0]}
            onPointerDown={(e) => onClick(e)}
            rotation={[0.5, -1, 0]}
        >
            <mesh
                castShadow
                geometry={nodes[geometry].geometry}
                material={materials[material]}
                scale={50}
            />
        </group>
    )
}