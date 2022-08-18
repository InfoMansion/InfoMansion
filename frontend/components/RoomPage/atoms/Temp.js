import {
  Box,
  Button,
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Divider,
  IconButton,
  Paper,
  Typography,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from '../../../utils/axios';
import { useCookies } from 'react-cookie';

export default function Temp({
  post,
  totheight,
  picwidth,
  openModal,
  isDeleted,
  setIsDeleted,
  handleModalClose,
}) {
  const [cookies] = useCookies(['cookie-name']);
  const TempDelete = async () => {
    const postInfo = {};
    try {
      const { data } = await axios.patch(
        `/api/v1/posts/${post.postId}`,
        postInfo,
        {
          headers: {
            Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            withCredentials: true,
          },
        },
      );
      // handleModalClose();
      setIsDeleted(!isDeleted);
      // console.log(data);
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <Box
      key={post.title}
      sx={{
        height: totheight,
        p: 3,
        my: 2,
        borderRadius: 2,
        backgroundColor: 'rgba(255,255,255,0.8)',
        overflow: 'hidden',
        alignItems: 'center',
      }}
    >
      <div
        style={{
          display: 'flex',
        }}
        // onClick={() => openModal(post)}
      >
        <CardContent
          sx={{
            display: 'flex',
            flexDirection: 'column',
            width: '100%',
            p: 0,
          }}
        >
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: '1fr auto',
              justifyContent: 'space-between',
            }}
          >
            <Typography
              variant="h6"
              sx={{ mr: 2 }}
              color="text.primary"
              style={{
                width: '100%',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap',
                cursor: 'pointer',
              }}
              onClick={() => openModal(post)}
            >
              {post.title}
            </Typography>
            <IconButton onClick={TempDelete}>
              <DeleteIcon
                style={{ float: 'right', cursor: 'pointer' }}
              ></DeleteIcon>
            </IconButton>
            <Typography
              variant="body4"
              color="#aaaaaa"
              style={{ whiteSpace: 'nowrap' }}
            >
              {post.modifiedDate.substring(0, 10)}
            </Typography>
          </Box>
          <Divider style={{ width: '100%' }} />

          <Typography variant="body2" color="text.secondary">
            <div dangerouslySetInnerHTML={{ __html: post.content }}></div>
          </Typography>
        </CardContent>
      </div>
    </Box>
  );
}
