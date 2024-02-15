import {useNavigate, useRouteError} from "react-router-dom";

export default function ErrorPage() {
    const navigate = useNavigate();

    const error = useRouteError(); // 에러 발생 여부를 가져옵니다.
    console.error(error);

    return (
        <div id="error-page">
            <h1  onClick={() => navigate('/')}>Oops!</h1>
            <h3  onClick={() => navigate('/')}>home</h3>
            <p>Sorry, an unexpected error has occurred.</p>
            <p>
                <i>{error.statusText || error.message}</i>
            </p>
        </div>
    );
}