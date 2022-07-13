import {Canvas, useThree, useFrame} from '@react-three/fiber'
import { useEffect } from 'react'
import { Camera } from 'three'
// import Exasset from './roompage/exasset';
import styles from '../styles/Home.module.css'
import Map_ini from './roompage/map_ini/Map_ini'

export default function Example() {
    
    function RoomCamera() {
        useFrame((state) => {
            // 카메라 위치 세팅
            const distance = 3;
            state.camera.position.x = distance;
            state.camera.position.y = distance;
            state.camera.position.z = distance;
            
            const degree = 45;
            const anglex = - degree * Math.PI / 180;
            const angley = degree * Math.PI / 180;

            state.camera.rotation.x = -0.7;
            state.camera.rotation.z = 0.6;
            state.camera.rotation.y = angley;
        })
        return null
      }

    return (
        <div>
            <div 
                className={styles.container}
                style={{ width : "500px", height : "500px" }}>
            <Canvas 
            >
                {/* light */}
                <pointLight position={[10, 10, 10]} intensity={3}/>
                <ambientLight />

                <RoomCamera />
                {/* object */}
                <Map_ini rotation={[0,0,0]} scale={1.0} />


            </Canvas>

            </div>
            <div 
                className={styles.container}
                style={{ width : "500px", height : "500px" }}>
            <Canvas flat linear>

                <pointLight position={[10, 10, 10]} />
                <ambientLight position={[10, 10, 10]} />
                <Map_ini rotation={[0,0,0]} scale={1.0} />
            </Canvas>

            </div>
        </div>
      )
}