import { Box, formControlClasses, } from '@mui/material';
import { useCallback, useEffect, useRef, useState } from 'react';
import useAuth from '../hooks/useAuth';
import LoginComponent from '../components/login';
import { useCookies } from 'react-cookie';
import axios from '../utils/axios';
import { useRouter } from 'next/router';
import { useInView } from 'react-intersection-observer';
import { Canvas, useFrame } from '@react-three/fiber';
import Particles from '../components/RoomPage/atoms/Particles';
import PostProcessing from '../components/RoomPage/atoms/PostProcessing';
import { Scroll, ScrollControls, Text, useScroll } from '@react-three/drei';
import { Color, MathUtils, TextureLoader } from 'three';
import TagButton from '../components/RoomPage/atoms/TagButton';

const damp = MathUtils.damp;

export default function Home() {
  const [roomImgs, setRoomImgs] = useState([]);
  const [windowSize, setWindowSize] = useState({width : 0, height : 0});
  const { auth } = useAuth();
  const [cookies] = useCookies(['cookie-name']);
  const router = useRouter();
  const [ref, inView] = useInView();

    // v1
  // const gap = 0.6
  // v2
  const w = 0.8
  const gap = 0.2
  const xW = w + gap
  const pixelConst = 130.5
  const distConst = 1.0;
  function Item({ src, index, position, scale, length, link, c = new Color() }) {
    const ref = useRef()
    let scroll = useScroll()
    const [hovered, hover] = useState(false)
    const [tag, setTag] = useState(false);
    const over = () => hover(true)
    const out = () => hover(false)

    const loader = new TextureLoader();
    // const texture =  loader.load('/test.png');
    const texture =  loader.load(src + "?not-from-cache-please");
    const dist = distConst * roomImgs.length;
    
    useEffect(() => {
      if (hovered) document.body.style.cursor = 'pointer'
      return () => (document.body.style.cursor = 'auto')
    }, [hovered])

    // v1
    // useFrame((state, delta) => {
    //   const y1 = scroll.curve(index/length - 1.5/length, 3 / length);
    //   const offset = position[0] - dist*scroll.offset;
    //   // let judgeX = (state.viewport.width/2 - Math.abs(offset)) * (offset < 0 ? -1 : 1);
    //   let judgeX = offset < 0 ? -1 : 1;

    //   let posXTo = judgeX;
    //   if(Math.abs(offset) < 0.5) {
    //     y1 *= Math.abs(posXTo)/2.4;
    //     posXTo = 0;
    //   }
    //   let posYTO = 1;
    //   if( state.viewport.width/2 < Math.abs(offset) ) posYTO = -1;

    //   const scaleTo = (hovered ? 1.5 : 1 ) + y1;
    //   ref.current.scale.x = damp(ref.current.scale.x, scaleTo, 6, delta)
    //   ref.current.scale.y = damp(ref.current.scale.y, scaleTo, 6, delta)
    
    //   ref.current.position.x = damp(ref.current.position.x, position[0] + posXTo/1.5, 6, delta);
    //   ref.current.position.y = damp(ref.current.position.y, position[1] + posYTO * Math.sqrt(Math.abs(judgeX))/5 - 0.5, 6, delta);
    //   ref.current.position.z = damp(ref.current.position.z, position[2] +  Math.abs(judgeX)/5 - 1, 6, delta);

    //   ref.current.rotation.y = damp(ref.current.rotation.y, posYTO * posXTo/12, 6, delta);
    //   ref.current.material.color.lerp(c.set(Math.abs(offset) < 0.5 ? 'white' : '#888'), hovered ? 0.3 : 0.1)
    // })

    //v2
    useFrame((state, delta) => {
      const y1 = scroll.curve(index/length - 1.5/length, 3 / length);
      let offset = position[0] - dist*scroll.offset;
      let posXTo = offset < 0 ? -1 : 1;
      offset = Math.abs(offset);
      let z = -0.02;
      let col = 888;

      if( offset < 0.4) {
        posXTo /= 100;
        y1 *= 2;
        z = 0;
        setTag(true);
      }
      else {
        setTag(false);
        if(offset < 1.5) {
          posXTo /= 1.2;
          z = -0.01
        }
      }
      const scaleTo = ((hovered ? 1.5 : 1 ) + y1*2)*(15-offset)/15 ;
      ref.current.scale.x = damp(ref.current.scale.x, scaleTo, 6, delta)
      ref.current.scale.y = damp(ref.current.scale.y, scaleTo, 6, delta)
    
      ref.current.position.x = damp(ref.current.position.x, position[0] + posXTo*(1.8 + 1.5*(15-offset)/15 - offset/10), 6, delta);
      ref.current.position.z = damp(ref.current.position.z, z, 6, delta);
      
      ref.current.material.color.lerp(c.set(offset < 0.5 ? 'white' : offset < 3 ? '#888' : '#666' ), hovered ? 0.3 : 0.1)
    })
    return (
      <group>
        <mesh ref={ref} castShadow receiveShadow
          onPointerOver={over} onPointerOut={out}
          position={position}
          onClick={() => router.push(`/` + link)}
        >
          <circleGeometry args={[0.5, 6, 0.525]}/>
          <meshBasicMaterial map={texture}/>
          {tag ?
            <TagButton
              pos={[0.3,0.36,-0.02]}
              rot={[0,0.68,-0.35]}
              fontSize={0.05}
              text={link}
            />
            : <></>
          }

        </mesh>
      </group>
    )
  }

  // 페이징 이벤트 처리.
  const [page, setPage] = useState(0);
  const [nextPage, setNextPage] = useState(false);

  function EventHandler() {
    const scroll = useScroll();

    useFrame(() => {
      if (scroll.offset > 0.99) {
        console.log(scroll);
        setNextPage(true);
      } 
    }, [])

    useEffect(() => {
      let loc = (roomImgs.length) / (roomImgs.length + 30);
      if (loc < 0) loc = 0;

      scroll.scroll.current = loc;
    }, [roomImgs])
    
    return(
      <group></group>
    )
  }

  useEffect(() => {
    if(!nextPage) return;
    setPage(page+1);
    setNextPage(false);
  }, [nextPage]);

  const handleResize = useCallback(() => {
    setWindowSize({
      width : window.innerWidth,
      height : window.innerHeight
    });
  }, []);

  useEffect(() => {
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => { window.removeEventListener('resize', handleResize); };
  }, [handleResize]);
  
  const init = useCallback(
    async token => {
      try {
        const { data } = await axios.get(
          `/api/v2/rooms/recommend?page=${page}&size=30`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              withCredentials: true,
            },
          },
        );
        // console.log(data);
        // console.log(data.data.roomResponseDtos);
        if(data.data.roomResponseDtos.content.length) {
          setRoomImgs( prev => [...prev, ...data.data.roomResponseDtos.content] );
        }
      } catch (e) {
        console.log(e);
      }
  },[page]);

  useEffect(() => {
    console.log(page)
    if (!auth.isAuthorized || !cookies.InfoMansionAccessToken) {
      return;
    }
    init(cookies.InfoMansionAccessToken);
  }, [page, init, auth.isAuthorized, cookies]);

  return (
    <>
      {auth.isAuthorized ? (
        <Box >
          <Canvas shadows
            style={{
              position : 'absolute',
              width : windowSize.width,
              height : 1000,
              bottom : windowSize.height > 1000 ? windowSize.height - 1000 : 0,
              zIndex : -1
            }}

          >
            <Particles size={5000} scale={0}/>
            <PostProcessing 
              luminanceSmoothing={30}
              intensity={100}
            />

            <ScrollControls horizontal damping={16} pages={(windowSize.width - xW*pixelConst + roomImgs.length * xW * pixelConst) / windowSize.width}>
              <Scroll>
                {roomImgs.map((v, i) => (
                  <Item key={i} 
                    src={v.roomImg} 
                    index={i} 
                    position={[i * xW, - windowSize.height/1000, 0]} 
                    scale={[w, 4, 1]}
                    length={roomImgs.length}
                    link={v.userName}
                  />
                ))}
                <EventHandler />
              </Scroll>
            </ScrollControls>
          </Canvas>
        </Box>
      ) : (
        <LoginComponent onSignIn={token => init(token)} />
      )}
    </>
  );
}
