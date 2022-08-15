import { Box, Button, Container } from '@mui/material';
import { useState, useEffect } from 'react';
import PostEditor from '../../components/PostPage/PostEditor';
import { MAIN_COLOR } from '../../constants';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { postDetailState } from '../../state/postDetailState';
import { useRecoilState } from 'recoil';
import { useRouter } from 'next/router';

export default function createPost() {
  //추후에 state관리 여기서 할 예정
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [category, setCategory] = useState('');
  const [stuffId, setStuffId] = useState('');
  const [cookies] = useCookies(['cookie-name']);
  const [imageUrlList, setImageUrlList] = useState([]);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [tempId, setTempId] = useState('');
  const router = useRouter();
  const postFinish = !!content && !!stuffId && !!title;
  const tempFinish = !!content && !!title;

  const handleSave = async () => {
    if (!tempId) {
      try {
        const userPost = {
          userStuffId: stuffId,
          title: title,
          content: content,
          // images: imageUrlList,
        };
        const { data } = await axios.post('/api/v2/posts', userPost, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        });
        router.back();
      } catch (e) {
        console.log(e);
      }
    } else {
      try {
        const userPost = {
          userStuffId: stuffId,
          title: title,
          content: content,
          // images: imageUrlList,
        };
        const { data } = await axios.post(`/api/v2/posts/${tempId}`, userPost, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        });
        router.back();
      } catch (e) {
        console.log(e);
      }
    }
  };

  const handleTempSave = async () => {
    if (!tempId) {
      try {
        const userPost = {
          title: title,
          content: content,
        };
        const { data } = await axios.post('/api/v2/posts/temp', userPost, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        });
        alert('임시저장 되었습니다.');
        router.back();
      } catch (e) {
        console.log(e);
      }
    } else {
      try {
        const userPost = {
          title: title,
          content: content,
        };
        const { data } = await axios.post(
          `/api/v2/posts/temp/${tempId}`,
          userPost,
          {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              withCredentials: true,
            },
          },
        );
        alert('임시저장 되었습니다.');
        router.back();
      } catch (e) {
        console.log(e);
      }
    }
  };

  return (
    <Container
      style={{
        position: 'relative',
        backgroundColor: 'rgba(255,255,255,0.6)',
        borderRadius: 5,
        height: window.innerHeight - 80,
      }}
      sx={{
        b: 4,
        p: 2,
        mt: 2,
      }}
    >
      <Box
        style={{
          position: 'absolute',
          right: 0,
          display: 'flex',
        }}
      >
        <Button
          type="submit"
          variant="contained"
          style={{ backgroundColor: MAIN_COLOR }}
          sx={{ my: 2, mr: 1 }}
          onClick={handleTempSave}
          disabled={!tempFinish}
        >
          임시저장
        </Button>
        <Button
          type="submit"
          variant="contained"
          style={{ backgroundColor: MAIN_COLOR }}
          sx={{ my: 2, mr: 5 }}
          onClick={handleSave}
          disabled={!postFinish}
        >
          저장
        </Button>
      </Box>
      <PostEditor
        title={title}
        onTitleChange={e => setTitle(e.target.value)}
        content={content}
        setContent={setContent}
        category={category}
        onCategoryChange={setCategory}
        setStuffId={setStuffId}
        tempId={tempId}
        setTempId={setTempId}
        // setImageUrlList={setImageUrlList}
        // imageUrlList={imageUrlList}
      />
    </Container>
  );
}
