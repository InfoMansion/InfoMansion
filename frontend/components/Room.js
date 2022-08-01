import { OrbitControls, OrthographicCamera } from '@react-three/drei'
import {Canvas} from '@react-three/fiber'
import { useEffect, useState, } from 'react'
import { useRouter } from 'next/router'
import { Box, Button } from '@mui/material'
import { easings, useSpring } from 'react-spring'
import { animated } from '@react-spring/three'
import  RoomManageMenu from './RoomPage/RoomManageMenu'

// data
import userStuff from './RoomPage/atoms/userStuff.json'
import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'

// 이 파일은 나중에 db에 데이터 넣을 때 쓸거라 안지우고 유지하겠습니다.
// import walltest from './walltest.json'
import RoomCamera from './RoomPage/atoms/RoomCamera'
import RoomLight from './RoomPage/atoms/RoomLight'

export default function Room( { StuffClick, ...props} ) {
    // 화면 확대 정도 조정.
    const [zoomscale] = useState(90);

    const [userID, setUserID] = useState(0);
    
    // 사용자 가구들.
    const [mapstuffs, setMapstuffs] = useState([]);
    const [stuffs, setStuffs] = useState([]);
    const [hovered, setHovered] = useState(0);
    const [clicked, setClicked] = useState(0);

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

    // stuff 호버 이벤트.
    function Hover(e, stuff) { setHovered(); }
    
    // stuff 클릭 이벤트.
    function Click(e, stuff) {
        if(stuff.category == 'NONE') return null;
        
        // setClicked 동기처리 되도록 바꿔야 함.
        setClicked(() => {
            if(clicked) return 0;
            else return stuff.stuff_name;
        });
        StuffClick(stuff);
    }

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
            <Canvas shadows
                style={{ zIndex : '1' }}
                onCreated={state => state.gl.setClearColor("#ffffff")} >
                
                <RoomLight />

                <RoomCamera 
                    camloc={camloc}
                    clicked={clicked}
                    zoomscale={zoomscale}
                />
                
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

            {/* 추후 오른쪽정렬 추가 예정 */}
            <Box
                sx={{
                    position : 'absolute',
                    zIndex : '2',
                    width : '100%',
                }}
            >
                <RoomManageMenu userID={userID}/>
            </Box>
        </div>
      ) 
}