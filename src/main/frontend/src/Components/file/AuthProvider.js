import { createContext, useState } from "react";

const defaultAuthValue = {
    auth: localStorage.getItem("accessToken"), // 초기 상태를 localStorage에서 가져온 id로 설정
    setAuth: () => {}  // 빈 함수로 초기화
};

export const AuthContext = createContext(defaultAuthValue);

function AuthProvider({ children }) {

    const [auth, setAuth] = useState(localStorage.getItem("accessToken") || null);

    const value = {auth, setAuth };

    return (
        <AuthContext.Provider value = {value}>
            {children}
        </AuthContext.Provider>
    );

}

export default AuthProvider;