import React, { useState, useCallback, useEffect } from 'react';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';
import styles from '../../styles/Editor.module.css';
import dynamic from 'next/dynamic';
import { EditorState, convertToRaw } from 'draft-js';
import styled from 'styled-components';
import draftToHtml from 'draftjs-to-html';
import { Input, Button, Autocomplete, TextField } from '@mui/material';

const Editor = dynamic(
  () => import('react-draft-wysiwyg').then(mod => mod.Editor),
  { ssr: false },
);
const categoryList = [
  { label: '카테고리 없음' },
  { label: 'Sport' },
  { label: 'Daily' },
  { label: 'Fasion & Beauty' },
  { label: 'Travel' },
  { label: 'Cooking' },
  { label: 'Art' },
  { label: 'Music' },
  { label: 'Interior' },
  { label: 'Nature' },
  { label: 'IT' },
  { label: 'Game' },
  { label: 'Clean' },
  { label: 'Culture/Current' },
  { label: 'Study' },
  { label: 'Home Appliance' },
];

export default function PostEditor(props) {
  const [editorState, setEditorState] = useState(EditorState.createEmpty());
  const [title, setTitle] = useState('');
  const onEditorStateChange = editorState => {
    // editorState에 값 설정
    setEditorState(editorState);
  };

  const onTitleStateChange = e => {
    setTitle(e.target.value);
  };

  const editorToHtml = draftToHtml(
    convertToRaw(editorState.getCurrentContent()),
  );

  const [windowSize, setWindowSize] = useState();

  const handleResize = useCallback(() => {
    setWindowSize({
      height: window.innerHeight,
    });
  }, []);

  useEffect(() => {
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, [handleResize]);

  return (
    <>
      {windowSize && (
        <div style={{ height: windowSize.height * 0.8, padding: '20px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <Autocomplete
              id="combo-box-demo"
              options={categoryList}
              style={{ flexWrap: 'nowrap !important' }}
              sx={{ width: 150, height: 1 }}
              sy={{ height: 1 }}
              renderInput={params => (
                <TextField {...params} label="카테고리" size="small" />
              )}
            />
            <Button
              type="submit"
              variant="contained"
              style={{
                backgroundColor: '#fc7a71',
                display: 'block',
                marginBottom: '0px',
              }}
              onClick={() => {
                console.log({ titlt: { title }, text: { editorToHtml } });
              }}
            >
              저장
            </Button>
          </div>
          <br />

          <Input
            placeholder="제목을 입력하세요"
            inputProps={title}
            disableClearable
            onChange={onTitleStateChange}
            disableUnderline={true} //here
            style={{
              fontSize: '6vh',
              width: '100%',
            }}
          />
          <Editor
            dwrapperClassName={styles.wrapper}
            // 에디터 주변에 적용된 클래스
            // 툴바 주위에 적용된 클래스
            toolbarClassName={styles.toolbar}
            editorClassName={styles.editor}
            // 툴바 설정
            toolbar={{
              // inDropdown: 해당 항목과 관련된 항목을 드롭다운으로 나타낼것인지
              list: { inDropdown: true },
              textAlign: { inDropdown: true },
              link: { inDropdown: true },
              history: { inDropdown: false },
            }}
            placeholder="내용을 작성해주세요."
            // 한국어 설정
            localization={{
              locale: 'ko',
            }}
            // 초기값 설정
            editorState={editorState}
            // 에디터의 값이 변경될 때마다 onEditorStateChange 호출
            onEditorStateChange={onEditorStateChange}
          />
        </div>
        /* <IntroduceContent
            dangerouslySetInnerHTML={{ __html: editorToHtml }}
          /> */
      )}
      <br />
    </>
  );
}
