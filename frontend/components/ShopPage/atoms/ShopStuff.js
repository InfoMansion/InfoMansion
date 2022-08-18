import { animated, config, useSpring } from '@react-spring/three';
import { useGLTF } from '@react-three/drei';
import { useState } from 'react';

export default function ShopStuff({ data, Click, stuffscale }) {
  const [geometry] = useState(data.geometry);
  const [material] = useState(data.material);
  const [glbpath] = useState(data.stuffGlbPath);
  const [hovered, setHovered] = useState(false);

  // 컴포넌트 오류 해결.
  if (!glbpath) return null;
  function onClick(e) { Click(e, data); }
  const { nodes, materials } = useGLTF( process.env.NEXT_PUBLIC_S3_PATH + glbpath );
  if (!nodes[geometry[0]]) return;


  const { scale } = useSpring({
    scale : hovered ? 1.2 : 1,
    config : config.wobbly
  })

  return (
    <animated.group
      onPointerDown={e => onClick(e)}

      onPointerEnter={() => setHovered(true)}
      onPointerLeave={() => setHovered(false)}
      scale={scale}
    >
      {geometry.map((geo, i) => (
        <mesh
          rotation={[0.6, -0.775, 0]}
          castShadow
          geometry={nodes[geo].geometry}
          material={materials[material[i]]}
          scale={100 * stuffscale}
        />
      ))}
      <pointLight position={[2, 5, 2]} intensity={0.04} />

      {/* 가격, 이름 등 태그 띄워주기 */}
    </animated.group>
  );
}
