import { useEffect } from "react";
import {useAuth} from "../file/AuthProvider";
import axios from "axios";

const MyComponent = () => {
    const { jwtToken } = useAuth(); // JWT 토큰을 컨텍스트로부터 받아옵니다.

    useEffect(async () => {
        try {
            const token = localStorage.getItem('jwtToken'); // 로컬 스토리지에서 토큰 가져오기
            const response = await axios.get('/user/status', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            console.log(response.data); // 로그인한 사용자 정보 확인
        } catch (error) {
            console.error(error.response.data); // 에러 발생시 출력
        }
    })
}

export default MyComponent;
