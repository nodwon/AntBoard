import * as React from 'react';
import Footer from "./Components/app/Footer"
import Header from "./Components/app/Header"
import Main from "./Components/app/Main";
import * as ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";

import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import "./index.css";
import ErrorPage from "./Components/router/ErrorPage";
import BoardDetail from "./Components/board/BoardDetail";
import BoardUpdate from "./Components/board/BoardUpdate";
const router = createBrowserRouter([
    {
        path: "/",
        element: <div>  <Header/>
            <div className="container">

                <Main/>
            </div>
            <Footer/></div>,
        errorElement : <ErrorPage/>
    },
    {
        path: "/board/:boardId",
        element: <BoardDetail/>,
        errorElement : <ErrorPage/>
    },
    {
        path: "/board/:boardId/edit",
        element: <BoardUpdate/>,
        errorElement : <ErrorPage/>
    }
]);
function App() {




    return (
        <div>
            <React.StrictMode>
                <RouterProvider router={router} />
            </React.StrictMode>
            <BrowserRouter>
            </BrowserRouter>
        </div>

    );
}

export default App;
