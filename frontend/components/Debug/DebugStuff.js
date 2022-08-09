import { Text, useGLTF } from "@react-three/drei";
import { useState } from "react";

export default function DebugStuff({data}) {
    const [geometry] = useState(data.geometry);
    const [material] = useState(data.material);
    const [glbpath] = useState(data.stuffGlbPath);
    const { nodes, materials } = useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glbpath);

    const fontProps = {
        fontSize : 0.15,
        letterSpacing : -0.1,
        'material-toneMapped' : false,
    }

    if(!nodes[geometry]) return
    
    return (
        <group>
            <mesh
                castShadow
                geometry={nodes[geometry].geometry}
                material={materials[material]}
                scale={35}
                rotation={[0.6, -0.775, 0]}
            />
            <Text
                {...fontProps} 
                children={data.stuffType}
                position={[0, -0.5, 2]}
            />
            <Text
                {...fontProps} 
                children={data.id}
                position={[0, -0.65, 2]}
            />
            <Text
                {...fontProps} 
                children={data.stuffName}
                position={[0, -0.9, 2]}
            />
            <pointLight position={[2, 5, 3]} intensity={0.05}/>
            <ambientLight intensity={0.02}/>
        </group>
    )
}