import Head from 'next/head';
import Image from 'next/image';
import styles from '../styles/Home.module.css';
import Login from './user/login';

export default function Home() {
  return (
    <div className={styles.container}>
      <h1>Main page</h1>
      <Login></Login>
    </div>
  );
}
