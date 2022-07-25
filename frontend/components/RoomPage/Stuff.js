import React, { useEffect, useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'
import { animated, config, useSpring, useTransition  } from '@react-spring/three';

export default function Model({ Hover, Click, data, ...props }) {
  const [geo] = useState(data.geometry);
  const [poly] = useState(data.materials);
  const [glb] = useState(data.stuff_glb_path);

  function onHover(e) { Hover(e, data); }
  function onClick(e) { 
    setClicked(Number(!clicked));
    Click(e, data); 
  }

  // component가 하나라도 잘못되었을 때 렌더링이 고장나는 것을 방지.
  if(!glb) return null;

  const [active, setActive] = useState(false);
  const {scale} = useSpring({
    scale : ( !clicked && active && data.category != "deco"  ) ? 1.2 : 1,
    config : config.wobbly
  })

  const [clicked, setClicked] = useState(0);
  const { spring } = useSpring({
    spring : clicked,
    config: {mass : 5, tension : 400, friction : 70, precision : 0.0001 },
  });

  const positionY = spring.to([0, 1], [0, 5]);

  // const transition = useTransition([], {
  //   // from: { scale: [0, 0, 0], rotation: [0, 0, 0] },
  //   // leave: { scale: [0.1, 0.1, 0.1], rotation: [0, 0, 0] },
  //   from : { position : [0, 3, 0] },
  //   leave : { position : [0, 0, 0] },
  //   config: { mass: 5, tension: 1000, friction: 100 },
  //   trail: 100
  // })


  const { nodes, materials } = useGLTF(`/stuffAssets/${glb}.glb`)
  // return transition((props) => {
  //   <animated.group
  //     onPointerOver={(e) => onHover(e)}
  //     onPointerDown={(e) => onClick(e)}

  //     onPointerEnter={() => setActive(true)}
  //     onPointerLeave={() => setActive(false)}

  //     castShadow
  //     {...props}
  //     dispose={null}
  //     scale={scale}
  //   >
  //     <mesh
  //       geometry={nodes[geo].geometry} 
  //       material={materials[poly]} 
  //       scale={100}
  //     />
  //   </animated.group>
  // }) 
  return <animated.group
      onPointerOver={(e) => onHover(e)}
      onPointerDown={(e) => onClick(e)}

      onPointerEnter={() => setActive(true)}
      onPointerLeave={() => setActive(false)}

      castShadow
      {...props}
      dispose={null}
      scale={scale}
    >
      {
        // category deco인거 y축 이동 방지하기 위해 동적 렌더링함.
        (data.category != 'deco') ? 
          <animated.mesh
            geometry={nodes[geo].geometry} material={materials[poly]} 
            scale={100}
            position-y={positionY}
          />
          : 
          <animated.mesh
            geometry={nodes[geo].geometry} material={materials[poly]} 
            scale={100}
          />
      }
    </animated.group>
}

useGLTF.preload(`/stuffAssets/IM.glb`)
