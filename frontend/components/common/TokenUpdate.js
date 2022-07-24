import { tokenState } from '../../state/token';
import { Axios } from 'axios';
import { Cookies, Cookies } from 'react-cookie';
import moment from 'moment';

export default function TokenUpdate() {
  const [token, setToken] = useRecoilState(tokenState);
  const isToken = !!token;
  const isValid = false;

  if (isToken) {
    const expiresAt = new Date(token['expiresAt']);
    const now = new Date();
    const diffTime = now - expiresAt;
    const isValid = diffTime >= 10000;
  }

  if (!isValid) {
    const getToken = async () => {
      try {
        const { res } = await Axios({
          url: 'http://localhost:8080/api/v1/auth/reissue',
          method: 'post',
          withCredentials: true,
        });
        const accessToken = res.data.accessToken;
        const expiresAt = res.data.expirestAt;
        const [cookie] = res.headers['set-cookie'];
        cookies.set(JSON.stringify(cookie));
        setToken({
          accessToken: accessToken,
          expiresAt: expiresAt,
        });
      } catch {}
    };
  }
}
