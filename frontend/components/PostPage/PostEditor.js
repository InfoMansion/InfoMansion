import React, {
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import dynamic from 'next/dynamic';
import 'react-quill/dist/quill.snow.css';
import styles from '../../styles/Editor.module.css';
import {
  Input,
  Autocomplete,
  TextField,
  styled,
  Container,
  Box,
} from '@mui/material';
import { MAIN_COLOR } from '../../constants';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
import { postDetailState } from '../../state/postDetailState';
import { useRecoilState } from 'recoil';
import CustomAlert from '../CustomAlert';
import Router from 'next/router';

Router.events.on('routeChangeStart', () => {
  localStorage.removeItem('title');
  localStorage.removeItem('content');
});

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
  title,
  content,
  setContent,
  setImageUrlList,
  imageUrlList,
  setTempId,
  tempId,
  open,
  setOpen,
  ...rest
}) => {
  const [cookies] = useCookies(['cookie-name']);
  const QuillRef = useRef();
  const [nowContent, setNowContent] = useState('content');
  const [nowTitle, setNowTitle] = useState('title');

  const onChange = (content, delta, source, editor) => {
    setContent(content);
    setNowContent(content);
    localStorage.setItem('content', content);
  };

  useEffect(() => {
    setNowTitle(title);
    localStorage.setItem('title', title);
  }, [title]);

  const ImageHandler = () => {
    const imageInput = document.createElement('input');
    imageInput.setAttribute('type', 'file');
    imageInput.setAttribute('accept', 'image/jpg,image/png,image/jpeg');
    imageInput.click();
    const tempTitle = localStorage.getItem('title') || '임시 제목';
    const tempContent = localStorage.getItem('content') || "<p>' '</p>";
    imageInput.onchange = async event => {
      const files = event.target.files;
      if (files[0]) {
        const userPostJson = {
          title: tempTitle,
          content: tempContent,
        };
        const userPost = JSON.stringify(userPostJson);
        const formData = new FormData();
        formData.append('image', files[0]);
        formData.append(
          'post',
          new Blob([userPost], {
            type: 'application/json',
          }),
        );
        try {
          const { data } = await axios.post(
            '/api/v2/posts/upload/temp',
            formData,
            {
              headers: {
                ContentType: 'multipart/form-data',
                Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
                withCredentials: true,
              },
            },
          );
          let imageUrl = data.data.imgUrl;
          let postId = data.data.postId;
          setTempId(postId);

          const range = QuillRef.current.getEditor().getSelection().index;
          if (range !== null && range !== undefined) {
            let quill = QuillRef.current.getEditor();
            quill.setSelection(range, 1);
            quill.clipboard.dangerouslyPasteHTML(
              range,
              `<img src=${imageUrl} alt="이미지 태그가 삽입" />`,
            );
            setOpen(true);
            // alert('이미지 업로드 전 까지의 내용이 임시저장 되었습니다.');
            localStorage.removeItem('title');
            localStorage.removeItem('content');
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
      // value={content}
      theme="snow"
      modules={modules}
      formats={formats}
      onChange={onChange}
      title={title}
      content={content}
      style={{
        backgroundColor: 'white',
        borderRadius: '6px',
      }}
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
  setTempId,
  setImageUrlList,
  imageUrlList,
}) {
  const [windowSize, setWindowSize] = useState();
  const [categoryList, setCategoryList] = useState([]);
  const [cookies] = useCookies(['cookie-name']);
  const [open, setOpen] = useState('');

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

  return typeof window !== 'undefined' ? (
    <>
      {windowSize && (
        <Box
          style={{
            display: 'grid',
            gridTemplateRows: 'auto auto 1fr',
            height: windowSize.height * 0.8,
          }}
          sx={{
            p: 2,
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
                backgroundColor: 'white',
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
                borderRadius: 2,
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
          <CustomAlert
            open={open}
            setOpen={setOpen}
            severity="info"
            message="이미지 업로드 전 까지의 내용이 임시저장 되었습니다."
          ></CustomAlert>
          <Input
            placeholder="제목을 입력하세요"
            inputProps={title}
            onChange={onTitleChange}
            disableUnderline={true} //here
            style={{
              fontSize: '30px',
              width: '100%',
              margin: '16px 0 8px',
              backgroundColor: 'white',
              borderRadius: 5,
            }}
            sx={{ px: 2 }}
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
            title={title}
            content={content}
            setContent={setContent}
            setTempId={setTempId}
            style={{
              backgroundColor: 'white',
            }}
            sx={{
              borderRadius: 5,
              height: windowSize.height * 0.75,
              overflow: 'auto',
            }}
            // 기타
            open={open}
            setOpen={setOpen}
            // imageUrlList={imageUrlList}
            // setImageUrlList={setImageUrlList}
          />
        </Box>
        /* <IntroduceContent
              dangerouslySetInnerHTML={{ __html: editorToHtml }}
            /> */
      )}
      <br />
    </>
  ) : null;
}
