import { useEffect, useContext } from "react";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../file/AuthProvider";
function Logout() {
    const { auth, setAuth } = useContext(AuthContext);
    const logout = () => {
        const navigate = useNavigate();

        alert(auth + "님, 성공적으로 로그아웃 됐습니다 🔒");

        navigate("/");
    };

    useEffect(() => {
        logout();
    }, []);

}

export default Logout;