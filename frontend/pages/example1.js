import { OrbitControls, OrthographicCamera } from '@react-three/drei'
import {Canvas, useThree, useFrame} from '@react-three/fiber'
import { useEffect, useState } from 'react'
import { Camera, Mesh } from 'three'
import styles from '../styles/Home.module.css'

// wall과 floor는 고정. 
import Wall from './StuffPages/Wall_test_1'
import Floor from './StuffPages/Floor_1'

import Curtain from './StuffPages/Curtain_white_large_1'
import Chair from './StuffPages/Chair_brown_1'
import Plant_orange_medium from './StuffPages/Plant_orange_medium_1'
import Shelf_white_medium from './StuffPages/Shelf_white_medium_1'
import Sofa_large_brown_1 from './StuffPages/Sofa_large_brown_1'
import Table_brown_small_1 from './StuffPages/Table_brown_small_1'
import Table_side_black_1 from './StuffPages/Table_side_black_1'
import Table_wood_1 from './StuffPages/Table_wood_1'

export default function Example() {
    // 화면 카메라 확대 수준 조절용 변수
    const [zoomscale, setzoomscale] = useState(70);

    let stuffhover = function(name) {
        console.log(name);
    }
    
    function Hover(name) {
        console.log(name + " 호버");
    }
    function Click(name) {
        console.log(name + " 클릭");
    }

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
        <div 
            className={styles.container}
            style={{ width : "500px", height : "500px" }}>
            <Canvas shadows onCreated={state => state.gl.setClearColor("white")} >
                {/* light */}
                <pointLight position={[10, 20, 4]} intensity={0.3}/>
                <directionalLight 
                    position={[20, 40, 20]} 
                    intensity={1}
                    castShadow
                    shadow-mapSize-width={1024}
                    shadow-mapSize-height={1024}
                    shadow-camera-far={50}
                    shadow-camera-left={-100}
                    shadow-camera-right={100}
                    shadow-camera-top={100}
                    shadow-camera-bottom={-100}
                />

                {/* 창 밖에서 들어오는 빛 테스트용 */}
                {/* <pointLight position={[-4, 2, 2]} intensity={0.5} /> */}
                
                <ambientLight intensity={0.2} />

                {/* camera */}
                <OrthographicCamera makeDefault zoom={zoomscale} />
                <RoomCamera />

                {/* object */}

                {/* 그림자 테스트용 */}
                <mesh castShadow position={[2,2,2]}>
                    <boxBufferGeometry attatch="geometry" />
                    <meshLambertMaterial attatch="material" color="orange" />
                </mesh>
                <mesh receiveShadow position={[2,-1.5,2]} scale={3}>
                    <boxBufferGeometry attatch="geometry" />
                    <meshLambertMaterial attatch="material" color="white" />
                </mesh>
                
                {/* 실제 구현될 방 요소 */}
                {/* 그림자를 받을 요소, 그림자를 뱉을 요소로 나눔. */}
                {/* 그림자 받을 요소 */}

                {/* 그림자 뱉을 요소 */}
                {/* 이거 클로저 함수로 컴포넌트 리턴받도록 변경할 것. */}

                <Table_wood_1 attatch="geometry" Hover={Hover} Click={Click} position={[12.06, -43, 16.13]}/>
                <mesh receiveShadow>
                    <Wall  position={[0,0,0]} />
                    <Floor position={[0,4,0]} />
                </mesh>

                <Curtain position={[-1.8,2,1.8]}/>
                <Chair castShadow position={[3.5,0,3.5]}/>
                <Plant_orange_medium position={[0.2,0,2.4]}/>
                <Shelf_white_medium position={[12, -43.01, 16]}/>
                <Sofa_large_brown_1 position={[1,0,0.3]} />
                <Table_brown_small_1 position={[11.9, -43, 16]}/>
                <Table_side_black_1 position={[12, -42.9, 15.9]}/>

                {/* 사용자 인터렉션 */}
                <OrbitControls />
            </Canvas>

        </div>
      )
}