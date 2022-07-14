import { OrthographicCamera } from '@react-three/drei'
import {Canvas, useThree, useFrame} from '@react-three/fiber'
import { useEffect } from 'react'
import { Camera } from 'three'
// import Exasset from './roompage/exasset';
import styles from '../styles/Home.module.css'
import Map_ini from './roompage/Map_ini'
import Desk_a from './roompage/Desk'
import Stand_small from './roompage/Stand_small'
import Sprokit from './roompage/Sprokit'
import Barrel from './roompage/Barrel'
import Sofa_small from './roompage/Sofa_small'

export default function Example() {
    
    function RoomCamera() {
        useFrame((state) => {
            // 카메라 위치 세팅
            const distance = 3;
            state.camera.position.x = distance;
            state.camera.position.y = distance;
            state.camera.position.z = distance;
            
            
            state.camera.lookAt(0, 0, 0);
            
            // const degree = 45;
            // const anglex = - degree * Math.PI / 180;
            // const angley = degree * Math.PI / 180;
            // state.camera.rotation.x = -0.7;
            // state.camera.rotation.z = 0.6;
            // state.camera.rotation.y = angley;
        })
        return null
      }

    return (
        <div>
            <div 
                className={styles.container}
                style={{ width : "500px", height : "500px" }}>
            <Canvas onCreated={state => state.gl.setClearColor("white")} >
                {/* light */}
                <pointLight position={[10, 10, -10]} intensity={0.6}/>
                <pointLight position={[10, -10, -10]} intensity={0.8}/>
                <pointLight position={[10, 10, 10]} intensity={0.4}/>
                <ambientLight />

                {/* camera */}
                <OrthographicCamera makeDefault zoom={130} />
                <RoomCamera />

                {/* object */}
                <Map_ini position={[0, 0, 0]} rotation={[0,0,0]} scale={1.0} />
                {/* <Sprokit position={[0,0,0]} scale={10}/> */}
                <Stand_small scale={10}/>
                <Desk_a position={[1.4,0,1.75]} rotation={[0,1.57,0]} scale={0.8}/>
                {/* <Barrel /> */}
                <Sofa_small position={[0.45,0,0.4]} rotation={[0,-1.57,0]} scale={0.7}/>

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