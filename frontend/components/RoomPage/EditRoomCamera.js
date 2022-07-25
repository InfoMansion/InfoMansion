import { useFrame } from "@react-three/fiber";

export default function EditRoomCamera({camloc}) {
    useFrame(({camera}) => {
        const distance = 40;

        camera.position.x = distance;
        camera.position.y = distance;
        camera.position.z = distance;
        
        camera.lookAt(0, 0, 0);
    })
    return null
}