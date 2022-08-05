import { Avatar, Box, Card, Divider, formControlLabelClasses, Grid, Typography } from "@mui/material";
import { styled } from '@mui/material/styles'
import { useEffect, useState } from "react";
import RecentPost from "./RecentPosts";
import SettingsIcon from '@mui/icons-material/Settings';
import Link from 'next/link';
import axios from "../../utils/axios";
import { useCookies } from "react-cookie";
import { useRouter } from "next/router";

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
    const router = useRouter();
    const [cookies] = useCookies(['cookie-name']);

    // username 가지고 userinfo 가져와야 함.
    const [userinfo, setUserinfo] = useState({
        userEmail : 'infomansion@google.co.kr',
        profileImage : "/profile/9b34c022-bcd5-496d-8d9a-47ac76dee556defaultProfile.png",
        username : 'infomansion',
        introduce : ``, 
        followcount : 10,
        followingcount : 20,
        categories : []
    })

    useEffect(() => {
        if(!router.isReady) return;
        try{
            axios.get(`/api/v1/users/${router.query.userName}`, {
                headers : {
                    Authorization : `Bearer ${cookies.InfoMansionAccessToken}`,
                }
            })
            .then(res => {
                setUserinfo(res.data.data);
            })
        } catch(e) {
            console.log(e);
        }
    }, [router.isReady]);


    return (
        <Card>
            <Grid 
                sx={{
                    p : 2
                }}
                container
            >
                <Grid item xs={3}
                    sx={{
                        display : 'flex',
                        justifyContent : 'center'
                    }}
                >
                    <Avatar 
                        alt="profile" 
                        src={process.env.NEXT_PUBLIC_S3_PATH + userinfo.profileImage}
                        
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
                        {userinfo.userEmail}
                    </Typography>
                    <Box
                        sx={{
                            display : 'flex',
                            my : 1
                        }}
                    >
                        {userinfo.categories.map((category, index) => (
                            <Typography
                                variant="body2"
                                style={{
                                    backgroundColor : '#fc7a71',
                                    color : 'white'
                                }}
                                sx={{
                                    px : 2,
                                    mr : 1,
                                    borderRadius : 4
                                }}
                            >
                                {category}
                            </Typography>
                        ))}
                    </Box>
                </Grid>
            
            <Divider />
            <Typography
                sx={{ m : 2, }}
                >
                {userinfo.introduce}
            </Typography>
            
            {/* 여기 브레이크포인트에 따라 더보기 버튼과 recentpost 바꿔 사용할 것. */}
            <RecentPost />
                </Grid>
        </Card>
    )
}