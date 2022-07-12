import Headernav from "./common/headernav";

export default function Mainframe({children}) {
    return (
        <>
        <Headernav />
        {/* 이 밑의 자식을 room, mainpage로 바꿔가면서 동작하기 */}
        <div>
            {children}
        </div>
        </>
    )
}