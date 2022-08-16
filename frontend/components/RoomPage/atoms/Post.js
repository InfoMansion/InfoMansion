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

export default function Post({ post, totheight, picwidth, openModal, pad = 1, my = 1 }) {
  return (
    <Box
      key={post.title}
      sx={{
        backgroundColor: 'rgba(255,255,255,0.8)',
        my : my,
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
          backgroundColor : 'white'
        }}
        onClick={() => openModal(post)}
      >
        <Box
          sx={{
            minWidth : picwidth,
            width : picwidth,
            height: '100%',

            display : 'flex',
            justifyContent : 'center'
          }}
        >
          <img
            src={`${post.defaultPostThumbnail}`}
            alt="no img"
            style={{ height : totheight }}
          />
        </Box>

        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            width: '100%',
            height : totheight,
            p : pad,
            backgroundColor : 'rgba(255, 255, 255,1)'
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
              sx={{ mr : 2 }}
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
          <Typography sx={{ overflow : 'hidden',}}
            variant="body2"
            color="text.secondary" 
          >
            <div dangerouslySetInnerHTML={{ __html: post.content }} />
          </Typography>
        </Box>
      </div>
    </Box>
  );
}
