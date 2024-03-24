import * as React from 'react';
import Footer from "./Components/app/Footer"
import Header from "./Components/app/Header"
import Main from "./Components/app/Main";
import {BrowserRouter, createBrowserRouter, RouterProvider} from "react-router-dom";
import "./index.css";
import ErrorPage from "./Components/router/ErrorPage";
import BoardDetail from "./Components/board/BoardDetail";
import BoardUpdate from "./Components/board/BoardUpdate";
import Login from "./Components/member/Login";
import SignUP from "./Components/member/Register";
import Register from "./Components/member/Register";

const router = createBrowserRouter([
    {
        path: "/",
        element: <div><Header/>
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
        <div>

            <React.StrictMode>
                <RouterProvider router={router}/>
            </React.StrictMode>
            <BrowserRouter>
            </BrowserRouter>

        </div>

    );
}

export default App;
