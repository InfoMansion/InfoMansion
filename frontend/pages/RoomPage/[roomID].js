import { useState } from "react";
import Room from "../../components/Room";
import { Box, Container, Grid } from '@mui/material'
import UserInfo from "../../components/RoomPage/UserInfo";
import { useRouter } from 'next/router'
import { styled } from "@mui/material/styles";

export default function RoomPage() {
    const [roomID, setRoomID] = useState(useRouter().query.roomID);

    // userID 가져오는 코드 작성해야 함.
    // 아직 작동은 안하지만, 사용자별 방을 보여줄 때 활용할 변수.
    const [userID, setUserID] = useState(1);
    return (
        <Container
            style={{
                margin : '30px auto'
            }}
        >
            <Grid 
                container
                spacing={2}
                sx={{
                    flexDirection:'row-reverse',
                    justifyContent : 'center'
                }}
            >

                {/* 이곳에 룸페이지가 구현됩니다. */}
                <Grid item 
                    lg={4}
                >
                    <UserInfo userID = {userID} />
                </Grid>
                <Grid item lg={7}>
                    {/* 여기에 post 페이지 */}
                    <Room roomID={roomID} />
                </Grid>
            </Grid>
        </Container>
    )
}