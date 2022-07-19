import { Avatar, Box, Card, Grid, Typography } from "@mui/material";
import { styled } from '@mui/material/styles'
import { maxWidth } from "@mui/system";
import { useState } from "react";
import RecentPost from "./RecentPost";

export default function UserInfo( {...props} ) {
    const [userID] = useState(props.userID);

    // userID 가지고 userinfo 가져와야 함.
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
        <Card 
            // sx={{
            //     m : 2
            // }}
        >
            <Grid 
                sx={{
                    p : 2
                }}
                container
                xs={{
                    width : '600px'
                }}
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
                    <Typography>{userinfo.username}</Typography>
                    <Typography>{userinfo.email}</Typography>
                </Grid>

            <Typography
                sx={{
                    m : 2,
                }}
                >
                {userinfo.introduction}
            </Typography>


            <RecentPost />
                </Grid>
        </Card>
    )
}