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
import RoomLight from './RoomPage/atoms/RoomLight'

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
        console.log(stuff.category + "클릭");
        if(stuff.category == 'NONE') return null;
        
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
                <RoomLight />
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