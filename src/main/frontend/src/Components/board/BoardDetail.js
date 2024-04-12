import axios from "axios";
import React, {useContext, useEffect, useState} from 'react';
import Header from "../app/Header";
import Footer from "../app/Footer";
import { useNavigate, useParams } from "react-router-dom";
import "../../css/BoardDetail.css";
import { AspectRatio, Box } from "@mui/joy";
import { Button, Card, CardContent, Typography, Stack } from '@mui/material';
import CommentList from "../comment/CommentList";
import CommentWrite from "../comment/CommentWrite";
import {AuthContext} from "../file/AuthProvider";

function BoardDetail() {
    const [boardDetails, setBoardDetails] = useState({
        boardId: null,
        title: '',
        content: '',
        createdDate: '',
        modifiedDate: '',
        files: [],
    });
    const { boardId } = useParams();
    const navigate = useNavigate();
    const { auth, setAuth } = useContext(AuthContext);

    const getBoardDetail = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/board/${boardId}`, { responseType: 'blob' });
            if (response.headers['content-type']?.includes('application/json')) {
                setBoardDetails(response.data);
            } else {
                throw new Error('Invalid content type');
            }
        } catch (error) {
            console.error("Failed to fetch board details:", error);
            // Handle the error according to the content type or error message
            if (error.response && error.response.headers['content-type']?.includes('text/html')) {
                // This may indicate that an HTML error page was returned, possibly due to a server error
            }
            // Add any additional error handling logic here
        }
    };

    const deleteBoard = async () => {
        try {
            await axios.delete(`http://localhost:8080/board/${boardId}/delete`);
            alert("게시글을 성공적으로 삭제했습니다.");
            navigate('/');
        } catch (error) {
            console.error("Failed to delete board:", error);
        }
    };

    useEffect(() => {
        getBoardDetail();
    }, [boardId]);

    return (
        <div id="boardDetail" className="board-detail">
            <Header/>
            <div className="mul-card">
                <Card className="board-card">
                    <div>
                        <Typography variant="h6">{boardDetails.title}</Typography>
                        <Typography variant="body1">{boardDetails.createdDate}</Typography>
                    </div>
                    <Box className="aspectRatioBox">
                        {boardDetails.files?.map((file, index) => (
                            <AspectRatio key={index} minHeight="60px" maxHeight="100px" sx={{ maxWidth: '50%' }}>
                                <img src={file.imageBase64Data} alt={`Attachment ${index + 1}`} className="aspectRatioImg"/>
                            </AspectRatio>
                        ))}
                    </Box>

                    <CardContent>
                        <Stack direction="row" spacing={1} mb={2}>
                            <Button variant="outlined" onClick={() => navigate('/')}>글목록</Button>
                            <Button variant="outlined" onClick={() => navigate(`/board/${boardDetails?.boardId}/edit`)}>수정</Button>
                            <Button variant="outlined" color="error" onClick={deleteBoard}>삭제</Button>
                        </Stack>
                        {auth  ? (
                        <CommentList boardId={boardId}/>
                            ):null
                        }
                        <CommentWrite boardId={boardId}/>
                    </CardContent>
                </Card>
            </div>
            <Footer/>
        </div>
    );
}

export default BoardDetail;
