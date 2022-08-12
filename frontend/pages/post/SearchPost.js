import React, { useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/router';
import { Tabs, Tab, Box, Divider, Typography, Button } from '@mui/material';
import { MAIN_COLOR } from '../../constants';
import TabPanel from '../../components/PostPage/TabPanel';
import Post from '../../components/RoomPage/atoms/Post';
import Profile from '../../components/PostPage/Profile';
import PostViewModal from '../../components/PostPage/PostViewModal';
import { useCookies } from 'react-cookie';
import axios from '../../utils/axios';
import { postDetailState } from '../../state/postDetailState';
import { loginUserState } from '../../state/roomState';
import { useRecoilState } from 'recoil';
import useAuth from '../../hooks/useAuth';

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

const categories = ['title', 'content', 'username'];

const defaultPosts = {
  title: {
    data: [],
    page: -1,
  },
  content: {
    data: [],
    page: -1,
  },
  username: {
    data: [],
    page: -1,
  },
};

export default function searchPost() {
  //검색 결과가 담기는 query입니다.
  const { query } = useRouter();
  const [category, setCategory] = useState('title');
  const [posts, setPosts] = useState(defaultPosts);
  const [showModal, setShowModal] = useState(false);
  const [post, setPost] = useState('');
  const [postDetail, setPostDetail] = useRecoilState(postDetailState);
  const { auth, setAuth } = useAuth();
  const [cookies] = useCookies(['cookie-name']);
  const [loginUser, setLoginUser] = useRecoilState(loginUserState);

  const styles = theme => ({
    indicator: {
      backgroundColor: 'white',
    },
  });

  const openModal = post => {
    setPost(post);
    try {
      axios
        .get(`/api/v1/posts/detail/${post.id}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          console.log(res.data);
          setPostDetail(res.data.data);
          if (postDetail.userName === auth.username) {
            setLoginUser(true);
          }
        })
        .then(setShowModal(true));
    } catch (e) {
      console.log(e);
    }
  };

  const getResult = useCallback(
    async (category, page = 0) => {
      if (posts[category].page === page) {
        return;
      }
      try {
        const { data } = await axios.get(
          `/api/v1/${
            category === 'username' ? 'users' : 'posts'
          }/search/${category}?searchWord=${query.keyword}&page=${page}&size=5`,
          {
            headers: {
              Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
              withCredentials: true,
            },
          },
        );
        console.log(data);
        setTimeout(() => {
          setPosts(prev => ({
            ...prev,
            [category]: {
              data: [
                ...prev[category].data,
                ...data.data[
                  category === 'username'
                    ? 'usersByUserName'
                    : 'postsByTitleOrContent'
                ].content,
              ],
              page,
            },
          }));
        }, 0);
      } catch (e) {
        console.log(e);
      }
    },
    [query.keyword, cookies, category],
  );

  useEffect(() => {
    setTimeout(() => {
      setPosts(defaultPosts);
    }, 0);
    getResult('title');
    getResult('content');
    getResult('username');
  }, [query.keyword]);
  console.log(posts[category]);

  const handleModalClose = () => {
    setShowModal(false);
    setPostDetail('');
  };

  const value = categories.indexOf(category);
  return (
    <Box sx={{ width: '100%' }}>
      <PostViewModal
        showModal={showModal}
        handleModalClose={handleModalClose}
      ></PostViewModal>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs
          value={value}
          onChange={(event, newValue) => {
            setCategory(categories[newValue]);
          }}
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
      {categories.map((category, index) => (
        <TabPanel key={category} value={value} index={index}>
          {posts[category].data.map(post => (
            <div key={post.id}>
              {category === 'username' ? (
                <Profile user={post} />
              ) : (
                <Post
                  post={post}
                  totheight={150}
                  picwidth={150}
                  maxcontent={150}
                  openModal={openModal}
                />
              )}
            </div>
          ))}{' '}
        </TabPanel>
      ))}
      <Button
        onClick={() => {
          getResult(category, posts[category].page + 1);
        }}
      >
        more
      </Button>
    </Box>
  );
}
