import Head from 'next/head'
import Image from 'next/image'
import styles from '../styles/Home.module.css'

export default function Home() {
  return (
    <div className={styles.container}>
      <h1>Main page</h1>
      {/* 이곳에 메인프레임을 구성해 주시면 됩니다. */}
    </div>
  )
}
