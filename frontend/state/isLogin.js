import { atom } from 'recoil';

export const isLoginState = atom({
  key: 'tokenState',
  default: false,
});
