import { useEffect, useRef, useState } from "react";
import { Box, Button, Container, Grid, Typography } from '@mui/material'
import { useRouter } from 'next/router'
import EditRoom from "../../components/EditRoom";
import MyStuffList from "../../components/RoomEditPage/MyStuffList";

import userStuffSave from '../../components/RoomEditPage/userStuffsSave.json'
import Link from "next/link";
export default function RoomEdit() {
    const router = useRouter();
    const [userID, setUserID] = useState(0);
    const [stuff, setStuff] = useState({});
    const [stuffon, setStuffon] = useState('');

    // stuff 가져오기.
    const [inistuffs, setInistuffs] = useState([]);
    
    useEffect(() => {
        if(!router.isReady) return;

        setUserID(router.query.userID);

        // stuff 가져오기.
        setInistuffs(userStuffSave[router.query.userID])
    }, [router.isReady])

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
                spacing={0}
                sx={{
                    flexDirection :'row-reverse',
                    justifyContent : 'center'
                }}
                >
                <Grid item lg={4}
                    style={{
                        backgroundColor : '#fabcaf'
                    }}
                    sx={{
                        p : 1,
                        position : 'relative'
                    }}
                    >
                        <Typography variant='h6'>
                            편집할 에셋을 클릭하세요
                        </Typography>

                        <MyStuffList 
                            stuffs={inistuffs.filter(stuff => stuff.category == "UNLOCATED")}
                        />
                        <Button
                            variant="contained"
                            sx={{
                                position : 'absolute',
                                right : 10,
                                bottom : 10
                            }}
                        >
                            {/* 추후에 변경된 사항 저장할지 묻는 기능 필요. */}
                            <Link href={`/${userID}`}>
                                편집 종료
                            </Link>
                        </Button>
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