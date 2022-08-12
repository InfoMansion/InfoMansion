import React, { useEffect, useState } from "react";
import { CssBaseline, Divider, Paper, Slide, Toolbar, Typography, useScrollTrigger } from '@mui/material'
import { Container } from "@mui/system";
import Post from "./atoms/Post";

import {useRecoilState} from 'recoil';
import {clickedStuffCategoryState} from '../../state/roomState'
import axios from "../../utils/axios";
import { useCookies } from "react-cookie";

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

export default function StuffPage( {data} ) {
    const [cookies] = useCookies(['cookie-name']);
    const [clickedStuffCategory] = useRecoilState(clickedStuffCategoryState);
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        if(!data.id) return;
        console.log(data.id);
        try {
            axios.get(`/api/v1/posts/${data.id}`, {
                headers: {
                    Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                },
            })
            .then( res => {
                console.log(res.data.data);
                setPosts(res.data.data);
            })
        }catch(e) {
            console.log(e);
        }
    }, [data])

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
                    {data.alias}
                </Typography>

                <Typography 
                    sx={{
                        borderRadius : 3,
                        backgroundColor : '#ffa0a0',
                        minWidth : 60,
                        color : '#ffffff'
                    }}
                >
                    {clickedStuffCategory.category}
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