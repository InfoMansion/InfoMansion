import React, { useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'

export default function Model({ Hover, Click, ...props }) {
  function onHover(e) { Hover(e); }
  function onClick(e) { Click(e); }

  const [geo] = useState(props.geo);
  const [poly] = useState(props.poly);

  const group = useRef()
  const { nodes, materials } = useGLTF('/stuffAssets/chair_brown_1.glb')
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

useGLTF.preload('/stuffAssets/chair_brown_1.glb')
