import React, { useEffect, useState } from "react";
import { Box, CssBaseline, Divider, Paper, Slide, Toolbar, Typography, useScrollTrigger } from '@mui/material'
import { Container } from "@mui/system";
import Post from "./atoms/Post";

import {useRecoilState} from 'recoil';
import {clickedStuffCategoryState} from '../../state/roomState'
import axios from "../../utils/axios";
import { useCookies } from "react-cookie";
import PostViewModal from "../PostPage/PostViewModal";
import { postDetailState } from "../../state/postDetailState";
import useAuth from "../../hooks/useAuth";

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
    
    const [post, setPost] = useState('');
    const [postDetail, setPostDetail] = useRecoilState(postDetailState);
    const [showModal, setShowModal] = useState(false);

    const {auth} = useAuth();
    const [userName] = useState(auth.username);

    const openModal = post => {
        setPost(post);
        try {
          axios
            .get(`/api/v1/posts/detail/${post.id}`, {
              headers: {
                Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              },
            })
            .then(res => {
              console.log(res.data);
              setPostDetail(res.data.data);
            })
            .then(setShowModal(true));
        } catch (e) {
          console.log(e);
        }
    };
    const handleModalClose = () => {
        setShowModal(false);
        setPostDetail('');
    };

    useEffect(() => {
        if(!data.id) return;

        let url = `/api/v1/posts/${data.id}`;
        if(data.category == 'POSTBOX')
            url = `/api/v1/posts/postbox/${userName}?page=1&size=3`

        try {
            axios.get( url , {
                headers: {
                    Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                },
            })
            .then( res => {
                if(data.category == "POSTBOX") {
                    setPosts(res.data.data.content);
                }
                else
                    setPosts(res.data.data.postsByUserStuff.content);
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
                width : '100%',
                borderRadius : 1,
                m : 1,  
            }}
        >
            <PostViewModal
                showModal={showModal}
                handleModalClose={handleModalClose}
            ></PostViewModal>

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
                    height : '500px',
                    p : 1,
                }}
                style={{
                    overflowY : 'scroll'
                }}
            >
            
            {/* 포스트 영역 */}
            {posts.map( post => (
                <Box>
                    <Post 
                        post={post} 
                        totheight={150}
                        picwidth={150} 
                        maxcontent={150}
                        openModal={openModal}
                    />
                    <Divider />
                </Box>
            ))}
            </Container>
        </Paper>
    )
}