import axios from "axios";
import * as React from 'react';
import {useEffect, useState} from 'react';
import Header from "../app/Header";
import Footer from "../app/Footer";
import {useNavigate, useParams} from "react-router-dom";
import "../../css/BoardDetail.css"
import {AspectRatio} from "@mui/joy";
import {Card, CardContent, Typography} from "@mui/material";


function BoardDetail() {
    const [getboard, setgetboard] = useState({});
    const {boardId} = useParams(); // useParams 사용
    const navigate = useNavigate();
    const [filename, setFiles] = useState([]); // 추가: 파일 목록 상태 추가
    const [selectedFiles, setSelectedFiles] = useState([]);
    const [thumbnails, setThumbnails] = useState([]);

    const handleFileChange = (event) => {
        setSelectedFiles([...event.target.files]);
    };

    const handleUpload = async () => {
        const formData = new FormData();
        selectedFiles.forEach((file) => formData.append("file", file));

        try {
            const response = await axios.post(`http://localhost:8080/board/${boardId}/file/thumbnail`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            setThumbnails(response.data);
        } catch (error) {
            console.error("Error uploading files:", error);
        }
    };


    const getBoardDetail = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/board/${boardId}`);
            console.log(response.data);
            console.log(response.data);
            setgetboard(response.data);
            setFiles(response.data.files); // 파일 정보를 가져와 filename 상태 업데이트
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
    }, [boardId]);
    useEffect(() => {
        // 쿠키에서 액세스 토큰 가져오기
        const accessToken = document.cookie.split(';').find(cookie => cookie.trim().startsWith('accessToken='));
        if (accessToken) {
            // 액세스 토큰을 axios의 기본 헤더에 설정
            axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken.split('=')[1]}`;
        }
    }, []);
    // updateBoard와 parentBoard의 값 가져오는 부분 이동
    const updateBoard = {
        boardId: getboard.boardId,
        title: getboard.title,
        content: getboard.content,
        CreatedDate: getboard.createdDate,
        ModifiedDate: getboard.modifiedDate,
        fname: getboard.files && getboard.files.length > 0 ? getboard.files[0].file_name : ""
    };
    const parentBoard = {
        boardId: getboard.boardId,
        title: getboard.title,
    };

    return (
        <div id="boardDetail" className="board-detail">
            <Header/>
            <div className="mul-card">
                <Card className="board-card">
                    <div>
                        <Typography variant="h6">{getboard.title}</Typography>
                        <Typography variant="body1">{getboard.createdDate}</Typography>
                    </div>
                    <AspectRatio minHeight="120px" maxHeight="200px">
                        {/*{filename && filename.length > 0 ? (*/}
                        {/*    filename.map((file, index) => (*/}
                        {/*        <img key={index} src={URL.createObjectURL(file)} alt="" className="card-image"/>*/}
                        {/*    ))*/}
                        {/*) : (*/}
                        {/*    <div>No image available</div>*/}
                        {/*)}*/}
                        <div className="thumbnails">
                            {thumbnails.map((thumbnail) => (
                                <a key={thumbnail.fileId} href={`data:image/jpeg;base64,${thumbnail.base64Data}`}
                                   target="_blank" rel="noopener noreferrer">
                                    <img src={`data:image/jpeg;base64,${thumbnail.base64Data}`} alt="Thumbnail"/>
                                </a>
                            ))}
                        </div>
                    </AspectRatio>
                    <CardContent>
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
                        {/* 댓글 리스트 컴포넌트 */}
                        <CommentList boardId={boardId}/>

                        {/* 댓글 작성 컴포넌트 */}
                        {
                            (auth) ? // 로그인한 사용자만 댓글 작성 가능
                                <CommentWrite boardId={boardId}/>
                                :
                                null
                        }
                    </CardContent>
                </Card>
            </div>
            <Footer/>
        </div>
    );

}

export default BoardDetail;
