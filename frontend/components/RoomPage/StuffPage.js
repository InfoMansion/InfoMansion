import React, { useEffect, useState } from 'react';
import {
  Box,
  CssBaseline,
  Divider,
  IconButton,
  Paper,
  Slide,
  TextField,
  Toolbar,
  Typography,
  useScrollTrigger,
} from '@mui/material';
import { Container } from '@mui/system';
import Post from './atoms/Post';

import { useRecoilState } from 'recoil';
import {
  clickedStuffCategoryState,
  loginUserState,
} from '../../state/roomState';
import CreateIcon from '@mui/icons-material/Create';
import axios from '../../utils/axios';
import PostViewModal from '../PostPage/PostViewModal';
import { postDetailState } from '../../state/postDetailState';
import useAuth from '../../hooks/useAuth';

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

export default function StuffPage({ data, cookies }) {
  const [clickedStuffCategory] = useRecoilState(clickedStuffCategoryState);
  const [posts, setPosts] = useState([]);

  const [post, setPost] = useState('');
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [showModal, setShowModal] = useState(false);
  const [isDeleted, setIsDeleted] = useState(false);

  const { auth } = useAuth();
  const [userName] = useState(auth.username);
  const [loginUser] = useRecoilState(loginUserState);

  const [editAlias, setEditAlias] = useState(false);
  const [alias, setAlias] = useState(data.alias);

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
    if (!data.id) return;
    setAlias(data.alias);

    let url = `/api/v1/posts/${data.id}`;
    if (data.category == 'POSTBOX')
      url = `/api/v1/posts/postbox/${userName}?page=1&size=3`;

    try {
      axios
        .get(url, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          if (data.category == 'POSTBOX') {
            setPosts(res.data.data.content);
          } else setPosts(res.data.data.postsByUserStuff.content);
        });
    } catch (e) {
      console.log(e);
    }
  }, [data, isDeleted]);

  function changeAlias() {
    if (!editAlias) {
      setEditAlias(true);
    } else {
      // 요청 보내기.
      console.log(data.category.category);

      axios
        .put(
          `/api/v1/userstuffs/option`,
          {
            id: data.id,
            alias: alias,
          },
          {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            },
          },
        )
        .then(res => {
          console.log(res);
        })
        .catch(e => {
          console.log(e);
        });

      setEditAlias(false);
    }
  }
  function handleChange(e) {
    setAlias(e.target.value);
  }

  return (
    <Paper
      elevation={1}
      style={{
        backgroundColor: 'rgba(255,255,255,0.8)',
      }}
      sx={{
        width: '100%',
        borderRadius: 1,
        m: 1,
      }}
    >
      <PostViewModal
        showModal={showModal}
        handleModalClose={handleModalClose}
        isDeleted={isDeleted}
        setIsDeleted={setIsDeleted}
      ></PostViewModal>

      <CssBaseline />

      {/* 타이틀 영역 */}
      <Toolbar
        sx={{
          display: 'flex',
          textAlign: 'center',
          backgroundColor: '#ffffff',
        }}
      >
        {editAlias ? (
          <TextField
            id="standard-error-helper-text"
            label="별칭 지정"
            defaultValue={alias}
            onChange={e => handleChange(e)}
            variant="standard"
          />
        ) : (
          <Typography variant="h4" sx={{}}>
            {alias}
          </Typography>
        )}
        {loginUser ? (
          <IconButton onClick={changeAlias}>
            <CreateIcon />
          </IconButton>
        ) : (
          <></>
        )}

        <Typography
          sx={{
            borderRadius: 3,
            backgroundColor: '#ffa0a0',
            minWidth: 60,
            color: '#ffffff',
            px: 1,
          }}
        >
          {clickedStuffCategory.category}
        </Typography>
      </Toolbar>

      <Divider />
      <Container
        sx={{
          height: '500px',
          p: 1,
        }}
        style={{
          overflowY: 'scroll',
        }}
      >
        {/* 포스트 영역 */}
        {posts.map(post => (
          <Box>
            <Post
              post={post}
              totheight={120}
              picwidth={100}
              maxcontent={150}
              openModal={openModal}
            />
            <Divider />
          </Box>
        ))}
      </Container>
    </Paper>
  );
}
