import { Paper, Typography, Box } from "@mui/material";
import { useState } from "react";
import ShowWindow from "../components/ShopPage/ShowWindow";

// furniture.json안에 stuff종류와 스터프들이 다 들어가야 함.
import furniture from '../components/ShopPage/furniture.json'

export default function Shop() {
    // 실제로는 db에서 카테고리 받아오기.
    const [furni] = useState(furniture);

    console.log(furni);

    return (
        <Box>
            <Box>
                크레딧이랑, 그런거 보여주기
            </Box>
            <Box>
                Stuff 클릭시 상세 정보 보여줄 위치
            </Box>


            { furni.map( f => 
                    <Box
                        key={f.name}
                        sx={{
                            mx : 2
                        }}
                    >
                        <Typography variant='h5'>{f.alias}</Typography>
                        <ShowWindow />
                    </Box>
                )}
        </Box>
    )
}