import { OrthographicCamera } from '@react-three/drei'
import {Canvas, useThree} from '@react-three/fiber'
import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'
import { useRouter } from 'next/router'
import { useSpring } from 'react-spring'

// data
import userStuff from './jsonData/userStuffs.json'
import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'
// 이 파일은 나중에 db에 데이터 넣을 때 쓸거라 안지우고 유지하겠습니다.
// import walltest from './walltest.json'
import EditRoomCamera from './RoomPage/atoms/RoomEditCamera'
import ScreenshotButton from './RoomPage/atoms/ScreenShotButton'
import RoomLight from './RoomPage/atoms/RoomLight'


const EditRoom = forwardRef(( props, ref ) => {
    useImperativeHandle(ref, () => ({
        ScreenShot
    }))

    const ScreenShotButtonRef = useRef();
    function ScreenShot() {
        ScreenShotButtonRef.current.ScreenShot();
    }

    // 화면 확대 정도 조정.
    const [zoomscale] = useState(90);

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
        // stuff 가져오기
        setMapstuffs(userStuff[router.query.userName].slice(0, 2));
        setStuffs(userStuff[router.query.userName].slice(2));

    }, [router.isReady]);

    // stuff 호버 이벤트.
    function Hover(e, stuff) { setHovered(); }
    
    // stuff 클릭 이벤트.
    function Click(e, stuff) {
        console.log(stuff.category + "클릭");
        if(stuff.category == 'NONE') return null;
        
        props.StuffClick(stuff);
    }


    return (
        <Canvas
            style={{
                zIndex : '1',
                width : "610px", 
                height : "610px",
            }}
            shadows
            onCreated={state => state.gl.setClearColor("#ffffff")} >
            <RoomLight />
            {/* camera */}

            <ScreenshotButton ref={ScreenShotButtonRef} />
            
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
      )
});

export default EditRoom;