import { useGLTF } from "@react-three/drei";
import { useEffect, useState } from "react";

export default function EditStuff({data}) {
    const [geometry, setGeometry] = useState(data.geometry);
    const [material, setMaterial] = useState(data.material);
    const [glbpath, setGlbpath] = useState(data.stuffGlbPath);
    if(!glbpath) return null;
  
    const [asset, setAsset] = useState(useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glbpath));
    let { nodes, materials } = asset
    
    useEffect(() => {
      setGeometry(data.geometry);
      setMaterial(data.material);
      setGlbpath(data.stuffGlbPath);
    }, [data.stuffGlbPath])
    
    useEffect(() => {
      setAsset(useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glbpath));
    })
  
    useEffect(() => {
      nodes = asset.nodes;
      materials = asset.materials;
    }, [asset])
    if(!nodes[geometry[0]]) return
    
    return (
        <group
            rotation={[0.6, -0.775, 0]}
        >
            {geometry.map( (geo, i) => (
              <mesh castShadow
                geometry={nodes[geo].geometry}
                material={materials[material[i]]}
                scale={50}
              />
            ))}
            <pointLight position={[2, 5, 3]} intensity={0.3}/>
        </group>
    )
}