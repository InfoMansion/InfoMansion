import { useThree } from "@react-three/fiber";
import { forwardRef, useImperativeHandle } from "react";
import * as THREE from 'three'
import { isPowerOfTwo } from "three/src/math/MathUtils";

const ScreenshotButton = forwardRef((props, ref) => {
    useImperativeHandle(ref, () => ({
        ScreenShot
    }))
    const { gl, scene, camera } = useThree();

    function ScreenShot() {
        gl.render(scene, camera);
        gl.toneMapping = THREE.ACESFilmicToneMapping;
        gl.toneMappingExposure = 1;
        gl.outputEncoding = THREE.sRGBEncoding;
        gl.preserveDrawingBuffer = true;

        gl.domElement.toBlob(
            function(blob) {
                var a = document.createElement('a')
                var url = URL.createObjectURL(blob)
                a.href = url
                a.download = 'canvas.png'
                a.click()
                console.log('function is actually being used')
                },
            'image/jpg',
            1.0
        )
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