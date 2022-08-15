import { Text } from "@react-three/drei";
import { useEffect, useRef, useState } from "react"

export default function TagButton({click, text, pos, rot, fontSize}) {
    const textref = useRef();
    const [hovered, setHovered] = useState(false);

    const fontProps = {
        fontSize : fontSize,
        letterSpacing : 0.2,
        'material-toneMapped' : false,
    }
    useEffect(() => {
        if (hovered) document.body.style.cursor = 'pointer'
        return () => (document.body.style.cursor = 'auto')
    }, [hovered])

    return (
        <Text
            position ={pos}
            rotation={rot}
            onPointerEnter={() => setHovered(true)}
            onPointerLeave={() => setHovered(false)}
            onClick={click}
            ref={textref} {...fontProps}
            children={text}
            color={hovered ? '#fc7a71' : 'white'}
        />
    )
}