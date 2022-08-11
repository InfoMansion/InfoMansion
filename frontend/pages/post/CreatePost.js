import { Button } from '@mui/material';
import { useState, useEffect } from 'react';
import PostEditor from '../../components/PostPage/PostEditor';
import { MAIN_COLOR } from '../../constants';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { postDetailState } from '../../state/postDetailState';
import { useRecoilState } from 'recoil';

export default function createPost() {
  //추후에 state관리 여기서 할 예정
  const [title, setTitle] = useState('');
  const [content, setContent] = useState([]);
  const [category, setCategory] = useState('');
  const [stuffId, setStuffId] = useState('');
  const [cookies] = useCookies(['cookie-name']);
  const [imageUrlList, setImageUrlList] = useState([]);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);

  const handleSave = async () => {
    if (!postDetail) {
      try {
        const userPost = {
          userStuffId: stuffId,
          title: title,
          content: content,
          images: imageUrlList,
        };
        console.log(userPost);
        const { data } = await axios.post('/api/v1/posts', userPost, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        });
        console.log(data);
      } catch (e) {
        console.log(e);
      }
    } else {
      try {
        const userPost = {
          postId: postDetail.id,
          userStuffId: stuffId,
          title: title,
          content: content,
          images: imageUrlList,
        };
        const { data } = await axios.put('/api/v1/posts', userPost, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        });
        console.log(data);
      } catch (e) {
        console.log(e);
      }
    }
  };
  return (
    <div style={{ position: 'relative' }}>
      <Button
        type="submit"
        variant="contained"
        style={{
          position: 'absolute',
          right: 20,
          top: 20,
          backgroundColor: MAIN_COLOR,
          display: 'block',
          marginBottom: '0px',
        }}
        onClick={handleSave}
      >
        저장
      </Button>
      <PostEditor
        title={title}
        onTitleChange={e => setTitle(e.target.value)}
        content={content}
        setContent={setContent}
        category={category}
        onCategoryChange={setCategory}
        setStuffId={setStuffId}
        setImageUrlList={setImageUrlList}
        imageUrlList={imageUrlList}
      />
    </div>
  );
}
