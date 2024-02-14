import { useRouteError } from "react-router-dom";

export default function ErrorPage() {
    const error = useRouteError(); // 에러 발생 여부를 가져옵니다.
    console.error(error);

    return (
        <div id="error-page">
            <h1>Oops!</h1>
            <p>Sorry, an unexpected error has occurred.</p>
            <p>
                <i>{error.statusText || error.message}</i>
            </p>
        </div>
    );
}