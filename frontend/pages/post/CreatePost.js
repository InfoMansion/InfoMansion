import { Button } from '@mui/material';
import { convertToRaw, EditorState } from 'draft-js';
import draftToHtml from 'draftjs-to-html';
import { useState } from 'react';
import PostView from '../../components/PostPage/PostView';
import { MAIN_COLOR } from '../../constants';

const contentToHtml = content => {
  return draftToHtml(convertToRaw(content.getCurrentContent()));
};

//추후에 state관리 여기서 할 예정
export default function CreatePost() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState(EditorState.createEmpty());
  const [category, setCategory] = useState('');
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
        onClick={() => {
          console.log({ title, content, category });
        }}
      >
        저장
      </Button>
      <PostView
        title={title}
        onTitleChange={e => setTitle(e.target.value)}
        content={content}
        onContentChange={setContent}
        category={category}
        onCategoryChange={setCategory}
      />
    </div>
  );
}
