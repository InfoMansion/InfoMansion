import { OrthographicCamera } from '@react-three/drei'
import {Canvas} from '@react-three/fiber'
import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'

import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'
// 이 파일은 나중에 db에 데이터 넣을 때 쓸거라 안지우고 유지하겠습니다.
// import walltest from './walltest.json'
import EditRoomCamera from './RoomPage/atoms/RoomEditCamera'
import ScreenshotButton from './RoomPage/atoms/ScreenShotButton'
import RoomLight from './RoomPage/atoms/RoomLight'
import { useRecoilState } from 'recoil'
import { editingState, editStuffState, positionState, rotationState } from '../state/editRoomState'

import EditingStuff from './RoomEditPage/atoms/EditingStuff'

const EditRoom = forwardRef(( {mapStuffs, stuffs, StuffClick}, ref ) => {
    useImperativeHandle(ref, () => ({ ScreenShot }))
    const ScreenShotButtonRef = useRef();
    function ScreenShot() { ScreenShotButtonRef.current.ScreenShot(); }
    
    const [zoomscale] = useState(90);

    const [editingStuff] = useRecoilState(editStuffState);
    const [editing] = useRecoilState(editingState);
    const [editingposition] = useRecoilState(positionState);
    const [editingrotation] = useRecoilState(rotationState);

    // stuff 호버 이벤트.
    function Hover(e, stuff) {}
    
    // stuff 클릭 이벤트.
    function Click(e, stuff) { 
        console.log(stuff);
        StuffClick(e, stuff);
    }

    return (
        <Canvas
            style={{
                zIndex : '1',
                width : "610px", 
                height : "610px",
            }}
            shadows
        >
            <RoomLight />

            <ScreenshotButton ref={ScreenShotButtonRef} />
            <EditRoomCamera camloc={[0, 0, 0]}/>
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

            {editing ?
                <EditingStuff 
                    data={editingStuff}
                    position={editingposition}
                    rotation={editingrotation}
                />
                : <></>
            }
        </Canvas>
      )
});

export default EditRoom;