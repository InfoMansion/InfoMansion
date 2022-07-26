import { useFrame } from "@react-three/fiber";

export default function RoomCamera({camloc}) {
    useFrame(({mouse, camera}) => {
        const distance = 40;
        const con = 3;
        const xoff = mouse.x*con;
        const yoff = mouse.y*con;

        camera.position.x = distance - xoff + camloc[0];
        camera.position.y = distance - yoff + camloc[1];
        camera.position.z = distance + camloc[2];
        
        camera.lookAt(xoff/20, yoff/100 + camloc[1], 0);
    })
    return null
}