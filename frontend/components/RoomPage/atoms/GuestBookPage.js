import PropTypes from 'prop-types';
import Backdrop from '@mui/material/Backdrop';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import Typography from '@mui/material/Typography';
import { useSpring, animated } from 'react-spring';
import { forwardRef, useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Button, Card, Divider, Grid, TextField } from '@mui/material';
import CreateIcon from '@mui/icons-material/Create';
import DeleteIcon from '@mui/icons-material/Delete';
import useAuth from '../../../hooks/useAuth';

const Fade = forwardRef(function Fade(props, ref) {
  const { in: open, children, onEnter, onExited, ...other } = props;
  const style = useSpring({
    from: { opacity: 0 },
    to: { opacity: open ? 1 : 0 },
    onStart: () => {
      if (open && onEnter) {
        onEnter();
      }
    },
    onRest: () => {
      if (!open && onExited) {
        onExited();
      }
    },
  });

  return (
    <animated.div ref={ref} style={style} {...other}>
      {children}
    </animated.div>
  );
});

Fade.propTypes = {
  children: PropTypes.element,
  in: PropTypes.bool.isRequired,
  onEnter: PropTypes.func,
  onExited: PropTypes.func,
};

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 800,
  height: 800,
  backgroundColor: 'rgba(255, 255, 255, 0.6)',
  b: 2,
  borderRadius: 2,
  boxShadow: 24,
  p: 4,
};

const postStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  height: 400,
  backgroundColor: 'white',
  b: 2,
  borderRadius: 2,
  boxShadow: 24,
  p: 4,
};

export default function GuestBookPage({
  open,
  setGuestBookOpen,
  userName,
  cookies,
}) {
  // 더미 데이터
  const [books, setBooks] = useState([]);
  // 방명록 작성
  const submitGuestBook = async event => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const gontent = data.get('content');
    const guestContent = {
      content: gontent,
    };
    try {
      const { data } = await axios.post(
        `/api/v1/posts/guestbook/${userName}`,
        guestContent,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        },
      );
      console.log(data);
      axios
        .get(`/api/v1/posts/guestbook/${userName}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          // console.log(res.data.data.content);
          setBooks(res.data.data.content);
        });
      setPostOpen(false);
    } catch (e) {
      console.log(e);
    }
  };

  const deleteGuestBook = async (id, event) => {
    const postInfo = {
      postId: id,
    };
    try {
      const { data } = await axios.patch(`/api/v1/posts/${id}`, postInfo, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          withCredentials: true,
        },
      });
      console.log(data);
      axios
        .get(`/api/v1/posts/guestbook/${userName}`, {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
          },
        })
        .then(res => {
          // console.log(res.data.data.content);
          setBooks(res.data.data.content);
        });
    } catch (e) {
      console.log(e);
    }
  };

  const [colorDeck] = useState([
    '#DCD3FF',
    '#ECD4FF',
    '#F3FFE3',
    '#FFCBC1',
    '#FFC9DE',
    '#AFCBFF',
  ]);

  const [postOpen, setPostOpen] = useState(false);
  const handlePostOpen = () => setPostOpen(true);
  const handlePostClose = () => setPostOpen(false);
  const { auth, setAuth } = useAuth();

  useEffect(() => {
    if (!userName) return;

    axios
      .get(`/api/v1/posts/guestbook/${userName}`, {
        headers: {
          Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
        },
      })
      .then(res => {
        // console.log(res.data.data.content);
        setBooks(res.data.data.content);
      });
  }, [userName]);

  return (
    <Modal
      aria-labelledby="spring-modal-title"
      aria-describedby="spring-modal-description"
      open={open}
      onClose={() => setGuestBookOpen(false)}
      closeAfterTransition
      BackdropComponent={Backdrop}
      BackdropProps={{ timeout: 500 }}
    >
      <Fade in={open}>
        <Box sx={style}>
          <Box
            style={{
              display: 'flex',
              justifyContent: 'space-between',
            }}
          >
            <Typography variant="h5">방명록</Typography>
            <CreateIcon onClick={handlePostOpen} sx={{ cursor: 'pointer' }} />
            <Modal
              open={postOpen}
              onClose={handlePostClose}
              aria-labelledby="modal-modal-title"
              aria-describedby="modal-modal-description"
            >
              <Box sx={postStyle} component="form" onSubmit={submitGuestBook}>
                <div
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                  }}
                >
                  <Typography
                    id="modal-modal-title"
                    variant="h6"
                    component="h2"
                  >
                    방명록 작성하기
                  </Typography>
                  <Button type="submit">
                    <CreateIcon sx={{ float: 'right' }} />
                  </Button>
                </div>
                <TextField
                  id="modal-modal-description"
                  name="content"
                  sx={{ mt: 2 }}
                  fullWidth
                  multiline
                  rows={10}
                  label={auth.username}
                />
              </Box>
            </Modal>
          </Box>

          <Divider sx={{ m: 1 }} />

          <Grid
            container
            spacing={2}
            sx={{
              p: 1,
              my: 2,
              overflow: 'scroll',
            }}
          >
            {books.map(book => (
              <Grid item xs={4}>
                <Card
                  sx={{
                    height: 200,
                    backgroundColor: colorDeck[Math.floor(Math.random() * 5)],
                    px : 2,
                    py : 1,
                    pb : 8
                  }}
                  key={book.id}
                >
                  <Typography variant="h6">{book.username}</Typography>
                  {book.username === auth.username ||
                  userName === auth.userName ? (
                    <DeleteIcon
                      style={{ float: 'right', cursor: 'pointer' }}
                      onClick={e => deleteGuestBook(book.id, e)}
                    />
                  ) : <></> 
                  }

                  <Typography variant="body2" style={{ color: 'grey' }}>
                    {book.modifiedDate.substr(0, 10)}
                  </Typography>

                  <Divider sx={{ mt: 1 }} />
                  <Typography style={{ height : '90%', overflowY : 'scroll' }}>
                    {book.content}
                  </Typography>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Box>
      </Fade>
    </Modal>
  );
}
