import React, { useEffect, useRef, useState } from 'react'
import { Text, useGLTF } from '@react-three/drei'
import { animated, config, useSpring } from '@react-spring/three';
import { Color } from 'three';
import { useFrame } from '@react-three/fiber';

export default function Model({ tagon, status, Hover, Click, data, ...props }) {
  const [geometry] = useState(data.geometry);
  const [material] = useState(data.materials);
  const [glbpath] = useState(data.stuff_glb_path);
  const [clicked, setClicked] = useState(0);
  
  // component가 하나라도 잘못되었을 때 렌더링이 고장나는 것을 방지.
  if(!glbpath) return null;
  // glb 임포트
  const { nodes, materials } = useGLTF(`/stuffAssets/${glbpath}.glb`)
  
  // stuff 컨트롤
  function onHover(e) { Hover(e, data); }
  function onClick(e) { 
    setClicked(Number(!clicked));
    Click(e, data); 
  }

  const [hovered, setHovered] = useState(false);
  const {scale} = useSpring({
    scale : ( !clicked && hovered && data.category != "NONE"  ) ? 1.2 : 1,
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
    if(tagon && status == 'view' && data.category != 'NONE'){
      locref.current.quaternion.copy(camera.quaternion);
      textref.current.material.color.lerp(color.set(hovered ? '#ffa0a0' : 'black'), 0.1);
    }
  }, [tagon])


  return <group
      position={[data.pos_x, data.pos_y, data.pos_z]}
    >
    {/* 스터프 */}
    {
      (status == 'view')
      ?
        <animated.group
          onPointerOver={(e) => onHover(e)}
          onPointerDown={(e) => onClick(e)}
          
          onPointerEnter={() => setHovered(true)}
          onPointerLeave={() => setHovered(false)}
          
          rotation={[data.rot_x, data.rot_y, data.rot_z]}
          scale={scale}
          {...props} dispose={null}
        >
          {
            // category NONE인거 y축 이동 방지하기 위해 동적 렌더링함.
            (data.category != 'NONE') ? 
              <animated.mesh
                geometry={nodes[geometry].geometry} material={materials[material]} castShadow
                scale={100}
                position-y={positionY}
              />
              : 
              <mesh
                geometry={nodes[geometry].geometry} material={materials[material]} castShadow
                scale={100}
              />
          }
        </animated.group> 
      : 
      <group
        rotation={[data.rot_x, data.rot_y, data.rot_z]}
        scale={1}
      >
        <mesh
          geometry={nodes[geometry].geometry} material={materials[material]} castShadow
          scale={100}
        />
      </group>
    }
    
    {/* 태그 */}
    {
      (tagon && status == 'view' && data.category != 'NONE') ?
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
          children={data.stuff_name}
        
          position={[0, 0, 0.01]}
        />
      </group> 
      : <></>
    }
  </group>

}

useGLTF.preload(`/stuffAssets/IM.glb`)
