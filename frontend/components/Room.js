import { Circle, Image, OrbitControls, OrthographicCamera, Text } from '@react-three/drei'
import {Canvas, useThree, useFrame} from '@react-three/fiber'
import { useEffect, useRef, useState, setState, useLayoutEffect } from 'react'
import { useRouter } from 'next/router'
import { Button } from '@mui/material'
import { useSpring } from 'react-spring'

import Stuff from './RoomPage/Stuff'
import MapStuff from './RoomPage/MapStuff'

// data
import userStuff from './userStuff.json'
import walltest from './walltest.json'

export default function Room( { StuffClick, ...props} ) {
    // 화면 확대 정도 조정.
    const [zoomscale] = useState(90);

    const [userID, setUserID] = useState(0);
    const [mapstuffs, setMapstuffs] = useState([]);
    const [stuffs, setStuffs] = useState([]);
    const [hovered, setHovered] = useState(0);
    const [clicked, setClicked] = useState(0);
    const { spring } = useSpring({
        spring : clicked,
        config : {mass : 5, tension : 400, friction : 70, precision : 0.0001 },
    })
    const positionY = spring.to([0, 1], [0, 10]);

    const [tagon, setTagon] = useState(true);
    const [camloc, setCamloc] = useState([0, 0, 0]);

    const router = useRouter();
    // 마운트시 stuff 로드
    useEffect(() => {
        if(!router.isReady) return;

        setUserID(router.query.userID);     
        // stuff 가져오기
        setMapstuffs(userStuff[router.query.userID].slice(0, 2));
        setStuffs(userStuff[router.query.userID].slice(2));

    }, [router.isReady]);

    // 마우스가 움직일 때 위치 받기.
    const [mouseloc, setmouseloc] = useState([325, 375]);

    // stuff 호버 이벤트.
    function Hover(e, stuff) { setHovered(); }
    
    // stuff 클릭 이벤트.
    function Click(e, stuff) {
        if(stuff.category == 'deco') return null;
        
        setClicked(Number(!clicked));
        if(!clicked) {
            setCamloc([0,5, 0]);
        }
        else {
            setCamloc([0, 0, 0]);
        }
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

            camera.position.x = distance - xoff + camloc[0];
            camera.position.y = distance - yoff + camloc[1];
            camera.position.z = distance + camloc[2];
            
            camera.lookAt(xoff/20, yoff/100 + camloc[1], 0);
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
                {/* 태그 토글 버튼 */}
                <Button variant="outlined"
                    style={{
                        position : 'absolute',
                        zIndex : '2'
                    }}
                    sx={{
                        m : 2
                    }}
                    onClick={() => setTagon(!tagon)}
                >
                    {
                        (tagon) ? <div>태그 숨기기.</div>
                        : <div>태그 보기.</div>
                    }
                </Button>

            {/* 캔버스 영역 */}
            <Canvas
                style={{
                    zIndex : '1'
                }}
                shadows
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
                <mesh >
                    <OrthographicCamera makeDefault zoom={zoomscale} />
                    
                </mesh>
                
                {/* 실제 구현될 방 요소 */}
                {/* 그림자를 받을 요소, 그림자를 뱉을 요소로 나눔. */}
                {/* 그림자 받을 요소 */}
                <mesh receiveShadow>
                    { mapstuffs.map( stuff => 
                        <MapStuff
                            Hover={Hover}
                            Click={Click}
                            
                            data={stuff}
                        />
                    )}
                </mesh>
                
                {/* 그림자 뱉을 요소 */}
                {/* 이거 클로저 함수로 컴포넌트 리턴받도록 변경할 것. */}
                <mesh castShadow>
                    { stuffs.map( stuff => 
                        <Stuff
                            Hover={Hover}
                            Click={Click}

                            data={stuff} 

                            key={stuff.name}
                        />
                    )}
                </mesh>

                {/* <OrbitControls /> */}
            </Canvas>
        </div>
      ) 
}