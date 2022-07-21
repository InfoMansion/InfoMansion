import { flexbox } from '@mui/system';
import { Link } from '@mui/material';
import Head from 'next/head';
import Image from 'next/image';
import styles from '../styles/Hex.module.css';

export default function Home() {
  return (
    <>
      <h1>Main page</h1>
      <div style={{ display: 'flex', width: '1280px', height: '640px' }}>
        <div style={{ width: '80%', height: '100%' }}>
          <ul className={styles.container}>
            {Array.from({ length: 14 }, (_, idx) => (
              <li className={styles.item}>
                <Link href={`/RoomPage/${idx + 1}`}></Link>
              </li>
            ))}
          </ul>
        </div>
        <div style={{ width: '20%', height: '100%' }}>
          <div
            style={{ width: '100%', height: '70%', background: 'red' }}
          ></div>
          <div
            style={{ width: '100%', height: '30%', background: 'black' }}
          ></div>
        </div>
      </div>
    </>
  );
}
