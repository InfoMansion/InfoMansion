import { flexbox } from '@mui/system';
import Head from 'next/head';
import Image from 'next/image';
import styles from '../styles/Home.module.css';
import Login from './user/login';

export default function Home() {
  return (
    <>
      <h1>Main page</h1>
      <Login></Login>
      <div style={{ display: 'flex', width: '1280px', height: '640px' }}>
        <div style={{ width: '80%', height: '100%', background: 'blue' }}></div>
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
