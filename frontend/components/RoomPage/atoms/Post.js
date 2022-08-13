import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Divider,
  Paper,
  Typography,
} from '@mui/material';

export default function Post({ post, totheight, picwidth, openModal }) {
  return (
    <Box
      key={post.title}
      sx={{
        maxHeight: totheight,
        my: 1,
        backgroundColor: 'rgba(255,255,255,0)',
        //overflow: 'hidden',
      }}
    >
      <CardActionArea
        sx={{
          display: 'flex',
          justifyContent: 'flex-start',
          my: 1,
        }}
        onClick={() => openModal(post)}
      >
        <Box
          sx={{
            maxWidth: picwidth,
            height: '100%',
            mr: 2,
          }}
        >
          <img src={`${post.defaultPostThumbnail}`} alt="no img" />
        </Box>

        <CardContent
          sx={{
            display: 'flex',
            flexDirection: 'column',
            p: 0,
          }}
        >
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}
          >
            <Typography variant="h6" sx={{ mr: 2 }} color="text.primary">
              {post.title}
            </Typography>
            <Typography variant="body4" color="#aaaaaa">
              {post.modifiedDate.substring(0, 10)}
            </Typography>
          </Box>
          <Divider style={{ width: '100%' }} />

          <Typography variant="body2" color="text.secondary">
            <div dangerouslySetInnerHTML={{ __html: post.content }}></div>
          </Typography>
        </CardContent>
      </CardActionArea>
    </Box>
  );
}
