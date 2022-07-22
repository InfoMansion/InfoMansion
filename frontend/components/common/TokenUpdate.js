import { tokenState } from '../../state/token';
import { Axios } from 'axios';
import { Cookies, Cookies } from 'react-cookie';
import moment from 'moment';

export default function TokenUpdate() {
  const [token, setToken] = useRecoilState(tokenState);

  if (token) {
    const expiredAt = this.$moment.utc(token['expiresAt']);
    const diffTime = this.$moment.duration(expiredAt.diff(this.$moment()));
    if (diffTime < 10000) {
      Axios({
        url: 'http://localhost:8080/api/v1/auth/reissue',
        method: 'post',
        withCredentials: true,
      }).then(res => {
        const accessToken = res.data.accessToken;
        const expiresAt = res.data.expirestAt;
        setToken({ accessToken: accessToken, expiresAt: expiresAt });
      });
    } else {
      Axios({
        url: 'http://localhost:8080/api/v1/auth/reissue',
        method: 'post',
        withCredentials: true,
      }).then(res => {
        const accessToken = res.data.accessToken;
        const expiresAt = res.data.expirestAt;
        setToken({ accessToken: accessToken, expiresAt: expiresAt });
      });
    }
  }
}
