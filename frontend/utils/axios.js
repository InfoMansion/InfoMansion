import axios from 'axios';
import moment from 'moment';

const HOST = process.env.NEXT_PUBLIC_ENV_HOST;
const PORT = process.env.NEXT_PUBLIC_ENV_PORT;
const BASE_URL = `${HOST}:${PORT}`;

const axiosApiInstance = axios.create({
  baseURL: BASE_URL,
});

axiosApiInstance.interceptors.request.use(
  async config => {
    if (!config.headers.Authorization) return config;

    const expireAt = new Date(localStorage.getItem('expiresAt'));
    const diffTime = moment().diff(expireAt, 'minutes');

    if (diffTime >= -5) {
      try {
        const response = await axios.post(
          BASE_URL + '/api/v1/auth/reissue',
          { accessToken: config.headers.Authorization.split(' ')[1] },
          {
            withCredentials: true,
          },
        );
        localStorage.setItem('expiresAt', response.data.data.expiresAt);
      } catch (e) {
        alert('세션이 만료됐습니다.');
        console.log(e);
        window.location.replace('/');
      }
    }
    return config;
  },
  error => {
    Promise.reject(error);
  },
);

axiosApiInstance.interceptors.response.use(
  response => response,
  // async function (error) {
  //   if (error.response.status === 401 && !originalRequest._retry) {
  //     alert('세션이 만료됐습니다.');
  //     window.location.replace('/');
  //   }
  // },
);

export default axiosApiInstance;
