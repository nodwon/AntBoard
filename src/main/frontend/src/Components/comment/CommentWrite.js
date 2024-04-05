import axios from "axios";
import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, TextField, Grid, Box } from "@mui/material";
import SendIcon from '@mui/icons-material/Send';

function CommentWrite() {
    const { boardId } = useParams();
    let userId = null;
    // const isLoggedIn = false;

    const cookie = document.cookie.split('; ').find(row => row.startsWith('id='));
    if (cookie) {
        userId = cookie.split('=')[1];
    }

    const [content, setContent] = useState("");
    const navigate = useNavigate();

    const changeContent = (event) => {
        setContent(event.target.value);
    };

    const createComment = async () => {
        const req = {
            content: content,
            // Assuming userId is intended to be part of the request body
            userId: userId
        };

        try {
            const resp = await axios.post(`http://localhost:8080/board/${boardId}/comment/write`, req);
            console.log("[CommentWrite.js] createComment() success :D", resp.data);
            alert("댓글을 성공적으로 등록했습니다 :D");
            navigate(0); // Refresh the page to show the new comment
        } catch (err) {
            console.error("[CommentWrite.js] createComment() error :<", err);
        }
    };

    return (
        <Box sx={{ width: '100%', my: 2 }}>
            <Grid container spacing={2} alignItems="center">
                <Grid item xs={10}>
                    <TextField
                        fullWidth
                        multiline
                        rows={1}
                        value={content}
                        onChange={changeContent}
                        variant="outlined"
                        placeholder="Write a comment..."
                    />
                </Grid>
                <Grid item xs={2} display="flex" justifyContent="flex-end">
                    <Button
                        variant="contained"
                        endIcon={<SendIcon />}
                        onClick={createComment}
                    >
                        Add
                    </Button>
                </Grid>
            </Grid>
        </Box>
    );
}

export default CommentWrite;
