import React, { useEffect, useRef, useState } from "react";
import axios from "axios";
import { Pagination, Stack, Typography } from "@mui/material";
import Comment from "./Comment.js";

function CommentList(props) {
    const boardId = props.boardId;

    const [page, setPage] = useState(1);
    const [ setPageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(5);
    const [setTotalCnt] = useState(0);
    const [commentList, setCommentList] = useState([]);

    const getCommentListRef = useRef(null);

    const changePage = (newPage) => {
        setPage(newPage);
        getCommentList(newPage);
        getCommentListRef.current(newPage);
    }

    const getCommentList = async (newPage) => {
        await axios.get(`http://localhost:8080/board/${boardId}/comment/list`, { params: { "page": newPage - 1 } })
            .then((resp) => {
                setPageSize(resp.data.pageSize);
                setTotalPages(resp.data.totalPages);
                setTotalCnt(resp.data.totalElements);
                setCommentList(resp.data.content);
            }).catch((err) => {
                console.error("[CommentList.js] getCommentList() error:", err);
            });
    }

    useEffect(() => {
        getCommentListRef.current = getCommentList;
        getCommentList(1);
    }, [boardId]);

    return (
        <>
            {commentList.length > 0 ? (
                <>
                    {commentList.map((comment, idx) => (
                        <div className="my-5" key={idx}>
                            <Comment obj={comment} key={`comment-${idx}`} page={page} getCommentList={getCommentListRef.current} />
                        </div>
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
                <Typography textAlign="center"></Typography>
            )}
        </>
    );
}

export default CommentList;
