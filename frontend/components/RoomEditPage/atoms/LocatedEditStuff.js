import React, { useEffect, useState } from 'react'
import { useGLTF } from '@react-three/drei'

export default function Model({ Click, data }) {
  const [geometry, setGeometry] = useState(data.geometry);
  const [material, setMaterial] = useState(data.material);
  const [glbpath, setGlbpath] = useState(data.stuffGlbPath);

  // component가 하나라도 잘못되었을 때 렌더링이 고장나는 것을 방지.
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
  if(!nodes[geometry]) return
  function onClick(e) {  Click(e, data); }

  return <group
        position={[data.posX, data.posY, data.posZ]}
        rotation={[data.rotX, data.rotY, data.rotZ]}
        scale={1}
    >
        <mesh
          geometry={nodes[geometry].geometry} 
          material={materials[material]} 
          castShadow
          onClick={(e) => onClick(e)}
          scale={100}
        />
    </group>

}

useGLTF.preload(`/stuffAssets/IM.glb`)