import { Circle, useGLTF } from "@react-three/drei";
import { useState } from "react";

export default function ShopStuff({data, Click, pos, dist}) {
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
            position={[pos[0] + dist, pos[1], pos[2]]}
            onPointerDown={(e) => onClick(e)}
            rotation={[0.6, -0.775, 0]}
        >
            <mesh
                castShadow
                geometry={nodes[geometry].geometry}
                material={materials[material]}
                scale={50}
            />
            <pointLight position={[2, 2, 2]} intensity={0.04}/>
        </group>
    )
}