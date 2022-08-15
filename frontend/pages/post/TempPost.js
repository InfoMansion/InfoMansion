import React, { useState, useEffect, useCallback } from 'react';
import { Box, Button, Container } from '@mui/material';
import Temp from '../../components/RoomPage/atoms/Temp';
import TempPostViewModal from '../../components/PostPage/TempPostViewModal';
import { useCookies } from 'react-cookie';
import axios from '../../utils/axios';
import { postDetailState } from '../../state/postDetailState';
import { loginUserState } from '../../state/roomState';
import { useRecoilState } from 'recoil';
import useAuth from '../../hooks/useAuth';

const defaultPosts = [];

export default function tempPost() {
  const [posts, setPosts] = useState(defaultPosts);
  const [showModal, setShowModal] = useState(false);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [post, setPost] = useState('');
  const { auth } = useAuth();
  const [cookies] = useCookies(['cookie-name']);
  const [loginUser, setLoginUser] = useRecoilState(loginUserState);

  const styles = theme => ({
    indicator: {
      backgroundColor: 'white',
    },
  });

  const openModal = post => {
    setPost(post);
    try {
      axios
        .get(`/api/v1/posts/detail/${post.postId}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          setPostDetail(res.data.data);
          if (postDetail.userName === auth.username) {
            setLoginUser(true);
          }
        })
        .then(setShowModal(true));
    } catch (e) {
      console.log(e);
    }
  };

  const getResult = useCallback(async () => {
    try {
      const { data } = await axios.get(`/api/v2/posts/temp`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      console.log(data);
      setPosts(data.data);
    } catch (e) {
      console.log(e);
    }
  }, []);

  useEffect(() => {
    getResult();
  }, []);

  const handleModalClose = () => {
    setShowModal(false);
    setPostDetail('');
  };

  return (
    <Container>
      <Box sx={{ width: '100%' }}>
        <TempPostViewModal
          showModal={showModal}
          handleModalClose={handleModalClose}
        ></TempPostViewModal>
        {posts.map((post, index) => (
          <Temp
            post={post}
            totheight={150}
            picwidth={150}
            maxcontent={150}
            openModal={openModal}
          />
        ))}
        {/* <Button
        onClick={() => {
          getResult(category, posts[category].page + 1);
        }}
        >
        more
      </Button> */}
      </Box>
    </Container>
  );
}
