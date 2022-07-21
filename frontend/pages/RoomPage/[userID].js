import { useRef, useState } from "react";
import Room from "../../components/Room";
import { Box, Container, Grid } from '@mui/material'
import UserInfo from "../../components/RoomPage/UserInfo";
import { useRouter } from 'next/router'
import StuffPage from "../../components/RoomPage/StuffPage";
import styles from '../../styles/Room.module.css'
import { useSpring, animated } from 'react-spring'
// import { }

export default function RoomPage() {
    const [userID, setUserID] = useState(useRouter().query.userID);
    const [stuffID, setStuffID] = useState('');
    const [stuffon, setStuffon] = useState('');

    function StuffClick(stuff) {
        // 여기서 stuffpage로 변수 전달하면 됨.
        setStuffon(!stuffon);
        console.log("방에서 디버그" + stuff);
    }
    const openAnimation = useSpring({
        from: { opacity: "0.3", maxHeight: "0px" },
        to : { 
            opacity : stuffon ? "1" : "0.3", 
            maxHeight: stuffon ? "400px" : "0px",
            height : stuffon ? '350px' : '0px',
        },
        config: { 
            // easing : easeQuadInOut,
            duration: "500" 
        }
    });
    const StuffAnimation = useSpring({
        from : { opacity : "0.3", maxHeight : "0px" },
        to: async (next, cancel) => {
            await next({
                opacity : stuffon ? "1" : "0.3", 
                maxHeight: stuffon ? "600px" : "0px",
                height : stuffon ? '600px' : '0px',
                position : 'absolute',
            })
            await next({
                // position : stuffon ? 'absolute' : 'static',
            })
        },
        config: { 
            // easing : easeQuadInOut,
            duration: "500" 
        },

    });

    return (
        <Container
            style={{
                margin : '30px auto'
            }}
        >
            <Grid 
                container
                spacing={2}
                sx={{
                    flexDirection :'row-reverse',
                    justifyContent : 'center'
                }}
            >
                <Grid item 
                    lg={4}
                >
                    <UserInfo userID = {userID} />
                </Grid>

                <Grid item lg={7}
                    sx={{
                        display : 'flex',
                        flexDirection : 'column-reverse'
                    }}
                >
                    <Box
                        sx={{
                            my : 1 
                        }}
                    >
                        <Room 
                            StuffClick={StuffClick} 
                            userID={userID} 
                        />
                    </Box>

                    {
                        <Box
                            sx={{
                                zIndex : 'tooltip',
                            }}
                        >
                            <animated.div
                                className={styles.stuffPage}
                                style={StuffAnimation}
                            >
                                <StuffPage />
                            </animated.div>

                            <animated.div
                                className={styles.stuffPage}
                                style={openAnimation}
                            >
                            </animated.div>

                        </Box>
                    }
                </Grid>
            </Grid>
        </Container>
    )
}