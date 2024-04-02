import { useEffect } from "react";
import {useAuth} from "../file/AuthProvider";

const MyComponent = () => {
    const { jwtToken } = useAuth();

    useEffect(() => {
        // JWT 토큰이 변경될 때마다 헤더에 설정
        if (jwtToken) {
            fetch(apiUrl, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwtToken}`
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Data:', data);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    }, [jwtToken]); // jwtToken이 변경될 때만 호출
};

export default MyComponent;
