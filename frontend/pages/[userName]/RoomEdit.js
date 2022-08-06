import { useEffect, useRef, useState } from "react";
import { Box, Button, Card, Container, Grid, Typography } from '@mui/material'
import { useRouter } from 'next/router'
import EditRoom from "../../components/EditRoom";
import MyStuffList from "../../components/RoomEditPage/MyStuffList";

import Link from "next/link";
import axios from "../../utils/axios";
import { useCookies } from "react-cookie";

export default function RoomEdit() {
    const [cookies] = useCookies(['cookie-name']);
    const editRoomRef = useRef();
    const router = useRouter();

    const [userName, setuserName] = useState(0);
    const [stuffon, setStuffon] = useState('');

    const [stuffs, setStuffs] = useState([]);    
    const [wallStuffs, setWallStuffs] = useState([]);
    const [floorStuffs, setFloorStuffs] = useState([]);
    const [locatedStuffs, setLocatedStuffs] = useState([]);
    
    // 현재 편집중인 스터프.
    const [editStuff, setEditStuff] = useState({});
    const [wallStuff, setWallStuff] = useState({
            "id" : "0",
            "category" : "NONE",
            "pos_x" : 0,
            "pos_y" : 0,
            "pos_z" : 0,
            "rot_x" : 0,
            "rot_z" : 0,
            "rot_y" : 0,
            "stuffAlias" : "기본 나무 바닥",
            "stuffName" : "floor_1958",
            "stuffNameKor" : "나무 바다아아아아아아악",
            "stuffGlbPath" : "/stuff-assets/floor_1958.glb",
            "geometry" : "low_poly_interior1958",
            "material" : "low_poly_interior"
    });
    const [floorStuff, setFloorStuff] = useState({});

    // stuff 가져오기.
    useEffect(() => {
        if(!router.isReady) return;
        try{
            axios.get(`/api/v1/userstuffs/list`, {
                headers : {
                    Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                }
            })
            .then( res => {
                setWallStuffs(res.data.data.filter(stuff => stuff.stuffType == 'WALL'))
                setFloorStuffs(res.data.data.filter(stuff => stuff.stuffType == 'FLOOR'))
                setStuffs(res.data.data.filter(stuff => stuff.stuffType != 'WALL' && stuff.stuffType != 'FLOOR'));
            })
        }catch(e) {
            console.log(e);
        }
    }, [router.isReady]);
    
    useEffect(() => {
        setWallStuff(wallStuffs.filter(stuff => stuff.selected)[0]);
    }, [wallStuffs])
    useEffect(() => {
        setFloorStuff(floorStuffs.filter(stuff => stuff.selected)[0]);
    }, [floorStuffs])

    useEffect(() => {
        setLocatedStuffs(stuffs.filter(stuff => stuff.selected));
    }, [stuffs])

    function StuffClick(stuff) {
        // 여기서 stuffpage로 변수 전달하면 됨.
        setStuffon(!stuffon);
    }

    function MapClick(stuff, tag) {
        if(tag == 1) {
            setFloorStuff(stuff);
        }else
            setWallStuff(stuff);
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
                                MapClick={MapClick}

                                stuffs={stuffs}
                                wallStuffs={wallStuffs}
                                floorStuffs={floorStuffs}
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

                        mapStuffs={[wallStuff, floorStuff]}
                        stuffs={locatedStuffs}
                    />
                </Grid>
            </Grid>
        </Container>
    )
}