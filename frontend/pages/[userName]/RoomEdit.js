import { useEffect, useReducer, useRef, useState } from "react";
import { Box, Button, Card, Container, Grid, Typography } from '@mui/material'
import { useRouter } from 'next/router'
import EditRoom from "../../components/EditRoom";
import MyStuffList from "../../components/RoomEditPage/MyStuffList";

import userStuffSave from '../../components/jsonData/userStuffsAll.json'
import userStuffs from '../../components/jsonData/userStuffs.json'

import Link from "next/link";

export default function RoomEdit() {
    const editRoomRef = useRef();

    const router = useRouter();
    const [userName, setuserName] = useState(0);
    const [stuff, setStuff] = useState({});
    const [stuffon, setStuffon] = useState('');

    // stuff 가져오기.
    const [saveStuffs, setSaveStuffs] = useState([]);
    const [locatedStuffs, setLocatedStuffs] = useState([]);
    
    // 현재 편집중인 스터프.
    const [editStuff, setEditStuff] = useState({});
    const [mapStuffs, setMapStuffs] = useState([]);

    useEffect(() => {
        if(!router.isReady) return;
        // stuff 가져오기.
        setuserName(router.query.userName);

        setSaveStuffs(userStuffSave[router.query.userName]);
        setLocatedStuffs(userStuffs[router.query.userName]);

    }, [router.isReady])

    // stuff를 불러왔을 때 실행.

    useEffect(() => {
        setMapStuffs(locatedStuffs.slice(0, 2));
    }, [locatedStuffs])

    function StuffClick(stuff) {
        // 여기서 stuffpage로 변수 전달하면 됨.
        setStuffon(!stuffon);
        setStuff(stuff);
    }
    function MapClick(stuff, tag) {
        let copyMap = [...mapStuffs];
        copyMap[tag] = stuff;
        setMapStuffs(copyMap);
    }

    function EndEdit() {
        // 편집여부가 있는지 판단.
        editRoomRef.current.ScreenShot();
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
                    }}
                    sx={{
                        p : 1,
                        position : 'relative'
                    }}
                    >
                        <Card>
                            <Box
                                sx={{
                                    m : 2,
                                    position : 'relative'
                                }}
                            >
                                <Typography variant='h6'>
                                    편집할 에셋을 클릭하세요
                                </Typography>

                                <MyStuffList
                                    stuffs={saveStuffs}
                                    MapClick={MapClick}
                                />
                                <Button
                                    variant="contained"
                                    sx={{
                                        position : 'absolute',
                                        right : 10,
                                        bottom : 10
                                    }}
                                    onClick={EndEdit}
                                >
                                    {/* 추후에 변경된 사항 저장할지 묻는 기능 필요. */}
                                    <Link href={`/${userName}`}>
                                        편집 종료
                                    </Link>
                                </Button>

                            </Box>
                        </Card>
                </Grid>

                <Grid item lg={7}>
                    <EditRoom
                        ref={editRoomRef}
                        StuffClick={StuffClick} 
                        userName={userName}

                        mapStuffs={mapStuffs}
                        stuffs={locatedStuffs}
                    />
                </Grid>
            </Grid>
        </Container>
        
    )
}