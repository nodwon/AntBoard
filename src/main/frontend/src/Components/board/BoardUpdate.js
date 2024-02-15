import axios from "axios";
import * as React from 'react';
import {Card, CardContent, Input, styled} from "@mui/material";
import Textarea from "@mui/joy/Textarea";
import Button from "@mui/joy/Button";
import {SvgIcon} from "@mui/joy";
import Header from "../app/Header";
import Footer from "../app/Footer";
import {useEffect, useState} from "react";
import * as PropTypes from "prop-types";
import {useLocation, useNavigate, useParams} from "react-router-dom";

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

function BoardUpdate() {
    const { boardId } = useParams(); // useParams 사용
    const [board, setBoard] = useState({});
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();

    const changeTitle = (event) => {
        setTitle(event.target.value);
    }
    const changeContent = (event) => {
        setContent(event.target.value);
    }

    const handleCancel = () => {
        // 홈 경로로 이동
        navigate('/');
    };
    useEffect(() => {
        axios.get(`http://localhost:8080/board/${boardId}`)
            .then(response => {
                setBoard(response.data);
                setTitle(response.data.title);
                setContent(response.data.content);
            })
            .catch(error => console.error(error));
    }, [boardId]);
    const updateBoard = async () => {
        const req = {
            title: title,
            content: content
        }
        await axios.post(`http://localhost:8080/board/${boardId}/edit`, req)
            .then((response) => {
                console.log(response);
                handleCancel();
            })
            .catch((err) => {
                console.log(err);
            })
    }
    return (
        <div>
        <Header/>
            <div className="py-4">
                <div className="container mx-auto">
                    <Card>
                        <CardContent className="p-4">
                            <div className="border p-4 mb-4">
                                <form>
                                    <p><Input type="text" className="form-control" value={title} onChange={changeTitle}
                                              size="50px"/></p>
                                    <p><Textarea type="text" className="form-control" value={content} onChange={changeContent}
                                                 rows="10" minRows={5} variant="soft"/></p>
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
                                    {/*<VisuallyHiddenInput type="file"/>*/}
                                </Button>
                            </div>
                            <div className="flex justify-between">
                                <Button type="Sumbit" size="md" variant={variant} color="success"
                                        onClick={updateBoard}>수정하기</Button>
                                <Button size="md" variant={variant} color="danger"  onClick={handleCancel}>취소</Button>
                            </div>
                        </CardContent>
                    </Card>
                </div>
            </div>
            <Footer/>
        </div>
)

}
export default BoardUpdate;
