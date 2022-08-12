import { useEffect, useState } from 'react';
import Room from '../../components/Room';
import { Box, Container } from '@mui/material';
import UserInfo from '../../components/RoomPage/UserInfo';
import { useRouter } from 'next/router';
import StuffPage from '../../components/RoomPage/StuffPage';
import styles from '../../styles/Room.module.css';
import { useSpring, animated } from 'react-spring';
import { followState, loginUserState } from '../../state/roomState';
import { useRecoilState } from 'recoil';
import axiosApiInstance from '../../utils/axios';
import { useCookies } from 'react-cookie';

export default function RoomPage() {
  const [userName, setUserName] = useState();
  const [stuff, setStuff] = useState({});
  const [stuffon, setStuffon] = useState(false);
  const [useron, setUseron] = useState(true);

  const [loginUser, setLoginUser] = useRecoilState(loginUserState);
  const [nowFollow, setNowFollow] = useState();
  const [, setFollow] = useRecoilState(followState);

  const [userInfo, setUserInfo] = useState({categories : []});
  const router = useRouter();
  const [cookies] = useCookies(['cookie-name']);

  const [hovered, setHovered] = useState(false); 
  function StuffClick(stuff) {
    // 여기서 stuffpage로 변수 전달하면 됨.
    setStuffon(!stuffon);
    setStuff(stuff);
  }

  function handleResize() {
    if (window.innerWidth >= 1200) setUseron(true);
    else setUseron(false);
  }
  useEffect(() => {
    window.addEventListener('resize', handleResize);
  });

  useEffect(() => {
    if (!router.isReady) return;
    setUserName(router.query.userName);
  }, [router.isReady]);

  useEffect(() => {
    try {
      axiosApiInstance
        .get(`/api/v1/users/${router.query.userName}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          console.log('user', res.data);

          setLoginUser(res.data.data.loginUser);
          setFollow(res.data.follow);
          setUserInfo(res.data.data);
          setNowFollow(res.data.data.follower);
        });

    } catch (e) {
      console.log(e);
    }
  }, [userName])

  useEffect(() => {
    console.log(userInfo);
  }, [userInfo])

  function followToggle() {
    console.log("클릭")
  }
  
  const userInfoAnimation = useSpring({
    from : { 
      position : 'absolute',
      left : '15%',
      zIndex : '2',
      maxHeight : '150px' 
    },
    to : async (next, cancel) => {
      await next({
        maxHeight : hovered ? '550px' : '150px',
        background : hovered ? 'rgba(255,255,255,0.8)' : 'transparent',
      })
    }
  })

  const StuffAnimation = useSpring({
    from: { opacity: '0.3', maxHeight: '0px' },
    to: async (next, cancel) => {
      await next({
        opacity: stuffon ? '1' : '0.3',
        maxHeight: stuffon ? '600px' : '0px',
        height: stuffon ? '600px' : '0px',
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

      <Room
        StuffClick={StuffClick} 
        userName={userName} 
        router={router} 
        pagePush={pagePush} 
        profileImage={userInfo.profileImage}
        setNowFollow={setNowFollow}
      />

      <Box
        sx={{
          zIndex: 'tooltip',
          width: 650,
        }}
      >
        {/* 페이지 */}
        <animated.div className={styles.stuffPage} style={StuffAnimation}>
          <StuffPage data={stuff} />
        </animated.div>
      </Box>
    </Box>
  );
}
