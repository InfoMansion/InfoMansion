import { Box, Card, Typography } from "@mui/material";
import { Canvas } from "@react-three/fiber";
import EditStuff from "./atoms/EditStuff";

import MapStuffList from './MapStuffList'
import StuffList from "./StuffList";

export default function MyStuffList({stuffs, MapClick, StuffClick}) { 
    return (
        <Box>
            <Typography>
                벽지
            </Typography>
            <MapStuffList 
                stuffs={stuffs.filter(stuff => (stuff.stuffType == "wall"))}
                tag={0}
                posy={-0.25}
                click={MapClick}
            />

            <Typography>
                바닥
            </Typography>
            <MapStuffList 
                stuffs={stuffs.filter(stuff => (stuff.stuffType == "floor"))}
                posy={0.6}
                tag={1}
                click={MapClick}
            />
            <Typography>
                가구
            </Typography>
            <StuffList 
                stuffs={stuffs.filter(stuff => (stuff.stuffType != "floor" && stuff.stuffType != "wall"))}
            />
        </Box>
    )
}