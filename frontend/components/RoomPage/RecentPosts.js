import { Box, Divider, Typography } from '@mui/material';
import { useState } from 'react';
import Post from './atoms/Post';
import PostViewModal from '../PostPage/PostViewModal';

export default function RecentPost({ posts }) {
  // 여기에 post를 몇개 넘겨줄지 혹시 백에서 정했나요?
  const [post, setPost] = useState('');

  const openModal = post => {
    setPost(post);
    setShowModal(true);
  };
  const [showModal, setShowModal] = useState(false);

  console.log('post', posts);
  // post의 css를 모듈화해서, 여기랑 stuffpage에 각각 적용
  return (
    <Box
      sx={{ m : 1 }}
    >
      <PostViewModal
        post={post}
        showModal={showModal}
        setShowModal={setShowModal}
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
