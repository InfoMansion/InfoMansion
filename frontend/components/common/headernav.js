import Link from 'next/link'

export default function Headernav() {
    return (
        <>
            <nav>
            <ul>
                <li>
                    <Link href="/">
                        <a>Main page</a>
                    </Link>
                </li>
                <li>
                    <Link href="/example1">
                        <a>example1</a>
                    </Link>
                </li>
                <li>
                    <Link href="/example2">
                        <a>example2</a>
                    </Link>
                </li>
            </ul>
            </nav>
        </>
    )
}