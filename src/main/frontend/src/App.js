import * as React from 'react';
import Footer from "./Components/app/Footer"
import Header from "./Components/app/Header"
import Main from "./Components/app/Main";
import { createBrowserRouter, RouterProvider} from "react-router-dom";
import "./index.css";
import ErrorPage from "./Components/router/ErrorPage";
import BoardDetail from "./Components/board/BoardDetail";
import BoardUpdate from "./Components/board/BoardUpdate";
import Login from "./Components/member/Login";
import Register from "./Components/member/Register";
import AuthProvider from "./Components/file/AuthProvider";
import HttpHeadersProvider from "./Components/file/HttpHeadersProvider";

const router = createBrowserRouter([
    {
        path: "/",
        element: <div>
            <Header/>
            <div className="container">
                <Main/>
            </div>
            <Footer/></div>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/board/:boardId",
        element: <BoardDetail/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/board/:boardId/edit",
        element: <BoardUpdate/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/login",
        element: <Login/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/register",
        element: <Register/>,
        errorElement: <ErrorPage/>
    }
]);


function App() {

    return (
        <React.StrictMode>
            <HttpHeadersProvider>

            <AuthProvider>
                    <RouterProvider router={router} />
            </AuthProvider>
            </HttpHeadersProvider>
        </React.StrictMode>

    );
}

export default App;
