import {Route, Routes} from "react-router-dom";
import Home from "../app/Main"
import BoardDetail from "../board/BoardDetail";
import BoardUpdate from "../board/BoardUpdate";
import Main from "../app/Main";
function Router() {

    return (
        <Routes>
            <Route path="/" element={<Main />}></Route>
            <Route path="/board/:boardId" element={<BoardDetail />}></Route>
            <Route path="/board/edit" element={<BoardUpdate />}></Route>

        </Routes>
    );
}

export default Router;