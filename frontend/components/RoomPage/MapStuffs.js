import MapStuff from './atoms/MapStuff'

export default function MapStuffs({Hover, Click, stuffs}) {
    return (
        <mesh receiveShadow>
        { stuffs.map( stuff => 
            <MapStuff
                Hover={Hover}
                Click={Click}
                
                data={stuff}
                key={stuff.stuff_name}
            />
        )}
        </mesh>
    )
}