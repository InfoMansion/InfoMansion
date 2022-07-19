import { useState } from "react";
import Room from "../components/Room";

export default function RoomPage() {
    // 아직 작동은 안하지만, 사용자별 방을 보여줄 때 활용할 변수.
    const [userID, setUserID] = useState('');

    return (
        <>
            {/* 이곳에 룸페이지가 구현됩니다. */}
            <Room userID={userID}/>
            {/* 유저페이지랑 기타 등등 */}
        </>
    )
}