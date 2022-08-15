import { Canvas } from '@react-three/fiber';
import { useEffect, useState } from 'react';
import { Box, Button, Container } from '@mui/material';
import RoomManageMenu from './RoomPage/RoomManageMenu';
import MapStuffs from './RoomPage/MapStuffs';
import Stuffs from './RoomPage/Stuffs';

import RoomCamera from './RoomPage/atoms/RoomCamera';
import RoomLight from './RoomPage/atoms/RoomLight';
import PostProcessing from './RoomPage/atoms/PostProcessing'
import axios from '../utils/axios';
import { useCookies } from 'react-cookie';

import { clickedStuffCategoryState, followState, loginUserState } from '../state/roomState';
import { useRecoilState, useSetRecoilState } from 'recoil';
import { pageLoading } from '../state/pageLoading';
import ConfigItems from './RoomPage/ConfigItems';
import Particles from './RoomPage/atoms/Particles'

import ConfigStuffs from './jsonData/ConfigStuffs.json'
import ConfigStuff from './RoomPage/atoms/ConfigStuff' 
import PostItems from './RoomPage/PostItems';
import GuestBookPage from './RoomPage/atoms/GuestBookPage'
import TagButton from './RoomPage/atoms/TagButton';

export default function Room({ StuffClick, setClickLoc, userName, pagePush, profileImage, setNowFollow }) {
  const [cookies] = useCookies(['cookie-name']);
  const [zoomscale] = useState(100);

  const [mapstuffs, setMapstuffs] = useState([]);
  const [stuffs, setStuffs] = useState([]);
  const [hovered, setHovered] = useState(0);
  const [clicked, setClicked] = useState(0);

  const [tagon, setTagon] = useState(false);
  const [camloc] = useState([0, 0, 0]);
  const [, setClickedStuffCategory] = useRecoilState(clickedStuffCategoryState);
  const [loginUser] = useRecoilState(loginUserState);
  const setPageLoading = useSetRecoilState(pageLoading);
  const [isFollow, setIsFollow] = useRecoilState(followState);
  const [guestBookOpen, setGuestBookOpen] = useState(false);

  // 마운트시 stuff 로드
  useEffect(() => {
    // stuff 가져오기
    if(!userName) return;
    try {
      setPageLoading(true);
      axios
        .get(`/api/v1/userstuffs/room/${userName}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {

          setMapstuffs(res.data.data.slice(0, 2));
          setStuffs(res.data.data.slice(2));
          setPageLoading(false);
        });
    } catch (e) {
      setPageLoading(false);
      console.log(e);
    }
  }, [userName]);

  // stuff 호버 이벤트.
  function Hover(e, stuff) {
    setHovered();
  }
  // stuff 클릭 이벤트.
  function Click(e, stuff) {
    if (stuff.category == 'NONE') return null;
    let midx = Number(window.innerWidth/2);
    let maxy = window.innerHeight - 620;
    
    let x = e.clientX < midx ? e.clientX : e.clientX - 400;
    let y = e.clientY > maxy ? maxy : e.clientY;

    setClickLoc([x, y])
    setClicked(clicked.id == stuff.id ? 0 : stuff);
    setClickedStuffCategory(stuff.category);
  }

  useEffect(() => {
    StuffClick(clicked);
  }, [clicked])
  
  function popupPosts(e, type) {
      if(type == 'postBox') {
        Click(e, {
          "id" : 999,
          "category" : "POSTBOX",
          "alias" : "보관함",
        })
      }
      else if(type =='guestBook') {
        setGuestBookOpen(true);
      }
  }

  const postFollow = async () => {
    if(loginUser) return;
    
    try {
      await axios.post(
        `/api/v1/follow/${userName}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        },
      );
      setIsFollow(prev => !prev);
      setNowFollow(prev => prev + 1);
      alert('팔로우');
    } catch (e) {
      console.log('error', e);
    }
  };

  const postUnFollow = async () => {
    try {
      await axios.delete(`/api/v1/follow/${userName}`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      });
      setIsFollow(prev => !prev);
      setNowFollow(prev => prev - 1);
      alert('팔로우 취소');
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <div
      style={{
        height: '900px',
        position: 'relative',
      }}
    >
      {/* 캔버스 영역 */}
      <Canvas shadows>
        <RoomLight />
        <RoomCamera camloc={camloc} clicked={clicked} zoomscale={zoomscale} />
        <PostProcessing />

        {/* 벽, 바닥 */}
        <MapStuffs stuffs={mapstuffs} Hover={Hover} Click={Click} />
        {/* stuffs */}
        <Stuffs
          stuffs={stuffs}
          Hover={Hover}
          Click={Click}
          status={'view'}
          tagon={tagon}
        />

        {/* 팔로우버튼 */}
        <ConfigStuff data={ConfigStuffs[0]} pos={[2, 3.5, -2]} iniscale={12} 
          Click={() => (isFollow ? postUnFollow() : postFollow())}
          color="#fa7070" inicolor="#FFA78C"
          isFollow={isFollow}
          speed={0.002}
        />

        {/* userName 표시 */}
        <TagButton 
          pos={[3, 4.4, 0]}
          rot={[0,0,0]}
          fontSize={0.3}
          text={userName}
        />

        {/* tagOnoff 표시 */}
        <TagButton 
          pos={[0, 4.3, 3.55]}
          rot={[0,1.58,0]}
          fontSize={0.2}
          click={() => setTagon(!tagon)}
          text={tagon ? 'Hide Tag' : 'Show Tag'}
        />

        <Particles />
        {/* <Stuff_s3test /> */}
      </Canvas>

      <Container
        style={{position : 'relative'}}
      >
        {
          loginUser ?
          <div>
            <Canvas
              style={{
                position : 'absolute',
                right : '3%',
                bottom : 50,
                width : '250px',
                height : '250px'
              }}
            >
              {/* 팔로우 버튼 */}
              <ConfigItems
                  pagePush={pagePush}
                  userName={userName}
                  profileImage={profileImage}
                />
            </Canvas>
          </div>
          : <></>
        }
        <Canvas
          style={{
            position : 'absolute',
            left : '3%',
            bottom : 50,
            width : '250px',
            height : '250px'
          }}
        >
          <PostItems
            popupPosts={popupPosts}
            userName={userName}
            profileImage={profileImage}
          />
        </Canvas>
      </Container>

      <GuestBookPage 
        open={guestBookOpen}
        setGuestBookOpen={setGuestBookOpen}
        userName={userName}
        cookies={cookies}
      />
    </div>
  );
}
