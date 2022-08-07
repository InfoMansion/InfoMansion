import React, { useState, useCallback, useEffect } from 'react';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';
import styles from '../../styles/Editor.module.css';
import dynamic from 'next/dynamic';
import { Input, Autocomplete, TextField, styled } from '@mui/material';
import { MAIN_COLOR } from '../../constants';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';

const Editor = dynamic(
  () => import('react-draft-wysiwyg').then(mod => mod.Editor),
  { ssr: false },
);

export default function PostEditor({
  title,
  onTitleChange,
  content,
  onContentChange,
  category,
  onCategoryChange,
}) {
  const [windowSize, setWindowSize] = useState();
  const [categoryList, setCategoryList] = useState([]);
  const [cookies] = useCookies(['cookie-name']);

  const handleResize = useCallback(() => {
    setWindowSize({
      height: window.innerHeight,
    });
  }, []);

  const UpdateUserCategory = async () => {
    try {
      const { data } = await axios.get('/api/v1/userstuffs/category', {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      let userCategoryList = [];
      data.data.forEach(stuff => {
        let categoryLabel = { label: stuff.category.categoryName };
        userCategoryList.push(categoryLabel);
      });
      setCategoryList(userCategoryList);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, [handleResize]);

  useEffect(() => {
    UpdateUserCategory();
  }, []);

  return (
    <>
      {windowSize && (
        <div
          style={{
            display: 'grid',
            gridTemplateRows: 'auto auto 1fr',
            height: windowSize.height * 0.8,
            padding: '20px',
          }}
        >
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <Autocomplete
              id="combo-box-demo"
              options={categoryList}
              disableClearable
              style={{ flexWrap: 'nowrap !important' }}
              sx={{
                width: 200,
                height: 1,
                '.MuiOutlinedInput-root': {
                  '&:hover fieldset, &.Mui-focused fieldset': {
                    borderColor: MAIN_COLOR,
                  },
                },
                '.MuiInputLabel-root': {
                  '&.Mui-focused': {
                    color: 'black',
                  },
                },
              }}
              value={category}
              onChange={(_, v) => {
                onCategoryChange(v.label);
              }}
              sy={{ height: 1 }}
              renderInput={params => (
                <TextField {...params} label="카테고리" size="small" />
              )}
            />
          </div>
          <Input
            placeholder="제목을 입력하세요"
            inputProps={title}
            onChange={onTitleChange}
            disableUnderline={true} //here
            style={{
              fontSize: '30px',
              width: '100%',
              margin: '16px 0 8px',
            }}
          />
          <Editor
            wrapperClassName={styles.wrapper}
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
            editorState={content}
            // 에디터의 값이 변경될 때마다 onEditorStateChange 호출
            onEditorStateChange={onContentChange}
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
