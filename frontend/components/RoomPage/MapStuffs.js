import { useEffect, useReducer } from 'react'
import MapStuff from './atoms/MapStuff'

export default function MapStuffs({Hover, Click, stuffs}) {
    return (
        <mesh receiveShadow>
        { stuffs.map( (stuff, index) => 
            <MapStuff
                Hover={Hover}
                Click={Click}
                
                data={stuff}
                key={index}
            />
        )}
        </mesh>
    )
}