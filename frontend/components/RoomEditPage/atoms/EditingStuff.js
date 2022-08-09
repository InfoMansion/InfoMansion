import { useGLTF } from "@react-three/drei";
import { useState } from "react";

export default function EditStuff({data, position, rotation}) {
    console.log(data);
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.material);
    const [glbpath] = useState(data.stuffGlbPath);
    const { nodes, materials } = useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glbpath);

    if(!nodes[geometry]) return
    
    return (
        <group
            rotation={rotation}
            position={position}
        >
            <mesh
                castShadow
                geometry={nodes[geometry].geometry}
                material={materials[material]}
                scale={100}
            />
        </group>
    )
}