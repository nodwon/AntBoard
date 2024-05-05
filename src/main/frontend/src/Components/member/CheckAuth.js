import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { AuthContext } from "../file/AuthProvider";

const CheckAuth = () => {
    const { setAuth } = useContext(AuthContext);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const checkAuthentication = async () => {
            try {
                await axios.get("/check", { withCredentials: true });
                setAuth(true); // 로그인됨
            } catch (err) {
                setAuth(false); // 로그인되지 않음
            } finally {
                setLoading(false);
            }
        };

        checkAuthentication();
    }, [setAuth]);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    return (
        <div>
            {auth ? <p>환영합니다!</p> : <p>로그인해주세요.</p>}
        </div>
    );
};

export default CheckAuth;