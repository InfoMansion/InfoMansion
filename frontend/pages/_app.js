import Head from 'next/head'
import Mainframe from '../components/Mainframe'
import '../styles/globals.css'
import { RecoilRoot } from 'recoil'

function MyApp({ Component, pageProps }) {
  return (
    <>
      <Head>
        <link rel="icon" href="/favicon.ico"/>
        <title>InfoMaision</title>
      </Head>
      <RecoilRoot>
      <Mainframe>
        <Component {...pageProps} />
      </Mainframe>
      </RecoilRoot>
    </>
  )
}

export default MyApp
