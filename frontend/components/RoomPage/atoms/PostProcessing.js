import { EffectComposer, Bloom } from '@react-three/postprocessing'

export default function PostProcessing({luminanceThreshold = 0.5, luminanceSmoothing = 1, intensity = 0.1}) {
    return (
        <EffectComposer multisampling={8}>
            <Bloom 
                luminanceThreshold={luminanceThreshold} 
                luminanceSmoothing={luminanceSmoothing} 
                intensity={intensity} 
            />
        </EffectComposer>
    )
}