import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Divider,
  Typography,
} from '@mui/material';
import IMG_S3_URL from '../../../constants';

export default function Post({
  post,
  totheight,
  picwidth,
  maxcontent,
  openModal,
}) {
  return (
    <Card
      key={post.title}
      sx={{
        maxHeight: totheight,
        my: 1,
        backgroundColor: 'rgba(255,255,255,0.5)',
      }}
    >
      <CardActionArea
        sx={{
          display: 'flex',
          alignItems: 'flex-start',
        }}
        onClick={() => openModal(post)}
      >
        <CardMedia
          component="img"
          sx={{
            width: picwidth,
          }}
          image={`${IMG_S3_URL}${post.defaultPostThumbnail}`}
          alt="no img"
        />

        <CardContent
          sx={{
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}
          >
            <Typography variant="h6" color="text.primary">
              {post.title}
            </Typography>
            <Typography variant="body4" color="#aaaaaa">
              {post.date}
            </Typography>
          </Box>
          <Divider sx={{ m: 1 }} />

          <Typography variant="body2" color="text.secondary">
            <div dangerouslySetInnerHTML={{ __html: post.content }}></div>
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}
