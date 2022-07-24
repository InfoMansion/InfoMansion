import Head from 'next/head';
import Layout from '../components/Layout';
import '../styles/globals.css';
import { RecoilRoot } from 'recoil';
import { CookiesProvider } from 'react-cookie';

function MyApp({ Component, pageProps }) {
  return (
    <>
      <Head>
        <link rel="icon" href="/favicon.ico" />
        <title>InfoMansion</title>
      </Head>
      <RecoilRoot>
        <CookiesProvider>
          <Layout>
            <Component {...pageProps} />
          </Layout>
        </CookiesProvider>
      </RecoilRoot>
    </>
  );
}

export default MyApp;
