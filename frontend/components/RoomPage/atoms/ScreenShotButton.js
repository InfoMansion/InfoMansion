import { useThree } from "@react-three/fiber";
import { forwardRef, useImperativeHandle, useState } from "react";
import { useCookies } from "react-cookie";
import * as THREE from 'three'
import axios from "../../../utils/axios";

const ScreenshotButton = forwardRef((props, ref) => {
    useImperativeHandle(ref, () => ({ ScreenShot }))
    const { gl, scene, camera } = useThree();
    const [cookies] = useCookies(['cookie-name']);

    function ScreenShot() {
        gl.render(scene, camera);
        gl.toneMapping = THREE.ACESFilmicToneMapping;
        gl.toneMappingExposure = 1;
        gl.outputEncoding = THREE.sRGBEncoding;
        gl.preserveDrawingBuffer = true;

        // 로컬에 저장.
        // gl.domElement.toBlob(
        //     function(blob) {
        //         console.log(blob); 
        //         var a = document.createElement('a')
        //         var url = URL.createObjectURL(blob)
        //         a.href = url
        //         a.download = 'canvas.jpg'
        //         a.click()
        //         console.log('function is actually being used')
        //     },
        //     'image/jpg',
        //     1.0
        // )
        
        // 서버에 사진 보내기.
        gl.domElement.toBlob( (blob) => {
            const formData = new FormData();
            formData.append("roomImg", blob);
            try {
                axios.put('/api/v1/rooms/edit', formData, {
                    headers: {
                        ContentType: 'multipart/form-data',
                        Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                    },
                })
                .then((res) => {
                    console.log(res);
                })
            } catch(e) {
                console.log(e);
            }
        }, 'image/png', 1.0)
    }
    return (
        <sprite {...props} position={[10, 10, 0]} scale={[1, 1, 1]} onClick={ScreenShot}>
            <spriteMaterial
                attach="material"
                color={'lightblue'}
                depthWrite={false}
                depthTest={false}
                renderOrder={10000}
                fog={false}
                onClick={ScreenShot}
            />
         </sprite>
    )
});

export default ScreenshotButton;