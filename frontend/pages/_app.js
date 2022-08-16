import Head from 'next/head';
import Layout from '../components/Layout';
import '../styles/globals.css';
import { RecoilRoot } from 'recoil';
import { CookiesProvider } from 'react-cookie';
import { AuthProvider } from '../context/AuthProvider';
import { createTheme, ThemeProvider } from '@mui/material';

function MyApp({ Component, pageProps }) {
  let theme = createTheme({
    typography: {
      fontFamily: `"NanumSquare", sans-serif`,
    },
  })

  return (
    <>
      <Head>
        <link rel="icon" href="/favicon.ico" />
        <title>InfoMansion</title>
      </Head>
      <RecoilRoot>
        <AuthProvider>
          <CookiesProvider>
            <ThemeProvider theme={theme}>
              <Layout>
                <Component {...pageProps} />
              </Layout>
            </ThemeProvider>
          </CookiesProvider>
        </AuthProvider>
      </RecoilRoot>
    </>
  );
}

export default MyApp;
