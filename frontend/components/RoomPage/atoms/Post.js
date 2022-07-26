import { Box, Card, CardActionArea, CardContent, CardMedia, Divider, Typography } from "@mui/material";

export default function Post({post, totheight, picwidth, maxcontent}) {
    return (
    <Card 
        key={post.name}
        sx={{
            maxHeight : totheight,
            my : 1,
            backgroundColor : 'rgba(255,255,255,0.5)'
        }}
    >
        <CardActionArea
            sx={{
                display : 'flex',
                alignItems : 'flex-start'
            }}
            onClick={() => console.log(post.name)}
        >
            <CardMedia
                component="img"
                sx={{
                        width : picwidth,
                    }}
                    image={`/image/${post.image}`}
                    alt='no img'
            />
                
                <CardContent
                    sx={{
                        display : 'flex',
                        flexDirection : 'column', 
                    }}
                >
                    <Box
                        sx={{
                            display : 'flex',
                            justifyContent : 'space-between',
                            alignItems : 'center'
                        }}
                    >
                        <Typography
                            variant="h6"
                            color="text.primary"
                        >
                            {post.name}
                        </Typography>
                        <Typography
                            variant="body4"
                            color="#aaaaaa">
                            {post.date}
                        </Typography>
                    </Box>
                    <Divider sx={{m : 1}}/>
                
                    <Typography
                        variant="body2"
                        color='text.secondary'
                        
                    >
                        { 
                            ((post.content).length > maxcontent) ?
                            
                            (((post.content).substring(0, maxcontent-3)) + '...') 
                            : mytextvar 
                        }
                    </Typography>
                </CardContent>
            </CardActionArea>
        </Card>
    )
}