import { Link } from '@mui/material';
import styles from '../styles/Hex.module.css';
import { useCallback, useEffect, useState } from 'react';

export default function Home() {
  const [windowSize, setWindowSize] = useState();

  const handleResize = useCallback(() => {
    setWindowSize({
      width: window.innerWidth,
    });
  }, []);

  useEffect(() => {
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, [handleResize]);

  return (
    <>
      <h1>Main page</h1>
      <div style={{ display: 'flex', width: '100%', height: '640px' }}>
        <div style={{ width: '80%', height: '100%' }}>
          <ul className={styles.container}>
            {windowSize &&
              Array.from(
                {
                  length: windowSize.width >= 1200 ? 13 : 7,
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
