import axios from "axios";
import * as React from 'react';
import {useEffect, useState} from 'react';
import {Card, CardContent, Input, styled} from "@mui/material";
import Textarea from "@mui/joy/Textarea";
import Button from "@mui/joy/Button";
import {SvgIcon} from "@mui/joy";
import Header from "../app/Header";
import Footer from "../app/Footer";
import * as PropTypes from "prop-types";
import {useNavigate, useParams} from "react-router-dom";

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
    const {boardId} = useParams(); // useParams 사용
    const [board, setBoard] = useState({});
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();
    const [files, setFiles] = useState([]);
    const [severFiles, setSeverFiles ] = useState(board.files || []);
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
    const handleChangeFile = (event) => {
        // 총 5개까지만 허용
        const selectedFiles = Array.from(event.target.files).slice(0, 5);
        setFiles((prevFiles) => [...prevFiles, ...selectedFiles]);
    };

    const handleRemoveFile = (index) => {
        setFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
    };

    const handleRemoveSeverFile = (index, boardId, fileId) => {
        setSeverFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
        fileDelete(boardId, fileId);
    }

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
                fileUpload(boardId);
                handleCancel();
            })
            .catch((err) => {
                console.log(err);
            })
    }
    /* 파일 업로드 */
    const fileUpload = async (boardId) => {
        console.log("업로드할 파일 목록:", files);
        // 파일 데이터 저장
        const fd = new FormData();
        files.forEach((file) => fd.append(`file`, file));

        await axios.post(`http://localhost:8080/board/${boardId}/file/upload`, fd)
            .then((resp) => {
                console.log("[file.js] fileUpload() success :D");
                console.log(resp.data);
                alert("게시물과 파일을 성공적으로 수정했습니다. :D");

                // 새롭게 등록한 글 상세로 이동
                navigate(`/board/${boardId}`);
            })
            .catch((err) => {
                console.log("[FileData.js] fileUpload() error :<");
                console.log(err);
            });
    };
    /* 파일 삭제 */
    const fileDelete = async (boardId, fileId) => {
        try {
            await axios.delete(`http://localhost:8080/board/${boardId}/file/delete?fileId=${fileId}`);
            console.log(" fileDelete() success :D");
            alert("파일 삭제 성공 :D");
        } catch (error) {
            console.error(" fileDelete() error :<");
            console.error(error);
        }
    };

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
                                    <p><Textarea type="text" className="form-control" value={content}
                                                 onChange={changeContent}
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
                                    <VisuallyHiddenInput type="file" onChange={handleChangeFile} multiple/>
                                </Button>
                                <div>
                                    <h4 className="table-primary">파일</h4>
                                    <div>
                                        {severFiles.length > 0 || files.length > 0 ? (
                                            <div className='file-box'>
                                                <ul>
                                                    {/* 기존의 파일 데이터, 삭제 로직 */}
                                                    {severFiles.map((file, index) => (
                                                        <li key={file.fileId} style={{
                                                            display: 'flex',
                                                            justifyContent: 'space-between',
                                                            alignItems: 'center'
                                                        }}>
										<span>
											<strong>File Name:</strong> {file.originFileName} &nbsp;
                                            <button className="delete-button" type="button"
                                                    onClick={() => handleRemoveSeverFile(index, boardId, file.fileId)}>
												x
											</button>
										</span>
                                                        </li>
                                                    ))}
                                                    {/* 새로운 파일을 저장할 때 */}
                                                    {files.map((file, index) => (
                                                        <li key={file.fileId} style={{
                                                            display: 'flex',
                                                            justifyContent: 'space-between',
                                                            alignItems: 'center'
                                                        }}>
									<span>
										<strong>File Name:</strong> {file.name} &nbsp;
                                        <button className="delete-button" type="button"
                                                onClick={() => handleRemoveFile(index)}>
											x
										</button>
									</span>
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        ) : (
                                            <div className='file-box'>
                                                <p>No files</p>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>
                            <div className="flex justify-between">
                                <Button type="Sumbit" size="md" variant={variant} color="success"
                                        onClick={updateBoard}>수정하기</Button>
                                <Button size="md" variant={variant} color="danger" onClick={handleCancel}>취소</Button>
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
