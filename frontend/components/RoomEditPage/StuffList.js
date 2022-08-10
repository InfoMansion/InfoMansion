import { OrthographicCamera, Scroll, ScrollControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";

import EditStuff from "./atoms/EditStuff";
import { useRecoilState } from "recoil";
import { categoryState, editingState, editStuffState, fromState, positionState, rotationState } from "../../state/editRoomState";

const multicon = 2.5;
const yoff = 1;

// 스터프 하나씩 반환.
function RenderStuff({stuff, index, click}) {
    let x = (index%3 -1) * multicon;
    let y = parseInt(index/3) * -1 * multicon + yoff
    
    return (
        <group
            position={[x, y, 0]}
            scale={2}

            onClick={click}
        >
            <EditStuff data={stuff} />
        </group>
    )
}

export default function StuffList({stuffs}) {
    const [, setEditStuff] = useRecoilState(editStuffState);
    const [, setEditing] = useRecoilState(editingState);
    const [, setEditPosition] = useRecoilState(positionState);
    const [, setEditRotation] = useRecoilState(rotationState);
    const [, setFrom] = useRecoilState(fromState);
    const [, setEditCategory] = useRecoilState(categoryState);

    function click(stuff) {
        setEditStuff(stuff);
        setEditing(true);

        setEditPosition([stuff.posX, stuff.posY, stuff.posZ]);
        setEditRotation([stuff.rotX, stuff.rotY, stuff.rotZ]);
        setEditCategory("NONE");
        setFrom('unlocated');
    }

    return (
        <Canvas
            style={{
                width : '100%',
                height : '300px',
            }}
        >
            {/* <ScrollControls
                vertical
                damping={10}
                pages={3}
            >
                <Scroll> */}
                    {stuffs.map((stuff, index) => (
                        <RenderStuff 
                            index={index} key={index}
                            stuff={stuff} 
                            click={() => click(stuff)}
                        />
                    ))}
                {/* </Scroll>
            </ScrollControls> */}
            
            <OrthographicCamera 
                makeDefault
                position={[0,0, 4]}
                zoom={50}   
            />
        </Canvas>
    )
}