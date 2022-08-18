import { Box, Button, Container } from '@mui/material';
import { useState, useEffect } from 'react';
import UpdatePostEditor from '../../components/PostPage/UpdatePostEditor';
import { MAIN_COLOR } from '../../constants';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import CustomAlert from '../../components/CustomAlert';
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
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [tempId, setTempId] = useState('');
  const router = useRouter();
  const postFinish = !!content && !!stuffId && !!title;
  const tempFinish = !!content && !!title;

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
        if (postDetail.defaultPostThumbnail) {
          setMessage('기존 글 수정 시 크레딧이 획득되지 않습니다.');
        } else {
          setMessage('글 쓰기로 획득 가능한 크레딧은 1회 20 하루 100입니다.');
        }
        setOpen(true);
        // router.back();
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
        if (postDetail.defaultPostThumbnail) {
          setMessage('기존 글 수정 시 크레딧이 획득되지 않습니다.');
        } else {
          setMessage('글 쓰기로 획득 가능한 크레딧은 1회 20 하루 100입니다.');
        }
        setOpen(true);
        // router.back();
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
        setMessage('임시저장 되었습니다.');
        setOpen(true);
        // router.back();
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
        setMessage('임시저장 되었습니다.');
        setOpen(true);
        // router.back();
      } catch (e) {
        console.log(e);
      }
    }
  };

  useEffect(() => {
    setTitle(postDetail.title);
    setContent(postDetail.content);
  }, []);

  useEffect(() => {
    if (!open && message) {
      router.back();
    }
  }, [open]);

  useEffect(() => {
    const timer = setInterval(() => {
      const tempTitle = document.getElementById('tempTitle').value;
      const tempContent = document.querySelector('.ql-editor').innerHTML;
      const tempTempId = document.getElementById('tempId').innerText;
      if (tempTempId == '' && tempTitle && tempContent) {
        const userPost = {
          title: tempTitle,
          content: tempContent,
        };
        axios
          .post(`/api/v2/posts/temp/${postDetail.id}`, userPost, {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              withCredentials: true,
            },
          })
          .then(res => {
            document.getElementById('tempId').innerText = res.data.data.postId;
          });
      } else if (tempTempId && tempTitle && tempContent) {
        try {
          const userPost = {
            title: tempTitle,
            content: tempContent,
          };
          const response = axios.post(
            `/api/v2/posts/temp/${tempTempId}`,
            userPost,
            {
              headers: {
                Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                withCredentials: true,
              },
            },
          );
          // router.back();
        } catch (e) {
          console.log(e);
        }
      }
    }, 600000);
    return () => clearInterval(timer);
  }, []);

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
      <div
        id="tempId"
        hidden
        onChange={event => {
          setTempId(event.target.value);
        }}
      >
        {tempId}
      </div>
      <Box
        style={{
          position: 'absolute',
          right: 0,
          display: 'flex',
        }}
      >
        <CustomAlert
          open={open}
          setOpen={setOpen}
          severity="info"
          message={message}
        ></CustomAlert>
        <Button
          variant="contained"
          style={{
            backgroundColor: MAIN_COLOR,
          }}
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
    </Container>
  );
}
