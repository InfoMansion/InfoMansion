import React, { useEffect, useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'
import { animated, config, useSpring  } from '@react-spring/three';

export default function Model({ Hover, Click, data, ...props }) {
  if(!data) return;
  const [geometry, setGeometry] = useState(data.geometry);
  const [material, setMaterial] = useState(data.material);
  const [glbpath, setGlbpath] = useState(data.stuffGlbPath);

  function onHover(e) { Hover(e, data); }
  function onClick(e) { Click(e, data); }
  
  const group = useRef()

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
  }, [glbpath])

  useEffect(() => {
    nodes = asset.nodes;
    materials = asset.materials;
  }, [asset])
  if(!nodes[geometry[0]]) return

  return (
    <group 
      castShadow
      ref={group} 
      {...props} 
      dispose={null}
    >
      {geometry.map( (geo, i) => (
        <mesh
          geometry={nodes[geo].geometry}
          material={materials[material[i]]}
          scale={100}
        />
      ))}
    </group>
  )
}

useGLTF.preload(`/stuffAssets/IM.glb`)
