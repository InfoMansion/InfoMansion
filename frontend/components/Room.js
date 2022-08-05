import {Canvas} from '@react-three/fiber'
import { useEffect, useState, } from 'react'
import { useRouter } from 'next/router'
import { Box, Button } from '@mui/material'
import  RoomManageMenu from './RoomPage/RoomManageMenu'

// data
import userStuffs from './jsonData/userStuffs.json'
import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'

// 이 파일은 나중에 db에 데이터 넣을 때 쓸거라 안지우고 유지하겠습니다.
// import walltest from './walltest.json'
import RoomCamera from './RoomPage/atoms/RoomCamera'
import RoomLight from './RoomPage/atoms/RoomLight'
import axios from '../utils/axios'
import { useCookies } from 'react-cookie'

import { clickedStuffCategoryState } from '../state/roomState'
import { useRecoilState } from 'recoil'

export default function Room( { StuffClick, userName} ) {
    const [cookies] = useCookies(['cookie-name']);
    // 화면 확대 정도 조정.
    const [zoomscale] = useState(90);
    // 내 방인지 판단하는 변수
    const [myroom] = useState(true);
    
    // 사용자 가구들.
    const [mapstuffs, setMapstuffs] = useState([]);
    const [stuffs, setStuffs] = useState([]);
    const [hovered, setHovered] = useState(0);
    const [clicked, setClicked] = useState(0);
    
    const [tagon, setTagon] = useState(true);
    const [camloc, setCamloc] = useState([0, 0, 0]);
    const [, setClickedStuffCategory] = useRecoilState(clickedStuffCategoryState);
    
    // 마운트시 stuff 로드
    const router = useRouter();
    useEffect(() => {
        if(!router.isReady) return;
        // stuff 가져오기
        try {
            axios.get(`/api/v1/userstuffs/room/${router.query.userName}`, {
                headers : {
                    Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                }
            })
            .then( res => {
                setMapstuffs(res.data.data.slice(0, 2));
                setStuffs(res.data.data.slice(2));
            })
        } catch(e) {
            console.log(e);
        }
        
    }, [router.isReady]);

    // stuff 호버 이벤트.
    function Hover(e, stuff) { setHovered(); }
    
    // stuff 클릭 이벤트.
    function Click(e, stuff) {
        if(stuff.category == 'NONE') return null;
        
        // setClicked 동기처리 되도록 바꿔야 함.
        setClicked(() => {
            if(clicked) return 0;
            else return stuff.stuffNameKor;
        });
        setClickedStuffCategory(stuff.category);
        StuffClick(stuff);
    }

    return (
        <div 
            style={{ 
                width : "600px", 
                height : "700px",
                // margin : '30px auto'
                position : 'relative'
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
                {/* 내 방일 때만 표시 */}

                {myroom ?
                    <Box
                        style={{
                            position : 'absolute',
                            bottom : 0,
                            right : 0,
                            zIndex : '2'
                        }}
                        >
                        <RoomManageMenu 
                            userName={userName}
                        />
                    </Box>
                    : <></>
                }
    
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

                {/* <Stuff_s3test /> */}
            </Canvas>
        </div>
      ) 
}