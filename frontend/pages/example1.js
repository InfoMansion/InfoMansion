import { OrthographicCamera } from '@react-three/drei'
import {Canvas, useThree, useFrame} from '@react-three/fiber'
import { useEffect, useState } from 'react'
import { Camera } from 'three'
import styles from '../styles/Home.module.css'
import Map_ini from './roompage/Map_ini'

// wall과 floor는 고정. 
import Wall from './roompage/Wall_test_1'
import Floor from './roompage/Floor_1'

import Curtain from './roompage/Curtain_white_large_1'
import Chair from './roompage/Chair_brown_1'
import Plant_orange_medium from './roompage/Plant_orange_medium_1'
import Shelf_white_medium from './roompage/Shelf_white_medium_1'
import Sofa_large_brown_1 from './roompage/Sofa_large_brown_1'
import Table_brown_small_1 from './roompage/Table_brown_small_1'
import Table_side_black_1 from './roompage/Table_side_black_1'
import Table_wood_1 from './roompage/Table_wood_1'

export default function Example() {
    // 화면 카메라 확대 수준 조절용 변수
    const [zoomscale, setzoomscale] = useState(70);

    function RoomCamera() {
        useFrame((state) => {
            // 카메라 위치 세팅
            const distance = 40;
            state.camera.position.x = distance;
            state.camera.position.y = distance;
            state.camera.position.z = distance;
            
            state.camera.lookAt(0, 0, 0);
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
                <pointLight position={[10, 20, 4]} intensity={0.6}/>
                <pointLight position={[100, 100, 100]} intensity={0.2}/>

                {/* 창 밖에서 들어오는 빛 테스트용 */}
                {/* <pointLight position={[-4, 2, 2]} intensity={0.5} /> */}
                
                <ambientLight intensity={0.2} />

                {/* camera */}
                <OrthographicCamera makeDefault zoom={zoomscale} />
                <RoomCamera />

                {/* object */}
                {/* <Map_ini position={[0, 0, 0]} rotation={[0,0,0]} scale={1.0} /> */}

                {/* 이거 클로저 함수로 컴포넌트 리턴받도록 변경할 것. */}
                <Wall  position={[0,0,0]} />
                <Floor position={[0,4,0]} />
                <Curtain position={[-1.8,2,1.8]}/>
                <Chair position={[3.5,0,3.5]}/>
                <Plant_orange_medium position={[0.2,0,2.4]}/>
                <Shelf_white_medium position={[12, -43.01, 16]}/>
                <Sofa_large_brown_1 position={[1,0,0.3]} />
                <Table_brown_small_1 position={[11.9, -43, 16]}/>
                <Table_side_black_1 position={[12, -42.9, 15.9]}/>
                <Table_wood_1 position={[12, -42.9, 16.2]}/>
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