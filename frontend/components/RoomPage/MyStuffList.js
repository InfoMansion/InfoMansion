import { Box } from "@mui/material";
import { Circle } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";

export default function MyStuffList() {
    return (
        <Box>
            <Canvas>
                <Circle />
            </Canvas>
        </Box>
    )
}