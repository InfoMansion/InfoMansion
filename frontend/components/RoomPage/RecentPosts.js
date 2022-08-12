import { Box, Divider, Typography } from '@mui/material';
import { useState } from 'react';
import Post from './atoms/Post';
import PostViewModal from '../PostPage/PostViewModal';
import { useCookies } from 'react-cookie';
import useAuth from '../../hooks/useAuth';
import { useRecoilState } from 'recoil';
import { postDetailState } from '../../state/postDetailState';
import axios from '../../utils/axios';

export default function RecentPost({ posts }) {
  const [cookies] = useCookies(['cookie-name']);
  const [post, setPost] = useState('');
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [showModal, setShowModal] = useState(false);
  const { auth } = useAuth();

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
            if (postDetail.userName === auth.username) {
              setLoginUser(true);
            }
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

  // post의 css를 모듈화해서, 여기랑 stuffpage에 각각 적용
  return (
    <Box
      sx={{ m : 1 }}
    >
      <PostViewModal
          showModal={showModal}
          handleModalClose={handleModalClose}
      ></PostViewModal>

      <Typography variant="h6">
        Recent post
      </Typography>
      <Divider />

      <Box
        sx={{
          maxHeight : '160px'
        }}
        style={{
          overflowY : 'scroll'
        }}
      >
        {posts.map(post => (
          <Box>
            <Post
              post={post}
              totheight={100}
              picwidth={70}
              maxcontent={50}
              openModal={openModal}
            />
            <Divider sx={{m : 0}}/>
          </Box>
        ))}
      </Box>
    </Box>
  );
}
