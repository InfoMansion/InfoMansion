import { useGLTF } from "@react-three/drei";
import { useState } from "react";

export default function EditStuff({data, Click}) {
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.material);
    const [glbpath] = useState(data.stuffGlbPath);
    const { nodes, materials } = useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glbpath);

    if(!nodes[geometry]) return
    
    return (
        <group
            rotation={[0.6, -0.775, 0]}
        >
            <mesh
                castShadow
                geometry={nodes[geometry].geometry}
                material={materials[material]}
                scale={50}
            />
            <pointLight position={[2, 5, 3]} intensity={0.3}/>
        </group>
    )
}