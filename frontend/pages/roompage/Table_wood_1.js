/*
Auto-generated by: https://github.com/pmndrs/gltfjsx
*/

import React, { useRef } from 'react'
import { useGLTF } from '@react-three/drei'

export default function Model({ ...props }) {
  const group = useRef()
  const { nodes, materials } = useGLTF('/table_wood_1.glb')
  return (
    <group ref={group} {...props} dispose={null}>
      <mesh geometry={nodes.low_poly_interior1901.geometry} material={materials.low_poly_interior} position={[-9.06, 43, -13.13]} rotation={[0, 0.95, 0]} scale={100} />
      <mesh geometry={nodes.low_poly_interior1903.geometry} material={materials.low_poly_interior} position={[-8.72, 43.83, -13.73]} rotation={[0, -1.27, 0]} scale={100} />
      <mesh geometry={nodes.low_poly_interior1904.geometry} material={materials.low_poly_interior} position={[-9.12, 43.78, -13.08]} rotation={[0, -0.73, 0]} scale={100} />
      <mesh geometry={nodes.low_poly_interior1931.geometry} material={materials.low_poly_interior} position={[-8.91, 43.77, -13.56]} rotation={[0, -0.55, -Math.PI / 2]} scale={100} />
    </group>
  )
}

useGLTF.preload('/table_wood_1.glb')
