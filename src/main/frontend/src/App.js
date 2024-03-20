import * as React from 'react';
import Footer from "./Components/app/Footer"
import Header from "./Components/app/Header"
import Main from "./Components/app/Main";
import {BrowserRouter, createBrowserRouter, RouterProvider} from "react-router-dom";
import "./index.css";
import ErrorPage from "./Components/router/ErrorPage";
import BoardDetail from "./Components/board/BoardDetail";
import BoardUpdate from "./Components/board/BoardUpdate";
import HttpHeadersProvider, {HttpHeadersContext} from "./context/HttpHeadersProvider";
import AuthProvider from "./context/AuthProvider";
import {useState} from "react";

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
    }
]);


function App() {


    return (
        <div>

                <React.StrictMode>
                    <RouterProvider router={router}/>
                </React.StrictMode>
                <BrowserRouter>
                    <AuthProvider>
                        <HttpHeadersProvider>
                        </HttpHeadersProvider>
                    </AuthProvider>
                </BrowserRouter>

        </div>

    );
}

export default App;
