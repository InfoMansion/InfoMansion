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
        backgroundColor: 'rgba(255,255,255,0.8)',
        p: 3,
        my: 2,
        borderRadius: 2,
        height: totheight,
        overflow: 'hidden',
        alignItems: 'center',
      }}
    >
      <div
        style={{
          display: 'flex',
          cursor: 'pointer',
        }}
        onClick={() => openModal(post)}
      >
        <Box
          sx={{
            minWidth: picwidth,
            height: '100%',
            mr: 2,
          }}
        >
          <img
            src={`${post.defaultPostThumbnail}`}
            alt="no img"
            style={{ width: picwidth, height: picwidth }}
          />
        </Box>
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
              }}
            >
              {post.title}
            </Typography>
            <Typography
              variant="body4"
              color="text.secondary"
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
