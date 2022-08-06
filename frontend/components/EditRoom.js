import { OrthographicCamera } from '@react-three/drei'
import {Canvas} from '@react-three/fiber'
import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'
import { useSpring } from 'react-spring'

import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'
// 이 파일은 나중에 db에 데이터 넣을 때 쓸거라 안지우고 유지하겠습니다.
// import walltest from './walltest.json'
import EditRoomCamera from './RoomPage/atoms/RoomEditCamera'
import ScreenshotButton from './RoomPage/atoms/ScreenShotButton'
import RoomLight from './RoomPage/atoms/RoomLight'
import MapStuff from './RoomPage/atoms/MapStuff'

const EditRoom = forwardRef(( {mapStuffs, stuffs, StuffClick}, ref ) => {
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

    const [hovered, setHovered] = useState(0);
    const [clicked, setClicked] = useState(0);
    const { spring } = useSpring({
        spring : clicked,
        config : {mass : 5, tension : 400, friction : 70, precision : 0.0001 },
    })
    const [camloc, setCamloc] = useState([0, 0, 0]);

    // stuff 호버 이벤트.
    function Hover(e, stuff) { setHovered(); }
    
    // stuff 클릭 이벤트.
    function Click(e, stuff) {
        console.log(stuff.category + "클릭");
        if(stuff.category == 'NONE') return null;
        
        StuffClick(stuff);
    }

    useEffect(() => {
        console.log(stuffs);
    }, [])

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

            <ScreenshotButton ref={ScreenShotButtonRef} />
            <EditRoomCamera camloc={camloc}/>
            <OrthographicCamera makeDefault zoom={zoomscale} />
            
            {/* 벽, 바닥 */}
            <MapStuffs
                Hover={Hover}
                Click={Click}
                stuffs={mapStuffs}
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