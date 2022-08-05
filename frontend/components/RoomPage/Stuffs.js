import Stuff from './atoms/Stuff'

export default function Stuffs({tagon, status, Hover, Click, stuffs}) {
    return (
        <group>
            {/* 여기서 3항연산자로 deco인거랑 아닌거 구분하면 좋을듯 함. */}
            { stuffs.map( (stuff, index) => 
                <Stuff
                    Hover={Hover}
                    Click={Click}

                    data={stuff}
                    tagon={tagon}

                    key={index}
                    status={status}
                />
            )}
        </group> 
    )
}