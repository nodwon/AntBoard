import axios from "axios";
import * as React from 'react';
import { useEffect, useState } from 'react';
import { Link } from "@mui/joy";
import Header from "../app/Header";
import Footer from "../app/Footer";
import {useNavigate, useParams} from "react-router-dom";
import Router from "../router/Router"; // useParams 추가
import "../../css/BoardDetail.css"

function BoardDetail() {
    const [getboard, setgetboard] = useState({});
    const { boardId } = useParams(); // useParams 사용
    const navigate = useNavigate();

    const getBoardDetail = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/board/${boardId}`);
            console.log(response.data);
            setgetboard(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    const deleteBoard = async () => {
        try {
            const response = await axios.delete(`http://localhost:8080/board/${boardId}/delete`);

            console.log(response.data);

            if (response.status === 200) {
                alert("게시글을 성공적으로 삭제했습니다 :D");
                navigate('/');
            }
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        getBoardDetail();
    }, []);

    // updateBoard와 parentBoard의 값 가져오는 부분 이동
    const updateBoard = {
        boardId: getboard.boardId,
        title: getboard.title,
        content: getboard.content,
        CreatedDate: getboard.createdDate,
        ModifiedDate: getboard.modifiedDate
    };
    const parentBoard = {
        boardId: getboard.boardId,
        title: getboard.title,
    };

    return (
        <div id="boardDetail" className="board-detail">
            <Header/>

            <div className="mul-card">
                <div className="mul-card-header">
                    <h5 className="mul-card-title">{getboard.title}</h5>
                </div>
                <div className="mul-card-body">
                    <table className="table table-striped">
                        <tbody>
                        <tr>
                            <th>작성자</th>
                            <td>{getboard.author}</td>
                        </tr>
                        <tr>
                            <th>작성일</th>
                            <td>{getboard.createDate}</td>
                        </tr>
                        <tr>
                            <th>내용</th>
                            <td>{getboard.content}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div className="mul-card-footer">
                    <div className="btn-group">
                        <button className="btn btn-outline-secondary" onClick={() => navigate('/')}>
                            <i className="fas fa-list"></i> 글목록
                        </button>
                        <button className="btn btn-outline-secondary" onClick={() => navigate({
                            pathname: `/${getboard.boardId}`,
                            state: {parentBoard: parentBoard}
                        })}>
                            <i className="fas fa-pen"></i> 답글쓰기
                        </button>
                        <button className="btn btn-outline-secondary"
                                onClick={() => navigate(`/board/${getboard?.boardId}/edit`, {state: {update: updateBoard}})}>
                            <i className="fas fa-edit"></i> 수정
                        </button>
                        <button className="btn btn-outline-danger" onClick={deleteBoard}>
                            <i className="fas fa-trash-alt"></i> 삭제
                        </button>
                    </div>
                </div>

            </div>
            <Footer/>
        </div>
    );

}

export default BoardDetail;
