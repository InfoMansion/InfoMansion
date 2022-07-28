import Head from 'next/head';
import Layout from '../components/Layout';
import '../styles/globals.css';
import { RecoilRoot } from 'recoil';
import { CookiesProvider } from 'react-cookie';
import { AuthProvider } from '../context/AuthProvider';

function MyApp({ Component, pageProps }) {
  return (
    <>
      <Head>
        <link rel="icon" href="/favicon.ico" />
        <title>InfoMansion</title>
      </Head>
      <RecoilRoot>
        <AuthProvider>
          <CookiesProvider>
            <Layout>
              <Component {...pageProps} />
            </Layout>
          </CookiesProvider>
        </AuthProvider>
      </RecoilRoot>
    </>
  );
}

export default MyApp;
