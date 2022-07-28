import { createContext, useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';

const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
  const [cookies] = useCookies(['cookie-name']);
  const [auth, setAuth] = useState();

  useEffect(() => {
    const accessToken = cookies['InfoMansionAccessToken'];
    if (accessToken) {
      setAuth({ isAuthorized: true, accessToken });
    } else {
      setAuth({});
    }
  }, [cookies]);

  return (
    <AuthContext.Provider value={{ auth, setAuth }}>
      {auth !== undefined && children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
