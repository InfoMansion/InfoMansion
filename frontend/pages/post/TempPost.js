import React, { useState, useEffect, useCallback } from 'react';
import { Box, Button, Container, Divider, Typography } from '@mui/material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Temp from '../../components/RoomPage/atoms/Temp';
import TempPostViewModal from '../../components/PostPage/TempPostViewModal';
import { useCookies } from 'react-cookie';
import axios from '../../utils/axios';
import { postDetailState } from '../../state/postDetailState';
import { loginUserState } from '../../state/roomState';
import { useRecoilState } from 'recoil';
import useAuth from '../../hooks/useAuth';
const theme = createTheme({
  palette: {
    primary: {
      light: '#ffffff',
      main: '#fc7a71',
      dark: '#ba000d',
      contrastText: '#000',
    },
  },
});

const defaultPosts = [];

export default function tempPost() {
  const [posts, setPosts] = useState(defaultPosts);
  const [showModal, setShowModal] = useState(false);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [post, setPost] = useState('');
  const { auth } = useAuth();
  const [cookies] = useCookies(['cookie-name']);
  const [loginUser, setLoginUser] = useRecoilState(loginUserState);
  const [isDeleted, setIsDeleted] = useState(false);

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
          const tempPostDetail = res.data.data;
          tempPostDetail.modifiedDate =
            tempPostDetail.modifiedDate.substring(0, 10) +
            ' ' +
            tempPostDetail.modifiedDate.substring(11, 16);
          setPostDetail(tempPostDetail);
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
      // console.log(data);
      setPosts(data.data);
    } catch (e) {
      console.log(e);
    }
  }, []);

  useEffect(() => {
    getResult();
  }, [isDeleted]);

  const handleModalClose = () => {
    setShowModal(false);
    setPostDetail('');
  };

  return (
    <ThemeProvider theme={theme}>
      <Container>
        <Box
          sx={{ width: '100%' }}
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Divider
            style={{
              bold: true,
              color: 'white',
              fontSize: 'x-large',
              width: '100%',
            }}
            sx={{
              '&::before, &::after': {
                borderColor: 'primary.light',
              },
            }}
          >
            나의 임시저장 글
          </Divider>
          <TempPostViewModal
            showModal={showModal}
            handleModalClose={handleModalClose}
            isDeleted={isDeleted}
            setIsDeleted={setIsDeleted}
          ></TempPostViewModal>
          <Box
            style={{
              maxWidth: 900,
              width: '100%',
            }}
          >
            {posts.map((post, index) => (
              <Temp
                post={post}
                totheight={150}
                maxcontent={150}
                openModal={openModal}
                isDeleted={isDeleted}
                setIsDeleted={setIsDeleted}
                handleModalClose={handleModalClose}
              />
            ))}
          </Box>
          {/* <Button
        onClick={() => {
          getResult(category, posts[category].page + 1);
        }}
        >
        more
      </Button> */}
        </Box>
      </Container>
    </ThemeProvider>
  );
}
