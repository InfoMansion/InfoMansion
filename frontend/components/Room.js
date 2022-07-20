import { OrbitControls, OrthographicCamera } from '@react-three/drei'
import {Canvas, useThree, useFrame} from '@react-three/fiber'
import { useEffect, useRef, useState } from 'react'
import { Camera, Mesh, StaticReadUsage } from 'three'
import styles from '../styles/Home.module.css'

// wall과 floor는 고정. 
import Wall from './StuffComponents/Wall_test_1'
import Floor from './StuffComponents/Floor_1'

// 방 구현 에셋들.
import Curtain from './StuffComponents/Curtain_white_large_1'
import Chair from './StuffComponents/Chair_brown_1'
import Plant_orange_medium from './StuffComponents/Plant_orange_medium_1'
import Shelf_white_medium from './StuffComponents/Shelf_white_medium_1'
import Sofa_large_brown_1 from './StuffComponents/Sofa_large_brown_1'
import Table_brown_small_1 from './StuffComponents/Table_brown_small_1'
import Table_side_black_1 from './StuffComponents/Table_side_black_1'
import Table_wood_1 from './StuffComponents/Table_wood_1'

import userStuff from './userStuff.json'

export default function Room( { StuffClick, ...props} ) {
    // 변수 선언부
    // 화면 카메라 확대 수준 조절용 변수
    const [zoomscale, setzoomscale] = useState(90);
    const [userID] = useState(props.userID);
    // 제대로 불러와지는거 확인됨.
    const [stuffs] = useState(userStuff[userID]);

    function Hover(e, name) {
        // console.log(e.nativeEvent.offsetX + " " + e.nativeEvent.offsetY);
        // console.log(name + " 호버");
    }
    function Click(e, name) {
        console.log(e.nativeEvent.offsetX + " " + e.nativeEvent.offsetY);
        console.log(name + " 클릭");

        // RoomPage의 stuffClick 함수 실행시키기.
        StuffClick(name);
    }

    function makeStuff(stuff) {
        // 스터프 제작하는 코드 작성될 예정.
        return  null;
    }

    function RoomCamera() {
        useFrame((state) => {
            // 카메라 위치 세팅

            // 마우스 커서 위치에 따라 시점 살짝식 바꾸게 가능/?
            const distance = 40;
            state.camera.position.x = distance;
            state.camera.position.y = distance;
            state.camera.position.z = distance;
            
            // console.log(e);
            state.camera.lookAt(0, 0, 0);
        })
        return null
    }

    return (
        <div 
            
            style={{ 
                width : "650px", 
                height : "800px",
                // margin : '30px auto'
                }}>
            
            <Canvas 
                onPointerMove={() => console.log("호버호버")}
                
                shadows 
                onCreated={state => state.gl.setClearColor("#ffffff")} >
                
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
                <RoomCamera />
                <OrthographicCamera makeDefault zoom={zoomscale} />
                
                {/* 실제 구현될 방 요소 */}
                {/* 그림자를 받을 요소, 그림자를 뱉을 요소로 나눔. */}
                {/* 그림자 받을 요소 */}
                <mesh receiveShadow>
                    <Wall  position={[0,0,0]} />
                    <Floor position={[0,4,0]} />
                </mesh>

                {/* 그림자 뱉을 요소 */}
                {/* 이거 클로저 함수로 컴포넌트 리턴받도록 변경할 것. */}
                {/* { stuffs.map( stuff => makeStuff(stuff) )} */}

                <Table_wood_1 Hover={Hover} Click={Click} position={[12.06, -43, 16.13]}/>
                <Chair Hover={Hover} Click={Click} position={[3.5,0,3.5]}/>

                <Sofa_large_brown_1 Hover={Hover} Click={Click} position={[1,0,0.3]} />
                <Shelf_white_medium Hover={Hover} Click={Click} position={[12, -43.01, 16]}/>

                <Curtain position={[-1.8,2,1.8]}/>
                <Plant_orange_medium position={[0.2,0,2.4]}/>
                <Table_brown_small_1 position={[11.9, -43, 16]}/>
                <Table_side_black_1 position={[12, -42.9, 15.9]}/>

                {/* 사용자 인터렉션 */}
                {/* <OrbitControls /> */}
            </Canvas>

        </div>
      )
}