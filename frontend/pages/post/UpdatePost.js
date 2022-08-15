import { Button } from '@mui/material';
import { useState, useEffect } from 'react';
import UpdatePostEditor from '../../components/PostPage/UpdatePostEditor';
import { MAIN_COLOR } from '../../constants';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { postDetailState } from '../../state/postDetailState';
import { useRecoilState } from 'recoil';
import { useRouter } from 'next/router';

export default function updatePost() {
  //추후에 state관리 여기서 할 예정
  const [title, setTitle] = useState('');
  const [content, setContent] = useState([]);
  const [category, setCategory] = useState('');
  const [stuffId, setStuffId] = useState('');
  const [cookies] = useCookies(['cookie-name']);
  const [imageUrlList, setImageUrlList] = useState([]);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [tempId, setTempId] = useState('');
  const router = useRouter();

  const handleSave = async () => {
    if (tempId) {
      try {
        const userPost = {
          userStuffId: stuffId,
          title: title,
          content: content,
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
    } else {
      try {
        const userPost = {
          userStuffId: stuffId,
          title: title,
          content: content,
        };
        const { data } = await axios.post(
          `/api/v2/posts/${postDetail.id}`,
          userPost,
          {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              withCredentials: true,
            },
          },
        );
        router.back();
      } catch (e) {
        console.log(e);
      }
    }
  };

  const handleTempSave = async () => {
    if (tempId) {
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
          `/api/v2/posts/temp/${postDetail.id}`,
          userPost,
          {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              withCredentials: true,
            },
          },
        );
        router.back();
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    setTitle(postDetail.title);
    setContent(postDetail.content);
  }, []);

  return (
    <div style={{ position: 'relative' }}>
      <Button
        variant="contained"
        style={{
          position: 'absolute',
          right: 100,
          top: 20,
          backgroundColor: 'white',
          display: 'block',
          marginBottom: '0px',
          color: 'black',
        }}
        onClick={handleTempSave}
      >
        임시저장
      </Button>
      <Button
        type="submit"
        variant="contained"
        style={{
          position: 'absolute',
          right: 20,
          top: 20,
          backgroundColor: 'white',
          display: 'block',
          marginBottom: '0px',
          color: 'black',
        }}
        onClick={handleSave}
      >
        저장
      </Button>
      <UpdatePostEditor
        title={title}
        onTitleChange={e => setTitle(e.target.value)}
        content={content}
        setContent={setContent}
        category={category}
        onCategoryChange={setCategory}
        setStuffId={setStuffId}
        setImageUrlList={setImageUrlList}
        imageUrlList={imageUrlList}
        setTempId={setTempId}
      />
    </div>
  );
}
