import axios from "axios";
import * as React from 'react';
import { useEffect, useState } from 'react';
import { Link } from "@mui/joy";
import Header from "../app/Header";
import Footer from "../app/Footer";
import { useParams } from "react-router-dom"; // useParams 추가

function boardDetail() {
    const [getboard, setgetboard] = useState({});
    const { boardId } = useParams(); // useParams 사용

    const getBoardDetail = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/board/${boardId}`); // 백틱 사용
            console.log(response.data);
            setgetboard(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    const deleteBoard = async () => {
        try {
            const response = await axios.delete(`http://localhost:8080/board/${boardId}/delete`); // 백틱 사용

            console.log(response.data);

            if (response.status === 200) {
                alert("게시글을 성공적으로 삭제했습니다 :D");
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
        <div>
            <Header/>

            <div className="bbs-detail-container">
                <div>

                    <div className="my-3 d-flex justify-content-end">
                        <Link className="btn btn-outline-secondary" to="/"><i className="fas fa-list">
                        </i> 글목록</Link> &nbsp;

                        <Link className="btn btn-outline-secondary" to={{pathname: `/${getboard.boardId}`}}
                              state={{parentBoard: parentBoard}}>
                            <i className="fas fa-pen"></i> 답글쓰기</Link> &nbsp;
                        <Link className="btn btn-outline-secondary" to="/board/edit" state={{update: updateBoard}}><i
                            className="fas fa-edit"></i> 수정</Link> &nbsp;
                        <button className="btn btn-outline-danger" onClick={deleteBoard}><i
                            className="fas fa-trash-alt"></i> 삭제
                        </button>


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
