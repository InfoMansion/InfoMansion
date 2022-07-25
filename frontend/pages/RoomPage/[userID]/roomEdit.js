import { useRef, useState } from "react";
import { Box, Button, Container, Grid } from '@mui/material'
import { useRouter } from 'next/router'
import EditRoom from "../../../components/EditRoom";


export default function RoomPage() {
    const [userID, setUserID] = useState(useRouter().query.userID);
    const [stuff, setStuff] = useState({});
    const [stuffon, setStuffon] = useState('');

    function StuffClick(stuff) {
        // 여기서 stuffpage로 변수 전달하면 됨.
        setStuffon(!stuffon);
        setStuff(stuff);
    }

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
                    flexDirection :'row-reverse',
                    justifyContent : 'center'
                }}
                >
                <Grid item lg={4}
                    style={{
                        backgroundColor : 'red'
                    }}
                    >
                    편집 페이지 입니다. 캡쳐 테스트를 위해 사용됩니다.
                </Grid>


                <Grid item lg={7}>
                    <Box sx={{ my : 1 }} >
                        <EditRoom 
                            StuffClick={StuffClick} 
                            userID={userID} 
                        >

                        </EditRoom>
                    </Box>
                </Grid>
            </Grid>
        </Container>
        
    )
}