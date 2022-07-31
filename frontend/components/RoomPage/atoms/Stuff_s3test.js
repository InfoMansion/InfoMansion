// 링크 불러올 때
import React, { useEffect, useRef, useState } from 'react'
import { Text, useGLTF } from '@react-three/drei'
import { animated, config, useSpring } from '@react-spring/three';
import { Color } from 'three';
import { useFrame } from '@react-three/fiber';

export default function Model({ tagon, status, Hover, Click, ...props }) {
  const [geometry] = useState('low_poly_interior2198');
  const [material] = useState('low_poly_interior');
  const [clicked, setClicked] = useState(0);

  // glb 임포트
  const { nodes, materials } = useGLTF(`https://infomansion-webservice-s3.s3.ap-northeast-2.amazonaws.com/wall_2198.glb`)
  console.log(nodes);
  console.log(materials);
  
  // stuff 컨트롤
  function onHover(e) { Hover(e, 'click'); }
  function onClick(e) { 
    setClicked(Number(!clicked));
    Click(e, data); 
  }

  const [hovered, setHovered] = useState(false);
  const {scale} = useSpring({
    scale : ( !clicked && hovered  ) ? 1.2 : 1,
    config : config.wobbly
  })

  // 클릭 애니메이션 관리.
  const { spring } = useSpring({
    spring : clicked,
    config: {mass : 5, tension : 400, friction : 70, precision : 0.0001 },
  });
  const positionY = spring.to([0, 1], [0, 7]);

  // Tag 컨트롤
  const color = new Color();
  const fontProps = {
    // font : '왜안되는데',
    fontSize : 0.15,
    letterSpacing : -0.1,
    'material-toneMapped' : false,
  }

  const textref = useRef(); 
  const locref = useRef();

  // 마우스 커서 형태 변경.
  useEffect(() => {
    if (hovered) document.body.style.cursor = 'pointer'
    return () => (document.body.style.cursor = 'auto')
  }, [hovered])
  
  useFrame(({camera}) => {
    if(tagon && status == 'view'){
      locref.current.quaternion.copy(camera.quaternion);
      textref.current.material.color.lerp(color.set(hovered ? '#ffa0a0' : 'black'), 0.1);
    }
  }, [tagon])


  return <group
      position={[0, 0, 0]}
    >
    {/* 스터프 */}
        <animated.group
          onPointerOver={(e) => onHover(e)}
          onPointerDown={(e) => onClick(e)}
          
          onPointerEnter={() => setHovered(true)}
          onPointerLeave={() => setHovered(false)}
          
          rotation={[0, 0, 0]}
          scale={scale}
          {...props} dispose={null}
        >
            <animated.mesh
              geometry={nodes[geometry].geometry} material={materials[material]} castShadow
              scale={100}
              position-y={positionY}
            />
        </animated.group> 
    
    <group ref={locref} position={[1, 1.5, 1]}>
      <mesh>
        <circleGeometry attach="geometry" args={[0.3, 20]} />
        <meshBasicMaterial 
            attacj="material" 
            color={ hovered ? 'white' : '#ffa0a0'} />
      </mesh>
      <Text
        ref={textref}
        {...fontProps} 
        children={"wall"}
      
        position={[0, 0, 0.01]}
      />
    </group> 
  </group>

}

useGLTF.preload(`/stuffAssets/IM.glb`)
