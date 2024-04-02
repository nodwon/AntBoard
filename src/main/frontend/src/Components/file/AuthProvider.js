import React, { createContext, useContext, useState } from 'react';

// createContext를 사용하여 인증 상태를 관리할 Context 생성
const AuthContext = createContext();

// 인증 관련 로직을 처리하는 AuthProvider 컴포넌트
export const AuthProvider = ({ children }) => {
    const [jwtToken, setJwtToken] = useState(null);

    const login = (token) => {
        setJwtToken(token);
    };

    const logout = () => {
        setJwtToken(null);
    };

    return (
        <AuthContext.Provider value={{ jwtToken, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

// useContext를 사용하여 AuthContext를 사용할 수 있는 hook 생성
export const useAuth = () => useContext(AuthContext);
