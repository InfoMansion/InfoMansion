// 링크 불러올 때
import React, { useEffect, useRef, useState } from 'react'
import { Text, useGLTF } from '@react-three/drei'
import { animated, config, useSpring } from '@react-spring/three';
import { Color } from 'three';
import { useFrame } from '@react-three/fiber';

export default function Model({ ...props }) {
  const [geometry] = useState('low_poly_interior2049');
  const [material] = useState('low_poly_interior');
  const [clicked, setClicked] = useState(0);

  // glb 임포트
  const { nodes, materials } = useGLTF(`https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/stuff-assets/deco_ash_2049.glb`)

  return <group
    >
    {/* 스터프 */}
      <mesh
        geometry={nodes[geometry].geometry} material={materials[material]} castShadow
        scale={1000}
        position={[3, 3, 3]}
      />
  </group>

}

useGLTF.preload(`/stuffAssets/IM.glb`)
