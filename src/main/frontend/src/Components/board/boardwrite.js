import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import header from "../app/Header";
import {Alert, SvgIcon} from "@mui/joy";
import {Card, CardContent, Grid, Input, styled} from "@mui/material";
import Textarea from "@mui/joy/Textarea";
import Button from "@mui/joy/Button";
import * as PropTypes from "prop-types";
const variant = "outlined"; // or any other variant you want to use
const VisuallyHiddenInput = styled('input')`
    clip: rect(0 0 0 0);
    clip-path: inset(50%);
    height: 1px;
    overflow: hidden;
    position: absolute;
    bottom: 0;
    left: 0;
    white-space: nowrap;
    width: 1px;
`;

VisuallyHiddenInput.propTypes = {type: PropTypes.string};
function SuccessAlert() {
    return (<Alert variant="filled" severity="success">
        새로운 게시글 성공했습니다.
    </Alert>);
}
function ErrorAlert(){
    return (<Alert variant="filled" severity="error">
        게시글 등록 실패
    </Alert>);
}
function Boardwrite(){
    const [title, setTitle] = useState("");
    const [content,setContent] = useState("");
    const navigate = useNavigate();
    const changeTitle =(event) =>{
        setTitle(event.target.value);
    };
    const changeContent =(event) =>{
        setContent(event.target.value);
    };
    const createBoard = async () =>{
        const req ={
            title : title,
            content : content
        };
        await axios
            .post("http://localhost:8080/board/write", req)
            .then((resp) => {
                console.log(("success"));
                console.log(resp.data);
                const boardId = resp.data.boardId;
                console.log("boardId", boardId);
                SuccessAlert();
            }).catch((err) => {
                ErrorAlert();
                console.log(err);
            });
        return(
            <Grid item xs={3}>
            <Card>
                <CardContent className="p-4">
                    <div className="border p-4 mb-4">
                        <form>
                            <p><Input type="text" className="form-control" value={title} onChange={changeTitle} size="50px"  placeholder="제목을 입력하세요"/></p>
                            <p><Textarea  type="text" className="form-control" value={content} onChange={changeContent} rows="10" minRows={5} placeholder="본문을 입력하세요" variant="soft"/></p>
                        </form>
                        <Button
                            component="label"
                            role={undefined}
                            tabIndex={-1}
                            variant="outlined"
                            color="neutral"
                            startDecorator={
                                <SvgIcon>
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                        strokeWidth={1.5}
                                        stroke="currentColor"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            d="M12 16.5V9.75m0 0l3 3m-3-3l-3 3M6.75 19.5a4.5 4.5 0 01-1.41-8.775 5.25 5.25 0 0110.233-2.33 3 3 0 013.758 3.848A3.752 3.752 0 0118 19.5H6.75z"
                                        />
                                    </svg>
                                </SvgIcon>
                            }
                        >
                            Upload a file
                            <VisuallyHiddenInput type="file"/>
                        </Button>
                    </div>
                    <div className="flex justify-between">
                        <Button type="Sumbit" size="md" variant={variant} color="success"  onClick={createBoard}>저장</Button>
                        <Button size="md" variant={variant} color="danger">취소</Button>
                    </div>
                </CardContent>
            </Card>
            </Grid>
        );
    }
}

export default Boardwrite;