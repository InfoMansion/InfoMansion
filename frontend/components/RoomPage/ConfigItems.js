import { OrbitControls, OrthographicCamera } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
import { useEffect, useRef, useState } from 'react';
import ConfigStuffs from '../jsonData/ConfigStuffs.json'
import ConfigStuff from './atoms/ConfigStuff'
import ProfileMesh from './atoms/ProfileMesh';


export default function ConfigItems({loginUser, pagePush, userName, profileImage}) {

    const locref = useRef();
    useFrame(({camera}) => {
        locref.current.quaternion.copy(camera.quaternion);
    }, [])

    console.log(ConfigStuffs);
    const [positions] = useState([
        [0.4, 0.5, -2],
        [-0.4, -0.5, -2]
    ])

    function ClickFollow() {
        
    }
    function ClickUserInfo() {
        
    }
    function ClickShop() {
        pagePush("/Shop");
    }
    function ClickSetting() {
        pagePush(`/${userName}/dashboard`);
    }
    function ClickWrite() {

    }

    return (
        <group ref={locref}>
            {loginUser ? 
                <group>
                    {/* 설정 */}
                    <ConfigStuff data={ConfigStuffs[1]} pos={positions[1]} iniscale={1.6} Click={ClickSetting} 
                        color="#FFF89D" inicolor="#97A371"
                        speed={0.002}
                    />
                    {/* 상점 */}
                    <ConfigStuff data={ConfigStuffs[2]} pos={positions[0]} iniscale={3} Click={ClickShop} 
                        speed={-0.001}
                    />

                    {/* 글쓰기도 들어와야 함 */}
                </group>
                : <></>
            }
            <ambientLight />
            <OrthographicCamera makeDefault  zoom={100}/>
            {/* <OrbitControls /> */}
        </group>
    )    
}