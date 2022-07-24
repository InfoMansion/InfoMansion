import React, { useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'

export default function Model({ Hover, Click, data, ...props }) {
  const [geo] = useState(data.geometry);
  const [poly] = useState(data.materials);
  const [glb] = useState(data.stuff_glb_path);

  function onHover(e) { Hover(e, data); }
  function onClick(e) { Click(e, data); }
  
  const group = useRef()

  // console.log(glb);
  // component가 하나라도 잘못되었을 때 렌더링이 고장나는 것을 방지.
  if(!glb) return null;

  const { nodes, materials } = useGLTF(`/stuffAssets/${glb}.glb`)
  return (
    <group 
      onPointerOver={(e) => onHover(e)}
      onPointerDown={(e) => onClick(e)}
      castShadow
      ref={group} 
      {...props} 
      dispose={null}
    >
      <mesh 
        geometry={nodes[geo].geometry} 
        material={materials[poly]} 
        // position={[0, 0, 0]} 
        // rotation={[0, 0, 0]} 
        scale={100} 
      />
    </group>
  )
}

// 더미 오브젝트(용량 작게) 만들어서 처리해야 함.
useGLTF.preload(`/stuffAssets/IM.glb`)
