import axios from 'axios';
const HOST = process.env.NEXT_PUBLIC_ENV_HOST;
const PORT = process.env.NEXT_PUBLIC_ENV_PORT;
const BASE_URL = `${HOST}:${PORT}`;

export default axios.create({
  baseURL: BASE_URL,
});

export const axiosPrivate = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true,
});
