import { Circle, CubicBezierLine, Image, QuadraticBezierLine, Text } from "@react-three/drei";
import { useFrame } from "@react-three/fiber"
import { useEffect, useRef, useState } from "react"
import { Color } from "three"

export default function StuffTag({children, ...props}) {

    const color = new Color();
    const fontProps = { 
        font: '/fonts/NanumSquareRound Regular_Regular.json', 
        fontSize: 0.15,
        letterSpacing: -0.1, 
        lineHeight: 1, 
        'material-toneMapped': false 
    };

    const textref = useRef();
    const locref = useRef();

    const [hovered, setHovered] = useState(false);

    const over = (e) => (e.stopPropagation(), setHovered(true));
    const out = () => setHovered(false);

    // 마우스 커서 변경.
    useEffect(() => {
        if (hovered) document.body.style.cursor = 'pointer'
        return () => (document.body.style.cursor = 'auto')
    }, [hovered])

    useFrame(({ camera, mouse }) => {
        locref.current.quaternion.copy(camera.quaternion)
        textref.current.material.color.lerp(color.set(hovered ? '#ffa0a0' : 'black'), 0.1)
    })

    return <group 
                ref={locref}
                position={[0,0,0]}
            >
                <mesh>
                    <circleGeometry attach="geometry" args={[0.4, 20]} />
                    <meshBasicMaterial attacj="material" color="red" />
                </mesh>
                <Text 
                    ref={textref} 
                    onPointerOver={over} 
                    onPointerOut={out} 
                    {...props} 
                    {...fontProps} 
                    children={children} 
                
                    position={[0, 0, 0.01]}
                />
            </group> 
}