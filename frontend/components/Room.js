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
import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'
// 이 파일은 나중에 db에 데이터 넣을 때 쓸거라 안지우고 유지하겠습니다.
// import walltest from './walltest.json'
import RoomCamera from './RoomPage/RoomCamera'
import RoomLight from './RoomPage/RoomLight'

export default function Room( { StuffClick, ...props} ) {
    // 화면 확대 정도 조정.
    const [zoomscale] = useState(90);

    const [userID, setUserID] = useState(0);

    // 사용자 가구들.
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
        
        // setClicked 동기처리 되도록 바꿔야 함.
        setClicked(Number(!clicked));
        if(!clicked) { setCamloc([0,5, 0]); }
        else { setCamloc([0, 0, 0]); }
        // RoomPage의 stuffClick 함수 실행시키기.
        StuffClick(stuff);
    }

    // 카메라 위치 세팅
    return (
        <div 
            style={{ 
                width : "600px", 
                height : "700px",
                // margin : '30px auto'
                }}
            >
                {/* 태그 토글 버튼 */}
                <Button variant="outlined"
                    style={{
                        position : 'absolute',
                        zIndex : '2'
                    }}
                    sx={{ m : 2 }}
                    onClick={() => setTagon(!tagon)}
                >
                    {
                        (tagon) ? <div>태그 숨기기.</div>
                        : <div>태그 보기.</div>
                    }
                </Button>

            {/* 캔버스 영역 */}
            <Canvas
                style={{ zIndex : '1' }}
                shadows
                onCreated={state => state.gl.setClearColor("#ffffff")} >
                
                <RoomLight />

                <RoomCamera camloc={camloc}/>
                <OrthographicCamera makeDefault zoom={zoomscale} />
                
                {/* 벽, 바닥 */}
                <MapStuffs 
                    stuffs={mapstuffs}
                    Hover={Hover}
                    Click={Click}    
                />
                {/* stuffs */}
                <Stuffs 
                    stuffs={stuffs}
                    Hover={Hover}
                    Click={Click}
                    status={'view'}
                    tagon={tagon}
                />

                {/* <OrbitControls /> */}
            </Canvas>
        </div>
      ) 
}