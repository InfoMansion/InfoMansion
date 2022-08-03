import { Divider, Typography } from '@mui/material';
import { useState } from 'react'
import Post from './atoms/Post';
import postdata from '../jsonData/posts.json'

export default function RecentPost() {

    // 여기에 post를 몇개 넘겨줄지 혹시 백에서 정했나요?
    const [posts] = useState(postdata.slice(0, 2));

    // post의 css를 모듈화해서, 여기랑 stuffpage에 각각 적용
    return (
        <div>
            <Divider />
            <Typography
                variant='h6'
            >
                Recent post
            </Typography>
            {posts.map(post => (
                <div>
                    <Post
                        post={post}
                        totheight={70}
                        picwidth={70}
                        maxcontent={50}
                    />       
                </div>
            ))}
        </div>
    )
}