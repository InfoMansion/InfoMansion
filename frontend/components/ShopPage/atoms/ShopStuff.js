import { Circle, useGLTF } from '@react-three/drei';
import { useState } from 'react';

export default function ShopStuff({ data, Click, pos, dist, tmp }) {
  const [geometry] = useState(data.geometry);
  const [material] = useState(data.material);
  const [glbpath] = useState(data.stuffGlbPath);

  // 컴포넌트 오류 해결.
  if (!glbpath) return null;
  function onClick(e) {
    Click(e, data);
  }
  const { nodes, materials } = useGLTF(
    process.env.NEXT_PUBLIC_S3_PATH + glbpath,
  );

  if (!nodes[geometry[0]]) return;

  return (
    <group
      position={
        tmp ? [pos[0], pos[1] + dist, pos[2]] : [pos[0] + dist, pos[1], pos[2]]
      }
      onPointerDown={e => onClick(e)}
      rotation={[0.6, -0.775, 0]}
    >
      {geometry.map((geo, i) => (
          <mesh
            castShadow
            geometry={nodes[geo].geometry}
            material={materials[material[i]]}
            scale={100}
          />
      ))}
      <pointLight position={[2, 5, 2]} intensity={0.04} />
    </group>
  );
}
