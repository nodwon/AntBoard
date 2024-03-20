import { createContext, useState } from "react";
import axios from "axios";

export const HttpHeadersContext = createContext();

function HttpHeadersProvider({ children }) {

    const [headers, setHeaders] = useState({
        "Authorization":`Bearer ${localStorage.getItem("bbs_access_token")}` // 새로고침하면 App Context 사라지기 때문에, 초기 값은 LocalStorage 값으로 세팅
    });

    const value = {headers, setHeaders};
    axios.get('http://localhost:8080/user/chekId', {
        headers: {
            'Authorization': 'Bearer your_jwt_token_here'
        }
    })
        .then(response => {
            // 성공적으로 데이터를 받아왔을 때 처리
        })
        .catch(error => {
            // 오류 처리
            console.error('There was a problem with your axios operation:', error);
        });

    return (
        <HttpHeadersContext.Provider value = {value}>
            {children}
        </HttpHeadersContext.Provider>
    );

}

export default HttpHeadersProvider;