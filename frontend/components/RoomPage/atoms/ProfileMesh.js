import { Circle, Image, Loader } from "@react-three/drei";
import { MeshBasicMaterial, TextureLoader } from "three";


export default function ProfileMesh({pos, profileImage}) {
    // const url = window.URL.createObjectURL(profileImage);
    
    const loader = new TextureLoader();
    loader.setCrossOrigin = 'anonymous';

    console.log(profileImage);
    
    let texture = new MeshBasicMaterial({
        map : loader.load("http://www.urbanbrush.net/web/wp-content/uploads/edd/2020/02/urbanbrush-20200227023608426223.jpg")
    })

    return (
        <group
            position={pos}
        >
            <Circle args={[0.6, 20]}
                material={texture} />
        </group>
    )
}