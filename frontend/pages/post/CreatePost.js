import dynamic from 'next/dynamic';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Input from '@mui/material/Input';
import Autocomplete from '@mui/material/Autocomplete';
import { useCallback, useEffect, useState } from 'react';

const ariaLabel = { 'aria-label': 'description' };

const TuiEditor = dynamic(() => import('../../components/TuiEditor'), {
  ssr: false,
});
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
export default function PostWithTuiEditor({}) {
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
        <div style={{ height: windowSize.height * 0.8 }}>
          <div style={{ height: '96%' }}>
            <Autocomplete
              disablePortal
              id="combo-box-demo"
              options={categoryList}
              sx={{ width: 200 }}
              sy={{ height: 10 }}
              renderInput={params => <TextField {...params} label="카테고리" />}
            />
            <Input
              placeholder="제목을 입력하세요"
              inputProps={ariaLabel}
              style={{
                fontSize: '5vh',
                height: '10%',
                width: '100%',
              }}
            />
            <TuiEditor />
            <div
              style={{
                paddingTop: '7px',
                float: 'right',
              }}
            >
              <Button variant="contained" size="small">
                작성
              </Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
