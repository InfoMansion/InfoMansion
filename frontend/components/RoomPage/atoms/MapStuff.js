import React, { useEffect, useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'
import { animated, config, useSpring  } from '@react-spring/three';

export default function Model({ Hover, Click, data, ...props }) {
  if(!data) return;
  const [geo, setGeo] = useState(data.geometry);
  const [poly, setPoly] = useState(data.material);
  const [glb, setGlb] = useState(data.stuffGlbPath);

  function onHover(e) { Hover(e, data); }
  function onClick(e) { Click(e, data); }
  
  const group = useRef()

  // component가 하나라도 잘못되었을 때 렌더링이 고장나는 것을 방지.
  if(!glb) return null;

  const [asset, setAsset] = useState(useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glb));
  let { nodes, materials } = asset


  useEffect(() => {
    setGeo(data.geometry);
    setPoly(data.material);
    setGlb(data.stuffGlbPath);

  }, [data.stuffGlbPath])
  
  useEffect(() => {
    setAsset(useGLTF(process.env.NEXT_PUBLIC_S3_PATH + glb));
  }, [glb])

  useEffect(() => {
    nodes = asset.nodes;
    materials = asset.materials;
  })
  if(!nodes[geo]) return

  return (
    <group 
      castShadow
      ref={group} 
      {...props} 
      dispose={null}
    >
      <mesh 
        geometry={nodes[geo].geometry} 
        material={materials[poly]} 
        scale={100} 
      />
    </group>
  )
}

useGLTF.preload(`/stuffAssets/IM.glb`)
