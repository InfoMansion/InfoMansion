import Link from 'next/link'

export default function Headernav() {
    return (
        <>
            <nav>
                {/* headernav는 이 파일을 수정해서 작업해 주시면 될거같습니다~~ */}
            <ul>
                <li>
                    <Link href="/">
                        <a>Main page</a>
                    </Link>
                </li>
                <li>
                    <Link href="/RoomPage">
                        <a>Roompage</a>
                    </Link>
                </li>
            </ul>
            </nav>
        </>
    )
}