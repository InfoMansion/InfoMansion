import axios from '../utils/axios';
import useAuth from './useAuth';
import { useCookies } from 'react-cookie';

const useRefreshToken = () => {
  const { setAuth } = useAuth();
  const [cookies] = useCookies(['cookie-name']);

  const refresh = async () => {
    try {
      const response = await axios.post(
        '/api/v1/auth/reissue',
        { accessToken: cookies['InfoMansionAccessToken'] },
        {
          withCredentials: true,
        },
      );
      console.log('res : ', response);
      setAuth(prev => {
        console.log(JSON.stringify(prev));
        console.log(response.data.data.accessToken);
        return { ...prev, accessToken: response.data.data.accessToken };
      });
      return response.data.data.accessToken;
    } catch (e) {
      console.log(e);
      return undefined;
    }
  };
  return refresh;
};

export default useRefreshToken;
