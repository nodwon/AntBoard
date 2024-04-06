import React, { createContext, useContext, useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import axios from 'axios';

// 컨텍스트 생성
const AuthContext = createContext(null);

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // 인증 상태를 확인하는 함수
    const checkAuthStatus = async () => {
        try {
            // 예제로 '/api/auth/status' 경로를 사용했지만, 실제 경로에 맞게 조정해야 함
            const response = await axios.get('/user/status', {
                withCredentials: true // 쿠키를 포함시키기 위한 옵션
            });
            if (response.status === 200) {
                setIsLoggedIn(true);
            } else {
                setIsLoggedIn(false);
            }
        } catch (error) {
            setIsLoggedIn(false);
        }
    };

    useEffect(() => {
        checkAuthStatus();
    }, []);

    // 로그아웃 함수
    const logout = () => {
        Cookies.remove('authToken');
        setIsLoggedIn(false);
        // 로그아웃 처리를 위한 추가적인 로직 구현
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
