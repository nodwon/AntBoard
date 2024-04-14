import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { Pagination, Stack, Typography, Paper, Avatar, Box } from '@mui/material';
import { deepPurple } from '@mui/material/colors';
import {useNavigate} from "react-router-dom";

function CommentList(props) {
    const boardId = props.boardId;
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(5);
    const [totalCnt, setTotalCnt] = useState(0);
    const [commentList, setCommentList] = useState([]);
    const getCommentListRef = useRef(null);
    const navigate = useNavigate();

    const changePage = (newPage) => {
        setPage(newPage);
        getCommentList(newPage);
        getCommentListRef.current(newPage);
    };
    const getInitials = (name) => {
        return name ? name.charAt(0).toUpperCase() : '?';
    };

    const getCommentList = async (newPage) => {
        await axios
            .get(`http://localhost:8080/board/${boardId}/comment/list`, { params: { page: newPage - 1 } })
            .then((resp) => {
                setPageSize(resp.data.pageSize);
                setTotalPages(resp.data.totalPages);
                setTotalCnt(resp.data.totalElements);
                setCommentList(resp.data.content);
            })
            navigate('/')
            .catch((err) => {
                console.error('[CommentList.js] getCommentList() error:', err);
            });
    };

    useEffect(() => {
        getCommentListRef.current = getCommentList;
        getCommentList(1);
    }, [boardId]);

    return (
        <>
            {commentList.length > 0 ? (
                <>
                    {commentList.map((comment, idx) => (
                        <Paper
                            key={idx}
                            elevation={3}
                            sx={{
                                margin: '20px',
                                padding: '20px',
                                borderRadius: '12px',
                                display: 'flex',
                                alignItems: 'center',
                            }}
                        >
                            <Avatar sx={{ bgcolor: deepPurple[500] }}>
                                {comment.commentWriterName ? comment.commentWriterName.charAt(0) : '?'} // This will display the name
                            </Avatar>
                            <Box sx={{ marginLeft: '20px' }}>
                                <Typography variant="subtitle2" component="div" sx={{ fontWeight: 'bold' }}>
                                    {comment.writerName}
                                </Typography>
                                <Typography variant="body2" component="div">
                                    {comment.content}
                                </Typography>
                            </Box>
                        </Paper>
                    ))}
                    <Stack spacing={2} direction="row" justifyContent="center">
                        <Pagination
                            count={totalPages}
                            page={page}
                            onChange={(event, newPage) => changePage(newPage)}
                            variant="outlined"
                            shape="rounded"
                        />
                    </Stack>
                </>
            ) : (
                <Typography textAlign="center">No comments yet.</Typography>
            )}
        </>
    );
}

export default CommentList;
