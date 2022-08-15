import PropTypes from 'prop-types';
import Backdrop from '@mui/material/Backdrop';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import Typography from '@mui/material/Typography';
import { useSpring, animated } from 'react-spring';
import { forwardRef, useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Card, Divider, Grid } from '@mui/material';
import CreateIcon from '@mui/icons-material/Create';

const Fade = forwardRef(function Fade(props, ref) {
    const { in: open, children, onEnter, onExited, ...other } = props;
    const style = useSpring({
        from: { opacity: 0 },
        to: { opacity: open ? 1 : 0 },
        onStart: () => {
            if (open && onEnter) { onEnter(); }
        },
        onRest: () => {
            if (!open && onExited) { onExited(); }
        },
    });

    return (
        <animated.div ref={ref} style={style} {...other}>
        {children}
        </animated.div>
    );
});

Fade.propTypes = {
    children: PropTypes.element,
    in: PropTypes.bool.isRequired,
    onEnter: PropTypes.func,
    onExited: PropTypes.func,
};

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 800,
    height : 800,
    backgroundColor : 'rgba(255, 255, 255, 0.6)',
    b : 2,
    borderRadius : 2,
    boxShadow: 24,
    p: 4,
};

export default function GuestBookPage({open, setGuestBookOpen, userName, cookies}) {
    // 더미 데이터
    const [books, setBooks] = useState([
        {
            "id" : 1,
            "content" : "content1",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 2,
            "content" : "content2",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 3,
            "content" : "content3",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 4,
            "content" : "content1",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 5,
            "content" : "content2",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 6,
            "content" : "content3",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 7,
            "content" : "content1",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 8,
            "content" : "content2",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 9,
            "content" : "content3",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 10,
            "content" : "content1",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 11,
            "content" : "content2",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        }, {
            "id" : 12,
            "content" : "content3",
            "modifiedDate" : "2022-08-15T00:00:28.0012579",
            "likes" : 2
        } 
    ]);
    const [colorDeck] = useState([
        "#DCD3FF", "#ECD4FF", "#F3FFE3", "#FFCBC1", "#FFC9DE", "#AFCBFF"
    ])


    useEffect(() => {
        if(!userName) return; 

        axios.get(`/api/v1/posts/guestbook/${userName}`, {
            headers: {
                Authorization: `Bearer ${cookies.InfoMansionAccessToken}`,
            },
        })
        .then(res => {
            console.log(res.data.data.content);
            // setBooks(res.data.data.content);
        })
    }, [userName])
    
    return (
        <Modal
            aria-labelledby="spring-modal-title"
            aria-describedby="spring-modal-description"
            open={open}
            onClose={() => setGuestBookOpen(false)}
            closeAfterTransition
            BackdropComponent={Backdrop}
            BackdropProps={{ timeout: 500 }}
        >
            <Fade in={open}>
                <Box sx={style}>
                    <Box
                        style={{
                            display : 'flex',
                            justifyContent : 'space-between',
                        }}
                    >
                        <Typography variant="h5">
                            방명록
                        </Typography>
                        <CreateIcon />
                    </Box>

                    <Divider sx={{m : 1}}/>
                    
                    <Grid container spacing={2} 
                        sx={{
                            p : 1,
                            my : 2,
                            height : 680,
                            overflow : 'scroll'
                        }}
                    >
                        {books.map( book => (
                            <Grid item xs={4}>
                                <Card
                                    sx={{
                                        height : 200,
                                        backgroundColor : colorDeck[Math.floor(Math.random() * 5)],
                                        p : 2
                                    }}
                                    key={book.id}
                                >
                                    <Typography variant="body2" sx={{float : 'right'}}>
                                        {book.modifiedDate.substr(0, 10)}
                                    </Typography>

                                    <Divider sx={{mt : 3}}/>
                                    <Typography style={{overflowY : 'scroll'}}>
                                        {book.content}
                                    </Typography>
                                </Card>
                            </Grid>
                        ))}
                    </Grid>
                </Box>
            </Fade>
        </Modal>
    );
}