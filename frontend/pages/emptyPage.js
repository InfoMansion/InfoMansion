import { Canvas } from "@react-three/fiber";
import Particles from "../components/RoomPage/atoms/Particles"
import PostProcessing from "../components/RoomPage/atoms/PostProcessing"

export default function emptyPage() {
  return (
    <Canvas
      style={{
        height : window.innerHeight
      }}
    >
      <Particles 
        scale={0.02}
      />
      <PostProcessing
      luminanceSmoothing={5}
        intensity={100}
      />

    </Canvas>
  )
}