import Stuff from './Stuff'

export default function Stuffs({status, Hover, Click, stuffs}) {

    return (
        <group>
            { stuffs.map( stuff => 
                <Stuff
                    Hover={Hover}
                    Click={Click}

                    data={stuff}

                    key={stuff.stuff_name}
                    status={status}
                />
            )}
        </group> 
    )
}