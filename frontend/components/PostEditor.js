import React, { useState } from 'react';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';
import styles from '../styles/Editor.module.css';
import dynamic from 'next/dynamic';
import { EditorState, convertToRaw } from 'draft-js';
import styled from 'styled-components';
import draftToHtml from 'draftjs-to-html';
const Editor = dynamic(
  () => import('react-draft-wysiwyg').then(mod => mod.Editor),
  { ssr: false },
);
const IntroduceContent = styled.div`
  position: relative;
  border: 0.0625rem solid #d7e2eb;
  border-radius: 0.75rem;
  overflow: hidden;
  padding: 1.5rem;
  width: 50%;
  margin: 0 auto;
  margin-bottom: 4rem;
`;

export default function PostEditor(props) {
  const [editorState, setEditorState] = useState(EditorState.createEmpty());

  const onEditorStateChange = editorState => {
    // editorState에 값 설정
    console.log(editorState);
    setEditorState(editorState);
  };

  const editorToHtml = draftToHtml(
    convertToRaw(editorState.getCurrentContent()),
  );

  return (
    <>
      {
        <div className={styles.App}>
          <header className={styles.header}>Rich Text Editor Example</header>
          <Editor
            dwrapperClassName={styles.wrapper}
            // 에디터 주변에 적용된 클래스
            editorClassName={styles.editor}
            // 툴바 주위에 적용된 클래스
            toolbarClassName={styles.toolbar}
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
          <IntroduceContent
            dangerouslySetInnerHTML={{ __html: editorToHtml }}
          />
        </div>
      }
    </>
  );
}
