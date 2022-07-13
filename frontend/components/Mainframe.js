import HeaderNav from "./common/HeaderNav";

export default function Mainframe({children}) {
    return (
        <>
        <HeaderNav />
        {/* 이 밑의 자식을 room, mainpage로 바꿔가면서 동작하기 */}
        <div>
            {children}
        </div>
        </>
    )
}