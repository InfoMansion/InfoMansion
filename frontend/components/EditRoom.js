import { OrthographicCamera } from '@react-three/drei'
import {Canvas} from '@react-three/fiber'
import { useEffect, useState } from 'react'
import { useRouter } from 'next/router'
import { useSpring } from 'react-spring'

// data
import userStuff from './RoomPage/atoms/userStuff.json'
import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'
// 이 파일은 나중에 db에 데이터 넣을 때 쓸거라 안지우고 유지하겠습니다.
// import walltest from './walltest.json'
import EditRoomCamera from './RoomPage/atoms/RoomEditCamera'
import ScreenshotButton from './RoomPage/atoms/ScreenShotButton'

export default function EditRoom( { StuffClick, ...props} ) {
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
                width : "610px", 
                height : "610px",
                // margin : '30px auto'
                }}
            >

            {/* 캔버스 영역 */}
            <Canvas
                style={{
                    zIndex : '1'
                }}
                shadows
                onCreated={state => state.gl.setClearColor("#ffffff")} >
                
                {/* light */}
                {/* 이것도 언젠가 컴포넌트화 할 것 */}
                <ambientLight intensity={0.2} />
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

                {/* camera */}

                <ScreenshotButton />
                
                {/* <Box></Box> */}
                <EditRoomCamera camloc={camloc}/>
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
                    status={'edit'}
                />
            </Canvas>
        </div>
      ) 
}