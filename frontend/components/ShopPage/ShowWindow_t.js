import { Scroll, ScrollControls } from '@react-three/drei';
import { Canvas, useFrame, useThree } from '@react-three/fiber';
import { Suspense, useEffect, useState } from 'react';
import ShopStuff from './atoms/ShopStuff';

export default function ShowWindow({ stuffs, type, click }) {
  let count = 12;
  const [zoomscale] = useState(50);
  // stuff 간 거리.
  const [dist, setDist] = useState(-4);

  const [pos, setPos] = useState([0, 0, 0]);
  useEffect(() => {
    if (type == 'floor') setPos([0, 1, 0]);
    else if (type == 'wall') setPos([0, -0.5, 0]);
  });

  function Click(e, stuff) {}

  return (
    <ScrollControls vertical damping={4} pages={dist - 2 - 4 * dist}>
      <Scroll>
        {/* 임시 빛 */}
        <group>
          <ambientLight />
          {stuffs.map(stuff => (
            <group key={stuff.id}>
              <ShopStuff
                Click={Click}
                data={stuff}
                pos={pos}
                dist={(count += dist)}
                tmp={true}
              />
            </group>
          ))}
        </group>
      </Scroll>
    </ScrollControls>
  );
}
