import React, { useState } from 'react';
import Box from '@mui/material/Box';
import { Divider, Typography } from '@mui/material';
import { Canvas } from '@react-three/fiber';
import ConfigStuff from './RoomPage/atoms/ConfigStuff'
import ConfigStuffs from './jsonData/ConfigStuffs.json'

function comp0() {
  return (
    <ConfigStuff
      data={ConfigStuffs[3]}
      pos={[0, 0, -2]} rot={[0, 0, 0]} iniscale={5}
      inicolor="#F3F676"
      speed={0.02}
    />
  )
}

function comp1() {
  return (
    <ConfigStuff
      data={ConfigStuffs[0]}
      pos={[0, 0, 0]} iniscale={50}
      color="#fa7070" inicolor="#fa7070"
      speed={0}
    />
  )
}

function comp2() {
  return (
    <ConfigStuff data={ConfigStuffs[5]}
      pos={[0, -2, 0]} iniscale={500}
      Click={() => {}}
      color="#fa7070" inicolor="#fa7070"
      speed={0.01}
    />
  )
}

function comp3() {
  return (
    <ConfigStuff
      data={ConfigStuffs[1]}
      pos={[0, 0, -2]} rot={[0, 0, 0]} iniscale={9}
      color="#FFF89D" inicolor="#9C9292"
      speed={0.02}
    />
  )
}

function comp4() {
  return (
    <ConfigStuff
      data={ConfigStuffs[3]}
      pos={[0, 0, -2]} rot={[0, 0, 0]} iniscale={5}
      inicolor="#F3F676"
      speed={0.02}
    />
  )
}

export default function Loading() {
  const [typos] = useState([
    ['더 멋진 방들을 보여드리기 위해,', '열심히 일하는 중입니다.'],
    ['그거 알고 계셨나요?', '하트 모양의 버튼을 클릭하여 다른 사람을 팔로우할 수 있습니다.'],
    ['귀여운 게시판 버튼을 누르면,', '방명록을 남길 수 있습니다.'],
    ['방을 꾸미고 싶을 때는 스패너 버튼을 눌러보세요'],
    ['이 메세지가 뜰 확률은 1억분의 1보다 낮습니다.', '운이 좋으시네요!'],
  ])
  const component = (type) => {
    const comp = (
      type == 0 ? comp0() : 
      type == 1 ? comp1() : 
      type == 2 ? comp2() : 
      type == 3 ? comp3() : 
      comp4
    );
    return comp;
  }

  const [type] = useState(parseInt(Math.random()*4));
  const [comp] = useState(component(type));


  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection : 'column',
        position: 'absolute',
        width: '100%',
        height: '100%',
        top: '0',
        left: '0',
        background: 'rgba(255, 255, 255, 0.9)',
        justifyContent: 'center',
        alignItems: 'center',
        zIndex: '3',
        minHeight: '100vh',
      }}
    >
      <Canvas
        style={{
          width : 200,
          height : 200,
        }}
      >
        {comp}
        <pointLight position={[-10,10,10]} />
        <ambientLight intensity={0.1}/>
      </Canvas>
      
      {typos[type].map( text => (
        <Typography variant="h6">
          {text}
        </Typography>
      ))}
      {/* <CircularProgress sx={{ color: MAIN_COLOR }} size="5rem" /> */}
    </Box>
  );
}
