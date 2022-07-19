/*
Auto-generated by: https://github.com/pmndrs/gltfjsx
*/

import React, { useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'

export default function Model({ Hover, Click, ...props }) {
  const [name, setName] = useState("Sofa_brown_large");
  function onHover() { Hover(name); }
  function onClick() { Click(name); }

  const group = useRef()
  const { nodes, materials } = useGLTF('/stuffAssets/sofa_large_brown_1.glb')
  return (
    <group 
      onPointerOver={() => onHover()}
      onPointerDown={() => onClick()}
      castShadow
      ref={group} 
      {...props} 
      dispose={null}
    >
      <mesh geometry={nodes.low_poly_interior1887.geometry} material={materials.low_poly_interior} position={[0,0,0]} rotation={[0, 1.57, 0]} scale={100} />
    </group>
  )
}

useGLTF.preload('/stuffAssets/sofa_large_brown_1.glb')
