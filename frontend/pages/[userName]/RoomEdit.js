import { useEffect, useRef, useState } from "react";
import { Box, Button, Card, Container, Grid, Typography } from '@mui/material'
import { useRouter } from 'next/router'
import Link from "next/link";
import axios from "../../utils/axios";
import { useCookies } from "react-cookie";

import EditRoom from "../../components/EditRoom";
import MyStuffList from "../../components/RoomEditPage/MyStuffList";
import EditConsole from "../../components/RoomEditPage/EditConsole";
import { useRecoilState } from "recoil";
import { editingState, editStuffState, fromState, positionState, rotationState } from "../../state/editRoomState";


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
    const [unlocatedStuffs, setUnlocatedStuffs] = useState([]);
    
    // 현재 편집중인 스터프.
    const [editing, setEditing] = useRecoilState(editingState);
    const [editStuff, setEditStuff] = useRecoilState(editStuffState);
    const [, setEditPosition] = useRecoilState(positionState);
    const [, setEditRotation] = useRecoilState(rotationState);
    const [from, setFrom] = useRecoilState(fromState);

    const [wallStuff, setWallStuff] = useState({});
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
                // console.log(res.data);

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
        setUnlocatedStuffs(stuffs.filter(stuff => !stuff.selected));
    }, [stuffs])

    function StuffClick(e, stuff) {
        // 여기서 stuffpage로 변수 전달하면 됨.
        
        if(editing) return;
        console.log(stuff);
        
        setEditing(true);
        setFrom('located');
        
        setEditStuff(stuff);
        setEditPosition([stuff.posX, stuff.posY, stuff.posZ]);
        setEditRotation([stuff.rotX, stuff.rotY, stuff.rotZ]);

        // locatedstuff배열에서 stuff 빼기.
        let copylocateddata = locatedStuffs.filter(data => data.userStuffId != stuff.userStuffId);
        console.log(copylocateddata);
        console.log(copylocateddata);
        setLocatedStuffs(copylocateddata);
    }

    function MapClick(stuff, tag) {
        if(tag == 1) {
            setFloorStuff(stuff);
        }else
            setWallStuff(stuff);
    }

    function EndEdit() {
        let senddata = [{...wallStuff}, {...floorStuff}, ...locatedStuffs];
        console.log(senddata);
        editRoomRef.current.ScreenShot();
    }
    
    function reset() {
        setEditing(false);
    }
    function addLocateStuff(e, stuff) {
        console.log(stuff);
        setLocatedStuffs([...locatedStuffs, stuff]);
    }
    function addUnlocatedStuff(e, stuff) {
        console.log(stuff);
        setUnlocatedStuffs([...unlocatedStuffs, stuff]);
    }
    function deleteUnlocatedStuff(e, stuff) {
        let unlocateddata = unlocatedStuffs.filter( data => data.userStuffId != stuff.userStuffId);
        setUnlocatedStuffs(unlocateddata);
        console.log(stuff);
        console.log(unlocateddata);
    }

    return (
        <Container
            style={{
                margin : '30px auto'
            }}
        >
            <Button onClick={reset}>임시리셋</Button>
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
                                {editing ? editStuff.alias : "편집할 에셋을 클릭하세요" }
                            </Typography>
                            {editing ?
                                <EditConsole 
                                    addLocateStuff={addLocateStuff}
                                    addUnlocatedStuff={addUnlocatedStuff}
                                    deleteUnlocatedStuff={deleteUnlocatedStuff}
                                />
                                :
                                <Box>
                                    <MyStuffList
                                        MapClick={MapClick}
                                        stuffs={unlocatedStuffs}
                                        wallStuffs={wallStuffs}
                                        floorStuffs={floorStuffs}
                                    />
                                    <Box
                                        sx={{
                                            position : 'absolute',
                                            right : 10,
                                            bottom : 10
                                        }}                      
                                    >

                                        <Button variant="outlined" >
                                            <Link href={`/${userName}`}>
                                                취소
                                            </Link>
                                        </Button>
                                        <Button
                                            variant="contained"
                                            onClick={EndEdit}
                                        >
                                            {/* 추후에 변경된 사항 저장할지 묻는 기능 필요. */}
                                            편집 완료
                                        </Button>
                                    </Box>
                                </Box>
                            }
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