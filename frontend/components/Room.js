import { Circle, Image, OrbitControls, OrthographicCamera, Text } from '@react-three/drei'
import {Canvas, useThree, useFrame} from '@react-three/fiber'
import { useEffect, useRef, useState, setState, useLayoutEffect } from 'react'
import { useRouter } from 'next/router'
import { useSpring, animated, config } from '@react-spring/three'

import Stuff from './RoomPage/Stuff'
import MapStuff from './RoomPage/MapStuff'

import userStuff from './userStuff.json'
import walltest from './walltest.json'
import { Color } from 'three'
import StuffTag from './RoomPage/StuffTag'

export default function Room( { StuffClick, ...props} ) {
    // 화면 확대 정도 조정.
    const [zoomscale] = useState(90);

    const [userID, setUserID] = useState(0);
    const [mapstuffs, setMapstuffs] = useState([]);
    const [stuffs, setStuffs] = useState([]);
    const [hovered, setHovered] = useState('');

    const router = useRouter();
    useEffect(() => {
        if(!router.isReady) return;

        setUserID(router.query.userID);     
        // stuff get
        setMapstuffs(userStuff[router.query.userID].slice(0, 2));
        setStuffs(userStuff[router.query.userID].slice(2));

    }, [router.isReady]);

    // 마우스가 움직일 때 위치 받기.
    const [mouseloc, setmouseloc] = useState([325, 375]);

    function Hover(e, stuff) {
        console.log(stuff.stuff_name_kor + " 호버");
        setHovered()
    }
    
    function Click(e, stuff) {
        console.log(e.nativeEvent.offsetX + " " + e.nativeEvent.offsetY);
        console.log(stuff.stuff_name + " 클릭");

        // 데코는 이벤트 진행 안함.
        if(stuff.category == 'deco') return null;
        // RoomPage의 stuffClick 함수 실행시키기.
        StuffClick(stuff);
    }

    // 카메라 위치 세팅
    function RoomCamera() {
        useFrame(({mouse, camera}) => {
            const distance = 40;
            const con = 3;
            const xoff = mouse.x*con;
            const yoff = mouse.y*con;

            camera.position.x = distance - xoff;
            camera.position.y = distance - yoff;
            camera.position.z = distance;
            
            camera.lookAt(xoff/20, yoff/100, 0);
        }, [mouseloc])
        return null
    }

    return (
        <div 
            style={{ 
                width : "650px", 
                height : "800px",
                // margin : '30px auto'
                }}
            >
            
            <Canvas               
                shadowmap
                onCreated={state => state.gl.setClearColor("#ffffff")} >
                
                {/* light */}
                <pointLight position={[10, 20, 4]} intensity={0.3}/>
                <directionalLight 
                    position={[20, 40, 20]} 
                    intensity={1}
                    castShadow
                    shadow-mapSize-width={10}
                    shadow-mapSize-height={10}
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
                    { mapstuffs.map( stuff => 
                        <MapStuff
                            Hover={Hover}
                            Click={Click}
                            
                            data={stuff}
                            key={stuff.name}
                        />
                        )}
                </mesh>
                
                {/* 그림자 뱉을 요소 */}
                {/* 이거 클로저 함수로 컴포넌트 리턴받도록 변경할 것. */}
                <mesh castShadow>
                    { stuffs.map( stuff => 
                        <group>
                            <Stuff
                                Hover={Hover}
                                Click={Click}

                                data={stuff} 

                                key={stuff.name}

                                position={[stuff.pos_x, stuff.pos_y, stuff.pos_z]}
                                rotation={[stuff.rot_x, stuff.rot_y, stuff.rot_z]}
                            />
                            <group 
                                position={[stuff.pos_x + 1, stuff.pos_y + 1.5, stuff.pos_z + 1]}
                            >
                                
                                <StuffTag children={stuff.stuff_name} />
                            </group>

                        </group>
                    )}
                </mesh>

                <OrbitControls />
            </Canvas>

        </div>
      )
}