import React, { useState, useCallback, useEffect } from 'react';
import { useRouter } from 'next/router';
import { Tabs, Tab, Box, Divider, Typography } from '@mui/material';
import { MAIN_COLOR } from '../../constants';
import TabPanel from '../../components/PostPage/TabPanel';
import Post from '../../components/RoomPage/atoms/Post';
import PostViewModal from '../../components/PostPage/PostViewModal';
import axios from '../../utils/axios';
import { useCookies } from 'react-cookie';
function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function searchPost() {
  //검색 결과가 담기는 query입니다.
  const { query } = useRouter();
  const [value, setValue] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [post, setPost] = useState('');
  const [postList, setPostList] = useState('');
  const [page, setPage] = useState(0);
  const [cookies] = useCookies(['cookie-name']);
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };
  const styles = theme => ({
    indicator: {
      backgroundColor: 'white',
    },
  });

  const init = useCallback(async () => {
    try {
      const { data } = await axios.get(
        `/api/v1/posts/search/${query.keyword}?page${page}&size=${5}`,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        },
      );
      setPostList(data.data.postsByContent.content);
      console.log(postList);
    } catch (e) {
      console.log(e);
    }
  }, [query]);

  useEffect(() => {
    init();
  }, [init]);

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
        {postList &&
          postList.map(post => (
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
