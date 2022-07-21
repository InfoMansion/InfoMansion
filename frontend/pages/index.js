import { debounce } from 'lodash';
import { Link } from '@mui/material';
import styles from '../styles/Hex.module.css';
import { useEffect, useState } from 'react';

export default function Home() {
  if (typeof window !== 'undefined') {
    const [windowSize, setWindowSize] = useState({ width: window.innerWidth });

    const handleResize = () => {
      setWindowSize({
        width: window.innerWidth,
      });
    };
    useEffect(() => {
      window.addEventListener('resize', handleResize);
      return () => {
        window.removeEventListener('resize', handleResize);
      };
    }, []);
  }

  return (
    <>
      <h1>Main page</h1>
      <div style={{ display: 'flex', width: '100%', height: '640px' }}>
        <div style={{ width: '80%', height: '100%' }}>
          <ul className={styles.container}>
            {Array.from(
              {
                length:
                  typeof window !== 'undefined'
                    ? window.innerWidth >= 1200
                      ? 13
                      : 7
                    : 13,
              },
              (_, idx) => (
                <li className={styles.item}>
                  <Link href={`/RoomPage/${idx + 1}`}></Link>
                </li>
              ),
            )}
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
