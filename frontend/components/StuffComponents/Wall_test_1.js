/*
Auto-generated by: https://github.com/pmndrs/gltfjsx
*/

import React, { useRef } from 'react'
import { useGLTF } from '@react-three/drei'

export default function Model({ ...props }) {
  const group = useRef()
  const { nodes, materials } = useGLTF('/stuffAssets/wall_test_1.glb')
  return (
    <group receiveShadow ref={group} {...props} dispose={null}>
      <mesh geometry={nodes.low_poly_interior1917.geometry} material={materials.low_poly_interior} position={[0,0,0]} scale={100} />
    </group>
  )
}

useGLTF.preload('/stuffAssets/wall_test_1.glb')
