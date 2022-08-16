export default function RoomLight({on}) {
    return (
        <group>
            <ambientLight intensity={0.2} />
            <pointLight position={[10, 20, 4]} intensity={0.1}/>
            {on ?
                <group>
                    <pointLight position={[10, 20, 4]} intensity={0.2}/>
                    <directionalLight 
                        position={[20, 40, 20]} 
                        intensity={1}
                        castShadow
                        shadow-mapSize-width={10}
                        shadow-mapSize-height={10}
                        shadow-camera-far={50}
                        shadow-camera-left={-100}
                        shadow-camera-right={100}
                        shadow-camera-top={100}
                        shadow-camera-bottom={-100}
                    />
                </group>
                :
                <></>
            }
        </group>
    )
}