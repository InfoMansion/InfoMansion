import { EffectComposer, Bloom } from '@react-three/postprocessing'

export default function PostProcessing() {
    return (
        <EffectComposer multisampling={8}>
            <Bloom luminanceThreshold={0.5} luminanceSmoothing={1} intensity={0.1} />
        </EffectComposer>
    )
}