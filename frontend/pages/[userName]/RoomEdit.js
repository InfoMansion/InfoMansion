import { useEffect, useRef, useState } from 'react';
import {
  Box,
  Button,
  Card,
  Container,
  css,
  Divider,
  Grid,
  Typography,
} from '@mui/material';
import { useRouter } from 'next/router';
import Link from 'next/link';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import CustomAlert from '../../components/CustomAlert';
import EditRoom from '../../components/EditRoom';
import MyStuffList from '../../components/RoomEditPage/MyStuffList';
import EditConsole from '../../components/RoomEditPage/EditConsole';
import { useRecoilState, useSetRecoilState } from 'recoil';
import {
  categoryState,
  editingState,
  editStuffState,
  fromState,
  positionState,
  rotationState,
} from '../../state/editRoomState';
import { pageLoading } from '../../state/pageLoading';
import { ColorButton } from '../../components/common/ColorButton';

export default function RoomEdit() {
  const [cookies] = useCookies(['cookie-name']);
  const editRoomRef = useRef();
  const router = useRouter();
  const [userName, setUserName] = useState();

  const [stuffs, setStuffs] = useState([]);
  const [wallStuffs, setWallStuffs] = useState([]);
  const [floorStuffs, setFloorStuffs] = useState([]);
  const [locatedStuffs, setLocatedStuffs] = useState([]);
  const [unlocatedStuffs, setUnlocatedStuffs] = useState([]);
  const setPageLoading = useSetRecoilState(pageLoading);

  // 현재 편집중인 스터프.
  const [editing, setEditing] = useRecoilState(editingState);
  const [editStuff, setEditStuff] = useRecoilState(editStuffState);
  const [, setEditPosition] = useRecoilState(positionState);
  const [, setEditRotation] = useRecoilState(rotationState);
  const [, setEditCategory] = useRecoilState(categoryState);
  const [, setFrom] = useRecoilState(fromState);

  const [wallStuff, setWallStuff] = useState({});
  const [floorStuff, setFloorStuff] = useState({});

  // custom Alert
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');

  // stuff 가져오기.
  useEffect(() => {
    try {
      setPageLoading(true);
      axios
        .get(`/api/v1/userstuffs/list`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          setWallStuffs(
            res.data.data.filter(stuff => stuff.stuffType == 'WALL'),
          );
          setFloorStuffs(
            res.data.data.filter(stuff => stuff.stuffType == 'FLOOR'),
          );
          setStuffs(
            res.data.data.filter(
              stuff => stuff.stuffType != 'WALL' && stuff.stuffType != 'FLOOR',
            ),
          );
        });
    } catch (e) {
      console.log(e);
    }
    setTimeout(() => {
      setPageLoading(false);
    }, 1500);
  }, []);

  useEffect(() => {
    setWallStuff(wallStuffs.filter(stuff => stuff.selected)[0]);
  }, [wallStuffs]);
  useEffect(() => {
    setFloorStuff(floorStuffs.filter(stuff => stuff.selected)[0]);
  }, [floorStuffs]);

  useEffect(() => {
    setLocatedStuffs(stuffs.filter(stuff => stuff.selected));
    setUnlocatedStuffs(stuffs.filter(stuff => !stuff.selected));
  }, [stuffs]);

  useEffect(() => {
    if (!router.isReady) return;

    setUserName(router.query.userName);
  }, [router.isReady]);

  function StuffClick(e, stuff) {
    if (editing) return;

    setEditing(true);
    setFrom('located');

    setEditStuff(stuff);
    setEditPosition([stuff.posX, stuff.posY, stuff.posZ]);
    setEditRotation([stuff.rotX, stuff.rotY, stuff.rotZ]);
    setEditCategory(stuff.selectedCategory);

    let copylocateddata = locatedStuffs.filter(
      data => data.userStuffId != stuff.userStuffId,
    );
    setLocatedStuffs(copylocateddata);
  }

  function MapClick(stuff, tag) {
    stuff.posX = 0;
    stuff.posY = 0;
    stuff.posZ = 0;
    stuff.rotX = 0;
    stuff.rotY = 0;
    stuff.rotZ = 0;
    stuff.selectedCategory = 'NONE';
    if (tag == 1) {
      setFloorStuff(stuff);
    } else setWallStuff(stuff);
  }

  function EndEdit() {
    let senddata = [{ ...wallStuff }, { ...floorStuff }, ...locatedStuffs];
    setPageLoading(true);

    axios
      .put('/api/v1/userstuffs/edit', senddata, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      })
      .then(res => {
        editRoomRef.current.ScreenShot();
        setTimeout(function () {
          router.push(`/${userName}`);
        }, 2000);
      })
      .catch(e => {
        setPageLoading(false);
        setMessage(e.response.data.message);
        setOpen(true);
        // alert(e.response.data.message);
        console.log(e);
      });
  }

  function addLocateStuff(e, stuff) {
    setLocatedStuffs([...locatedStuffs, stuff]);
  }
  function addUnlocatedStuff(e, stuff) {
    setUnlocatedStuffs([...unlocatedStuffs, stuff]);
  }
  function deleteUnlocatedStuff(e, stuff) {
    let unlocateddata = unlocatedStuffs.filter(
      data => data.userStuffId != stuff.userStuffId,
    );
    setUnlocatedStuffs(unlocateddata);
  }

  return (
    <Container
      style={{
        margin: '30px auto',
      }}
    >
      <Grid
        container
        spacing={0}
        sx={{
          flexDirection: 'row-reverse',
          justifyContent: 'center',
        }}
      >
        <Grid item lg={5} sx={{ p: 1 }}>
          <Box
            sx={{
              p: 2,
              position: 'relative',
              backgroundColor: 'rgba(255,255,255,0.8)',
              borderRadius: 2,
            }}
          >
            <Typography variant="h6" sx={{ mb: 1 }}>
              {editing ? editStuff.alias : '편집할 에셋을 클릭하세요'}
            </Typography>
            <Divider />

            {editing ? (
              <EditConsole
                addLocateStuff={addLocateStuff}
                addUnlocatedStuff={addUnlocatedStuff}
                deleteUnlocatedStuff={deleteUnlocatedStuff}
              />
            ) : (
              <Box>
                <MyStuffList
                  MapClick={MapClick}
                  stuffs={unlocatedStuffs}
                  wallStuffs={wallStuffs}
                  floorStuffs={floorStuffs}
                />
                <Box
                  sx={{
                    position: 'absolute',
                    right: 15,
                    top: 15,
                  }}
                >
                  <Button color="error" variant="outlined" size="small">
                    <Link href={`/${userName}`}>취소</Link>
                  </Button>

                  <ColorButton
                    variant="contained"
                    onClick={EndEdit}
                    size="small"
                    sx={{ ml: 1, backgroundColor: '#fc7a71' }}
                  >
                    {/* 추후에 변경된 사항 저장할지 묻는 기능 필요. */}
                    편집 완료
                  </ColorButton>
                  <CustomAlert
                    open={open}
                    setOpen={setOpen}
                    severity="error"
                    message={message}
                  ></CustomAlert>
                </Box>
              </Box>
            )}
          </Box>
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
  );
}
