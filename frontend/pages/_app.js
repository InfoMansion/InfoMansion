import Head from 'next/head'
import Mainframe from '../components/Mainframe'
import '../styles/globals.css'

function MyApp({ Component, pageProps }) {
  return (
    <>
      <Head>
        <link rel="icon" href="/favicon.ico"/>
        <title>InfoMaision</title>
      </Head>
      <Mainframe>
        <Component {...pageProps} />
      </Mainframe>
    </>
  )
}

export default MyApp
