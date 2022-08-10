import React, {
  forwardRef,
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import dynamic from 'next/dynamic';
import 'react-quill/dist/quill.snow.css';
import styles from '../../styles/Editor.module.css';
import { Input, Autocomplete, TextField, styled } from '@mui/material';
import { MAIN_COLOR } from '../../constants';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { postDetailState } from '../../state/postDetailState';
import { useRecoilState } from 'recoil';
import { Quill } from 'react-quill';

const ReactQuill = dynamic(
  async () => {
    const { default: RQ } = await import('react-quill');
    return function comp({ forwardRef, ...props }) {
      return <RQ ref={forwardRef} {...props} />;
    };
  },
  { ssr: false },
);

// 옵션에 상응하는 포맷, 추가해주지 않으면 text editor에 적용된 스타일을 볼수 없음
export const formats = [
  'header',
  'font',
  'size',
  'bold',
  'italic',
  'underline',
  'strike',
  'align',
  'blockquote',
  'list',
  'bullet',
  'indent',
  'background',
  'color',
  'link',
  'image',
  'width',
];

const Editor = ({
  placeholder,
  value,
  content,
  setContent,
  setImageUrlList,
  imageUrlList,
  ...rest
}) => {
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const [cookies] = useCookies(['cookie-name']);
  const QuillRef = useRef();
  const onChange = (content, delta, soruce, editor) => {
    setContent(content);
    console.log(content);
  };

  // const loadPrevContent = () => {
  //   const range = QuillRef.current.getEditor().getSelection().index;
  //   if (range !== null && range !== undefined) {
  //     let quill = QuillRef.current.getEditor();
  //     quill.clipboard.dangerouslyPasteHTML(0, `${postDetail.content}`);
  //   }
  // };

  const ImageHandler = () => {
    const imageInput = document.createElement('input');
    const formData = new FormData();
    let imageUrl;

    imageInput.setAttribute('type', 'file');
    imageInput.setAttribute('accept', 'image/jpg,image/png,image/jpeg');
    imageInput.click();

    imageInput.onchange = async event => {
      const files = event.target.files;
      if (files[0]) {
        formData.append('image', files[0]);
        try {
          const { data } = await axios.post('/api/v1/image/post', formData, {
            headers: {
              ContentType: 'multipart/form-data',
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              withCredentials: true,
            },
          });
          let imageUrl = data.data;
          let tempUrlList = imageUrlList;
          tempUrlList.push(imageUrl);
          setImageUrlList(tempUrlList);
          const range = QuillRef.current.getEditor().getSelection().index;
          if (range !== null && range !== undefined) {
            let quill = QuillRef.current.getEditor();
            quill.setSelection(range, 1);
            quill.clipboard.dangerouslyPasteHTML(
              range,
              `<img src=${imageUrl} alt="이미지 태그가 삽입" />`,
            );
          }
        } catch (e) {
          console.log(e);
        }
      }
    };
  };
  const modules = useMemo(
    () => ({
      toolbar: {
        container: [
          ['bold', 'italic', 'underline', 'strike', 'blockquote'],
          [{ size: ['small', false, 'large', 'huge'] }, { color: [] }],
          [
            { list: 'ordered' },
            { list: 'bullet' },
            { indent: '-1' },
            { indent: '+1' },
            { align: [] },
          ],
          ['image'],
        ],
        handlers: {
          image: ImageHandler,
        },
      },
    }),
    [],
  );
  return (
    <ReactQuill
      {...rest}
      placeholder={placeholder}
      forwardRef={QuillRef}
      value={postDetail.content}
      theme="snow"
      modules={modules}
      formats={formats}
      onChange={onChange}
    ></ReactQuill>
  );
};

export default function PostEditor({
  title,
  onTitleChange,
  content,
  setContent,
  category,
  onCategoryChange,
  setStuffId,
  setImageUrlList,
  imageUrlList,
}) {
  const [windowSize, setWindowSize] = useState();
  const [categoryList, setCategoryList] = useState([]);
  const [cookies] = useCookies(['cookie-name']);
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);

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
      console.log(data.data);
      data.data.forEach(stuff => {
        let categoryLabel = {
          label: stuff.category.categoryName,
          userStuffId: stuff.userStuffId,
        };
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
                setStuffId(v.userStuffId);
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
            value={postDetail.title}
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
            content={content}
            setContent={setContent}
            // 기타
            imageUrlList={imageUrlList}
            setImageUrlList={setImageUrlList}
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
