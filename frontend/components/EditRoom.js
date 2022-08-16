import { OrthographicCamera } from '@react-three/drei'
import {Canvas} from '@react-three/fiber'
import { forwardRef, useImperativeHandle, useRef, useState } from 'react'

import MapStuffs from './RoomPage/MapStuffs'
import Stuffs from './RoomPage/Stuffs'
import EditRoomCamera from './RoomPage/atoms/RoomEditCamera'
import ScreenshotButton from './RoomPage/atoms/ScreenShotButton'
import RoomLight from './RoomPage/atoms/RoomLight'
import { useRecoilState } from 'recoil'
import { editingState, editStuffState, positionState, rotationState } from '../state/editRoomState'

import EditingStuff from './RoomEditPage/atoms/EditingStuff'
import TagButton from './RoomPage/atoms/TagButton'

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
            <pointLight position={[10, 20, 4]} intensity={1}/>

            <ScreenshotButton ref={ScreenShotButtonRef} />
            <EditRoomCamera camloc={[0, 0, 0]}/>
            <OrthographicCamera makeDefault zoom={zoomscale} />
            {/* xyz 표시 */}
            {editing ? 
                <group>
                    <TagButton 
                        pos={[2.5, 0, 5.3]}
                        rot={[-1.58,0,-1.58]}
                        fontSize={0.7}
                        text={"X"}
                    />
                    <TagButton
                        pos={[1.6, 0, 4.9]}
                        rot={[0,0.78,0]}
                        fontSize={0.7}
                        text={"Y"}
                    />
                    <TagButton 
                        pos={[2, 0, 5.8]}
                        rot={[-1.58,0,0]}
                        fontSize={0.7}
                        text={"Z"}
                    />
                </group>
                : <></>
            }

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