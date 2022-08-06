import { Box, Typography } from "@mui/material";
import MapStuffList from './MapStuffList'
import StuffList from "./StuffList";

export default function MyStuffList({stuffs, wallStuffs, floorStuffs, MapClick, StuffClick}) { 
    return (
        <Box>
            <Typography>
                벽지
            </Typography>
            <MapStuffList 
                stuffs={wallStuffs} tag={0} posy={-0.25}
                click={MapClick}
            />

            <Typography>
                바닥
            </Typography>
            <MapStuffList 
                stuffs={floorStuffs} posy={0.6} tag={1}
                click={MapClick}
            />
            <Typography>
                가구
            </Typography>
            <StuffList stuffs={stuffs} />
        </Box>
    )
}