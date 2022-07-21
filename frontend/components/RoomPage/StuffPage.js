import React, { useEffect, useState } from "react";
import { AppBar, Box, Card, CardActionArea, CardContent, CardMedia, CssBaseline, Divider, Paper, Slide, Toolbar, Typography, useScrollTrigger } from '@mui/material'
import { Container, textAlign } from "@mui/system";
import { useFrame } from "@react-three/fiber";

function ElevationScroll(props) {
    const { children, window } = props;
    const trigger = useScrollTrigger({
      disableHysteresis: true,
      threshold: 0,
      target: window ? window() : undefined,
    });
  
    return React.cloneElement(children, {
      elevation: trigger ? 4 : 0,
    });
}

export default function StuffPage( {data, ...props} ) {
    // 실제로 사용할 부분.

    // 더미
    const [maxcontent] = useState(200);
    const [stuff, setStuff] = useState(data);

    useEffect(() => {
        setStuff(data);
    }, [data])

    // 사용자 PK, stuffID 같이 넘겨주면 받을 수 있음.
    // 여기서 넘겨주고 받아와야 함.
    const [posts, setPosts] = useState([
        { 
            name : '간식을 아침에 줬으면 좋겠다',
            content : `
            관현악이며, 꽃이 별과 갑 인간의 있는 두손을 이것이다. 불어 어디 영원히 가는 없는 군영과 것은 미인을 쓸쓸하랴? 
            피가 놀이 따뜻한 이 인생을 얼마나 산야에 소담스러운 이상은 듣는다. 맺어, 이 뜨거운지라, 뜨고, 가진 칼이다. 인생의 부패를 방지하는 청춘이 피고 그들은 유소년에게서 청춘의 피다. 트고, 위하여, 낙원을 봄날의 방황하였으며, 갑 있다.
            때에, 따뜻한 위하여, 약동하다. 얼음 몸이 위하여 끓는다. 청춘의 얼마나 설레는 맺어, 크고 사막이다.
            `,
            image : 'pic1.jpg'
        },
        {
            name : '오늘 점심은 머먹지',
            content : `
            이상은 하는 얼마나 귀는 끝까지 그들의 교향악이다. 청춘을 인생의 같이, 할지니, 끓는다.
            일월과 행복스럽고 인간의 보는 보배를 우리 교향악이다. 바로 인간의 같으며, 때문이다. 풀이 커다란 구하기 싶이 심장의 아니다.
            튼튼하며, 얼마나 고행을 현저하게 있는가? 평화스러운 그들에게 위하여 찾아 노년에게서 것은 열매를 봄바람이다. 있는 속잎나고, 설레는 천고에 하는 되려니와, 싶이 많이 부패뿐이다. 내려온 우는 자신과 뭇 주며, 몸이 설레는 것이다.
            `,
            image : 'pic2.jpg'
        },
        {
            name : '이 글은 또 뭘 쓰지...',
            content : `
            같지 역사를 없으면 황금시대다. 가슴이 못할 풍부하게 그와 이것이다.
            오직 그들은 거친 청춘 풍부하게 있음으로써 이상의 위하여 보는 있으랴? 우리의 듣기만 대한 설산에서 것은 용기가 쓸쓸하랴? 노년에게서 없으면, 피고 힘있다. 갑 밝은 보이는 때에, 그들은 튼튼하며, 것이 방황하였으며, 보라.
            고동을 있는 안고, 커다란 목숨이 과실이 무엇을 이것이다. 그들은 찬미를 위하여 뼈 새 봄바람이다. 그들은 찾아다녀도, 없으면 이것이다. 끓는 트고, 인류의 아니다.
            `,
            image : 'pic3.png'
        },
        { 
            name : '간식을 아침에 줬으면 좋겠다w',
            content : `
            관현악이며, 꽃이 별과 갑 인간의 있는 두손을 이것이다. 불어 어디 영원히 가는 없는 군영과 것은 미인을 쓸쓸하랴? 
            피가 놀이 따뜻한 이 인생을 얼마나 산야에 소담스러운 이상은 듣는다. 맺어, 이 뜨거운지라, 뜨고, 가진 칼이다. 인생의 부패를 방지하는 청춘이 피고 그들은 유소년에게서 청춘의 피다. 트고, 위하여, 낙원을 봄날의 방황하였으며, 갑 있다.
            때에, 따뜻한 위하여, 약동하다. 얼음 몸이 위하여 끓는다. 청춘의 얼마나 설레는 맺어, 크고 사막이다.
            `,
            image : 'pic1.jpg'
        },
        {
            name : '오늘 점심은 머먹지w',
            content : `
            이상은 하는 얼마나 귀는 끝까지 그들의 교향악이다. 청춘을 인생의 같이, 할지니, 끓는다.
            일월과 행복스럽고 인간의 보는 보배를 우리 교향악이다. 바로 인간의 같으며, 때문이다. 풀이 커다란 구하기 싶이 심장의 아니다.
            튼튼하며, 얼마나 고행을 현저하게 있는가? 평화스러운 그들에게 위하여 찾아 노년에게서 것은 열매를 봄바람이다. 있는 속잎나고, 설레는 천고에 하는 되려니와, 싶이 많이 부패뿐이다. 내려온 우는 자신과 뭇 주며, 몸이 설레는 것이다.
            `,
            image : 'pic2.jpg'
        },
        {
            name : '이 글은 또 뭘 쓰지..w.',
            content : `
            같지 역사를 없으면 황금시대다. 가슴이 못할 풍부하게 그와 이것이다.
            오직 그들은 거친 청춘 풍부하게 있음으로써 이상의 위하여 보는 있으랴? 우리의 듣기만 대한 설산에서 것은 용기가 쓸쓸하랴? 노년에게서 없으면, 피고 힘있다. 갑 밝은 보이는 때에, 그들은 튼튼하며, 것이 방황하였으며, 보라.
            고동을 있는 안고, 커다란 목숨이 과실이 무엇을 이것이다. 그들은 찬미를 위하여 뼈 새 봄바람이다. 그들은 찾아다녀도, 없으면 이것이다. 끓는 트고, 인류의 아니다.
            `,
            image : 'pic3.png'
        },
    ])

    return (
        <Paper
            elevation={1}
            style={{
                backgroundColor : 'rgba(255,255,255,0.8)'
            }}
            sx={{
                width : '650px',
                borderRadius : 1,
                m : 1
            }}
        >
            <CssBaseline />
            {/* 타이틀 영역 */}
            <Toolbar
                sx={{
                    display : 'flex',
                    textAlign : 'center',
                    backgroundColor : '#ffffff',
                }}
                >
                <Typography
                    variant='h4'
                    sx={{
                        mr : 2
                    }}
                >
                    {stuff.stuff_alias}
                </Typography>

                <Typography 
                    sx={{
                        borderRadius : 3,
                        backgroundColor : '#ffa0a0',
                        minWidth : 50,
                        color : '#ffffff'
                    }}
                >
                    {stuff.category}
                </Typography>
            </Toolbar>

            <Divider />
            <Container
                sx={{
                    maxHeight : '500px',
                    p : 2,
                    mb : 2
                }}
                style={{
                    overflowY : 'scroll'
                }}
            >
            
            {/* 포스트 영역 */}
            {posts.map( post => (
                
                <Card 
                    key={post.name}
                    sx={{
                        maxHeight : '150px',
                        my : 1,
                        backgroundColor : 'rgba(255,255,255,0.5)'
                    }}
                >
                    <CardActionArea
                        sx={{
                            display : 'flex',
                            alignItems : 'flex-start'
                        }}
                        onClick={() => console.log(post.name)}
                    >
                        <CardMedia
                            component="img"
                            sx={{
                                    width : 150,
                                }}
                                image={`/image/${post.image}`}
                                alt='no img'
                        />
                            
                            <CardContent
                                sx={{
                                    display : 'flex',
                                    flexDirection : 'column', 
                                }}
                            >
                                <Typography
                                    variant="h6"
                                    color="text.primary"
                                >
                                    {post.name}
                                </Typography>
                            
                                <Divider sx={{m : 1}}/>
                            
                                <Typography
                                    variant="body2"
                                    color='text.secondary'
                                    
                                >
                                    { 
                                        ((post.content).length > maxcontent) ?
                                        
                                        (((post.content).substring(0, maxcontent-3)) + '...') 
                                        : mytextvar 
                                    }
                                </Typography>
                            </CardContent>
                        </CardActionArea>
                    </Card>
                ))}
            </Container>
        </Paper>
    )
}