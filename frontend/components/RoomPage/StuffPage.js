import React, { useEffect, useState } from "react";
import { Box, Card, CardActionArea, CardContent, CardMedia, CssBaseline, Divider, Paper, Slide, Toolbar, Typography, useScrollTrigger } from '@mui/material'
import { Container } from "@mui/system";
import Post from "./atoms/Post";
import postdata from '../jsonData/posts.json'

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
    const [stuff, setStuff] = useState(data);

    useEffect(() => {
        setStuff(data);
    }, [data])

    // 사용자 PK, stuffID 같이 넘겨주면 받을 수 있음.
    // 여기서 넘겨주고 받아와야 함.
    const [posts, setPosts] = useState(postdata);

    return (
        <Paper
            elevation={1}
            style={{
                backgroundColor : 'rgba(255,255,255,0.8)'
            }}
            sx={{
                width : '634px',
                borderRadius : 1,
                m : 1,  
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
                        minWidth : 60,
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
                <Post 
                    post={post} 
                    totheight={150} 
                    picwidth={150} 
                    maxcontent={150}
                />
            ))}
            </Container>
        </Paper>
    )
}