import React, { useState } from 'react';
import { useRouter } from 'next/router';
import { Tabs, Tab, Box, Divider, Typography } from '@mui/material';
import { MAIN_COLOR } from '../../constants';
import TabPanel from '../../components/PostPage/TabPanel';
import Post from '../../components/RoomPage/atoms/Post';
import postData from '../../components/jsonData/posts.json';
import PostViewModal from '../../components/PostPage/PostViewModal';
function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function SearchPost() {
  //검색 결과가 담기는 query입니다.
  const { query } = useRouter();
  const [value, setValue] = useState(0);
  const [posts] = useState(postData.slice(0, 5));
  const [showModal, setShowModal] = useState(false);
  const [post, setPost] = useState('');
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };
  const styles = theme => ({
    indicator: {
      backgroundColor: 'white',
    },
  });

  const openModal = post => {
    setPost(post);
    setShowModal(true);
  };
  return (
    <Box sx={{ width: '100%' }}>
      <PostViewModal
        post={post}
        showModal={showModal}
        setShowModal={setShowModal}
      ></PostViewModal>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs
          value={value}
          onChange={handleChange}
          textColorPrimary="black"
          sx={{
            '.MuiButtonBase-root': {
              '&.Mui-selected': {
                color: MAIN_COLOR,
              },
            },
          }}
          TabIndicatorProps={{
            style: {
              backgroundColor: MAIN_COLOR,
            },
          }}
        >
          <Tab label="제목" {...a11yProps(0)} />
          <Tab label="내용" {...a11yProps(1)} />
          <Tab label="닉네임" {...a11yProps(2)} />
        </Tabs>
      </Box>
      <TabPanel value={value} index={0}>
        <Divider />
        {posts.map(post => (
          <div>
            <Post
              post={post}
              totheight={150}
              picwidth={150}
              maxcontent={150}
              openModal={openModal}
            />
          </div>
        ))}{' '}
      </TabPanel>
      <TabPanel value={value} index={1}>
        내용 관련 검색 결과의 포스트들이 오겠죠?
      </TabPanel>
      <TabPanel value={value} index={2}>
        닉네임 관련 검색 결과의 포스트들이 오겠죠?
      </TabPanel>
    </Box>
  );
}
