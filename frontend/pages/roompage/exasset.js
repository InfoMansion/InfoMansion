import { useFrame } from "@react-three/fiber";
import { useRef } from "react";
import { useGLTF } from "@react-three/drei"

export default function exasset({...props}) {
    // const mesh = useRef(null);
    // useFrame(() => (mesh.current.rotation.y =  mesh.current.rotation.z += 0.01)); // #2

    const publicUri = process.env.PUBLIC_URL;

    const group = useRef();
    const { nodes, materials } = useGLTF('./map_ini.gltf')

    return (
        <group ref={group} {...props} dispose={null}>
            <group rotation={[-Math.PI / 2, 0, 0]}>
                <mesh geometry={nodes.Object_2.geometry} material={materials.matantimeridian_material} />
                <mesh geometry={nodes.Object_3.geometry} material={materials.matequator_material} />
                <mesh geometry={nodes.Object_4.geometry} material={materials.matlatlon_material} />
                <mesh geometry={nodes.Object_5.geometry} material={materials.matmeridian_material} />
                <mesh geometry={nodes.Object_6.geometry} material={materials.matsurface_material} />
            </group>
        </group>

        // <mesh>
        //     <sphereBufferGeometry />
        //     <meshStandardMaterial color="red" />
        // </mesh>
    );
  }