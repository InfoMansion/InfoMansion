import { Avatar, Box, Card, Divider, Grid, Typography } from "@mui/material";
import { styled } from '@mui/material/styles'
import { useState } from "react";
import RecentPost from "./RecentPosts";
import SettingsIcon from '@mui/icons-material/Settings';
import Link from 'next/link';

const Root = styled('div')(({ theme }) => ({
    padding: theme.spacing(1),
    margin : '30px auto',
    
    [theme.breakpoints.down('lg')]: {
      width : '600px'
    },
    [theme.breakpoints.up('lg')]: {
      width : '1280px',
    },
}));

export default function UserInfo( {userName} ) {
    // username 가지고 userinfo 가져와야 함.
    const [userinfo, setuserinfo] = useState({
        email : 'hellossafy@ssafy.com',
        profile_image : 'profile.jpg',
        username : 'hellossafy',
        introduction : `
        안녕 나는 한쿡이 너무 좋아서 얼마 전에 한쿡에 온 hellossafy라고 해.
        다들 반가워 잘 부탁해.

        InfoMansion 너무 좋음거 같아.
        `, 
        followcount : 10,
        followingcount : 20,
    })

    return (
        <Card>
            <Grid 
                sx={{
                    p : 2
                }}
                container
            >
                <Grid item xs={3}>
                    <Avatar 
                        alt="profile" 
                        src={`/image/${userinfo.profile_image}`}
                        
                        sx={{ 
                            width : '100%',
                            maxWidth : '80px',
                            height : '100%',
                            maxHeight : '80px'
                        }}
                    />

                </Grid>
                <Grid item xs={9}>
                    <Box
                        sx={{
                            display : 'flex',
                            alignItems : 'center'
                        }}
                    >
                        <Typography
                            variant='h4'
                        >
                            {userinfo.username}
                        </Typography>

                        <Link href={userName + "/dashboard"}>
                            <SettingsIcon  
                                sx={{mx : 2}}
                                style={{color : '#777777'}}
                            />
                        </Link>
                    </Box>
                    <Typography
                        variant="body2"
                        color="text.secondary"
                    >
                        {userinfo.email}
                    </Typography>
                </Grid>
            
            <Divider />
            <Typography
                sx={{
                    m : 2,
                }}
                >
                {userinfo.introduction}
            </Typography>
            
            {/* 여기 브레이크포인트에 따라 더보기 버튼과 recentpost 바꿔 사용할 것. */}
            <RecentPost />
                </Grid>
        </Card>
    )
}