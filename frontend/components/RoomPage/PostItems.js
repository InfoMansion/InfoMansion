import { OrthographicCamera } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
import { useRef, useState } from 'react';
import ConfigStuffs from '../jsonData/ConfigStuffs.json'
import ConfigStuff from './atoms/ConfigStuff'


export default function PostItems({popupPosts, userName, profileImage}) {
    const locref = useRef();
    useFrame(({camera}) => {
        locref.current.quaternion.copy(camera.quaternion);
    }, [])

    const [positions] = useState([
        [0, 0.2, -5],
        [0.7, -0.9, -5]
    ])
    function ClickPostbox(e) {
        popupPosts(e, `postBox`);
    }
    function ClickGuestBook(e) {
        popupPosts(e, `guestBook`);
    }

    return (
        <group ref={locref}>
            <group receiveShadow>
                {/* postbox */}
                <ConfigStuff data={ConfigStuffs[4]} pos={positions[0]} iniscale={2} Click={ClickPostbox} 
                    color="#FFF89D" inicolor="#E0A912"
                    speed={0.001}
                />
                {/* guestbook */}
                <ConfigStuff data={ConfigStuffs[5]} pos={positions[1]} iniscale={100} Click={ClickGuestBook} 
                    speed={0}
                />
            </group>
            <ambientLight intensity={1}/>
            <pointLight position={[2,2,2]}/>
            <OrthographicCamera makeDefault zoom={100}/>
        </group>
    )    
}