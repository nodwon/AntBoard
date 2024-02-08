import axios from "axios";
import * as React from 'react';
import {Link} from "@mui/joy";
import Header from "../app/Header";
import Footer from "../app/Footer";
import {useState} from "react";
import {useNavigate, useParams} from "react-router-dom";

function boardDetail() {
    const {getboard, setgetboard} = useState({});
    const { boardId } = useParams(); // 파라미터 가져오기
    const navigate = useNavigate();

    const handleCancel = () => {
        // 홈 경로로 이동
        history.push('/');
    };
    const getBoardDetail = async () => {
        try {
            const response = await axios.get('http://localhost:8080/board/{boardId}');
            console.log(response.data);
            setgetboard(response.data);
        } catch (error) {
            console.error(error);
        }
    };
    const deleteBoard = async () => {
        try {
            const response = await axios.delete(`http://localhost:8080/board/{boardId}/delete`, {headers: this.headers});

            console.log("[BbsDetail.js] deleteBbs() success :D");
            console.log(response.data);

            if (response.status === 200) {
                alert("게시글을 성공적으로 삭제했습니다 :D");
                navigate("/");
            }
        } catch (error) {
            console.log("[BbsDetail.js] deleteBbs() error :<");
            console.error(error);
        }
    };
    const updateBoard = {
        boardId: getboard.boardId,
        title: getboard.title,
        content: getboard.content,
        CreatedDate : getboard.createdDate,
        ModifiedDate : getboard.modifiedDate

    };
    const parentBoard = {
        boardId: getboard.boardId,
        title: getboard.title,
    };
    return (
        <div>
            <Header/>

            <div className="bbs-detail-container">
            <div>

                <div className="my-3 d-flex justify-content-end">
                    <Link className="btn btn-outline-secondary" to="/"><i className="fas fa-list">
                    </i> 글목록</Link> &nbsp;

                    <Link className="btn btn-outline-secondary" to={{pathname: `//${getboard.boardId}` }} state={{ parentBoard: parentBoard }}>
                        <i className="fas fa-pen"></i> 답글쓰기</Link> &nbsp;


                </div>

                <table className="table table-striped">
                    <tbody>

                    <tr>
                        <th>제목</th>
                        <td>
                            <span>{getboard.title}</span>
                        </td>
                    </tr>

                    <tr>
                        <th>작성일</th>
                        <td>
                            <span>{getboard.createDate}</span>
                        </td>
                    </tr>

                    <tr>
                        <th>내용</th>
                        <td></td>
                    </tr>
                    </tbody>
                </table>

                <div className="content-box">{getboard.content}</div>
            </div>
        </div>


            <Footer/>
        </div>
    );

}
export default boardDetail;