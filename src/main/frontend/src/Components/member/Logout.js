import { useEffect, useContext } from "react";
import {useNavigate} from "react-router-dom";
import {removeCookie} from "./Login";
function Logout() {



    const logout = () => {
        const logout = removeCookie("token");
        const navigate = useNavigate();

        alert(auth + "님, 성공적으로 로그아웃 됐습니다 🔒");

        navigate("/");
    };

    useEffect(() => {
        logout();
    }, []);

}

export default Logout;