import { useEffect, useState } from "react";
import Room from "../../components/Room";
import { Box, Card, Container, Grid } from '@mui/material'
import UserInfo from "../../components/RoomPage/UserInfo";
import { useRouter } from 'next/router'
import StuffPage from "../../components/RoomPage/StuffPage";
import styles from '../../styles/Room.module.css'
import { useSpring, animated } from 'react-spring'


export default function RoomPage() {
    const [userName, setuserName] = useState(useRouter().query.userName);
    const [stuff, setStuff] = useState({});
    const [stuffon, setStuffon] = useState(false);
    const [useron, setUseron] = useState(true);
    
    function StuffClick(stuff) {
        // 여기서 stuffpage로 변수 전달하면 됨.
        setStuffon(!stuffon);
        setStuff(stuff);
    }

    function handleResize() {
        if(window.innerWidth >= 1200) setUseron(true);
        else setUseron(false);
    }
    useEffect(() => {
        window.addEventListener('resize', handleResize);   
    })

    const spaceAnimation = useSpring({
        from: { 
            opacity : 1, 
            height : window.innerWidth > 1200 ? "0px" : "200px" 
        },
        to : {  
            opacity : stuffon ? 0 : 1,
            maxHeight: stuffon ? "400px" : window.innerWidth > 1200 ? "0px" : "200px",
            height : stuffon ? '350px' : window.innerWidth > 1200 ? "0px" : "200px",
        },
        config: { 
            mass : 5,
            tension : 400,
            friction : 70, 
            precision : 0.0001,
            duration: "500",
        },
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
        },
        config: { 
            mass : 5,
            tension : 400,
            friction : 70, 
            precision : 0.0001,
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
                spacing={0}
                sx={{
                    flexDirection :'row-reverse',
                    justifyContent : 'center',
                }}
            >
                <Grid item lg={4}>
                    {useron ?
                        <UserInfo userName={userName} size="lg"/>
                        : <></>
                    }
                </Grid>

                <Grid item lg={7}
                    sx={{
                        display : 'flex',
                        flexDirection : 'column-reverse',
                        alignItems : 'center',
                        p : 1,
                        my : 2
                    }}
                >
                    <Box sx={{ my : 1 }} >
                        <Room 
                            StuffClick={StuffClick} 
                            userName={userName} 
                        />
                    </Box>

                    
                    <Box 
                        sx={{ 
                            zIndex : 'tooltip', 
                            width : 650,
                        }} 
                    >
                        {/* 페이지 */}
                        <animated.div
                            className={styles.stuffPage}
                            style={StuffAnimation}
                        >
                            <StuffPage data={stuff}/>
                        </animated.div>

                        {/* 공간 먹기 */}
                        <animated.div
                            className={styles.stuffPage}
                            style={spaceAnimation}
                        >
                            {stuffon || useron ? 
                                <></>
                                : 
                                <UserInfo userName={userName} size="md"/>
                            }
                        </animated.div>

                    </Box>            
                </Grid>
            </Grid>
        </Container>
    )
}