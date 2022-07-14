/*
Auto-generated by: https://github.com/pmndrs/gltfjsx
*/

import React, { useRef } from 'react'
import { useGLTF } from '@react-three/drei'

export default function Model({ ...props }) {
  const group = useRef()
  const { nodes, materials } = useGLTF('/barrel.glb')
  return (
    <group ref={group} {...props} dispose={null}>
      <group scale={0.43}>
        <mesh geometry={nodes.Mesh_barrel.geometry} material={materials.cloth} />
        <mesh geometry={nodes.Mesh_barrel_1.geometry} material={materials.metal} />
      </group>
    </group>
  )
}

useGLTF.preload('/barrel.glb')
