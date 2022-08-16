import { useEffect, useState } from 'react';
import Room from '../../components/Room';
import { Box } from '@mui/material';
import UserInfo from '../../components/RoomPage/UserInfo';
import { useRouter } from 'next/router';
import StuffPage from '../../components/RoomPage/StuffPage';
import styles from '../../styles/Room.module.css';
import { useSpring, animated } from 'react-spring';
import { followState, loginUserState } from '../../state/roomState';
import { useRecoilState } from 'recoil';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';

export default function RoomPage() {
  const [userName, setUserName] = useState();
  const [stuff, setStuff] = useState({});
  const [stuffon, setStuffon] = useState(false);

  const [loginUser, setLoginUser] = useRecoilState(loginUserState);
  const [nowFollow, setNowFollow] = useState();
  const [, setFollow] = useRecoilState(followState);

  const [userInfo, setUserInfo] = useState({categories : []});
  const router = useRouter();
  const [cookies] = useCookies(['cookie-name']);
  const [stuffPageLoc, setStuffPageLoc] = useState([0, 0]);

  const [hovered, setHovered] = useState(false); 
  function StuffClick(stuff) {
    console.log(stuff);

    setStuffon(stuff);
    setStuff(stuff);
  }

  useEffect(() => {
    if (!router.isReady) return;
    setUserName(router.query.userName);
  }, [router.isReady]);

  useEffect(() => {
    if(!userName) return;
    try {
      axios
        .get(`/api/v1/users/${userName}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          setLoginUser(res.data.data.loginUser);
          setFollow(res.data.data.follow);
          setUserInfo(res.data.data);
          setNowFollow(res.data.data.follower);
        });
    } catch (e) {
      console.log(e);
    }
  }, [userName])

  const userInfoAnimation = useSpring({
    from : { 
      position : 'absolute',
      left : '10%',
      top : 65,
      zIndex : '2',
      maxHeight : '170px' 
    },
    to : async (next, cancel) => {
      await next({
        maxHeight : hovered ? '550px' : '170px',
        background : hovered ? 'rgba(255,255,255,0.8)' : 'transparent',
      })
    }
  })

  const StuffAnimation = useSpring({
    from: { 
      position : 'absolute',
      left : 0,
      top : 0,
      zIndex : '3',
      opacity: '0.3', 
      maxHeight: '0px' 
    },
    to: async (next, cancel) => {
      await next({
        opacity: stuffon ? '1' : '0.3',
        maxHeight: stuffon ? '600px' : '0px',
        height: stuffon ? '600px' : '0px',
        left : stuffPageLoc[0],
        top : stuffPageLoc[1],
        position: 'absolute',
      });
    },
    config: { mass: 5, tension: 400, friction: 70, precision: 0.0001, duration: '500' },
  });

  function pagePush(page) { router.push(page); }

  return (
    <Box>
      <animated.div 
        onMouseEnter={() => setHovered(true)} 
        onMouseLeave={() => setHovered(false)}
        style={userInfoAnimation}
        className={styles.userPage}
      >
        <UserInfo 
          loginUser={loginUser} setNowFollow={setNowFollow}
          userName={userName} userInfo={userInfo} nowFollow={nowFollow}
          focused={hovered}
        />
      </animated.div>
      <animated.div className={styles.stuffPage} style={StuffAnimation}>
        {stuff ? <StuffPage data={stuff} /> : <></> }
      </animated.div>

      <Room
        StuffClick={StuffClick} 
        setClickLoc={setStuffPageLoc}
        userName={userName} 
        router={router} 
        pagePush={pagePush} 
        profileImage={userInfo.profileImage}
        setNowFollow={setNowFollow}
      />

    </Box>
  );
}
