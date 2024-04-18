import React, {useContext, useEffect, useState} from "react";
import Button from '@mui/joy/Button';
import {Card, CardContent, Grid, Input, Pagination, styled, Typography} from '@mui/material'; // Import Card and Typography components
import "../../css/home.css";
import * as PropTypes from "prop-types";
import {Stack, SvgIcon} from "@mui/joy";
import axios from "axios";
import Textarea from "@mui/joy/Textarea";
import {useNavigate, useParams} from "react-router-dom";
import {AuthContext} from "../file/AuthProvider";
import {HttpHeadersContext} from "../file/HttpHeadersProvider";

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

export default function Main() {
    const {boardId} = useParams();
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();
    const [files, setFiles] = useState([]);

    //Paging
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(5);
    const [totalCnt, setTotalCnt] = useState(0);
    const [AllBoard, setBoardList] = useState([]);
    // 게시글 전체조회
    const { auth, setAuth } = useContext(AuthContext);
    const { headers, setHeaders } = useContext(HttpHeadersContext);
    let userId = null;

    const changeTitle = (event) => {
        setTitle(event.target.value);
    };

    const changeContent = (event) => {
        setContent(event.target.value);
    };
    const handleChangeFile = (event) => {
        // 총 5개까지만 허용
        const selectedFiles = Array.from(event.target.files).slice(0, 5);
        setFiles((prevFiles) => [...prevFiles, ...selectedFiles]);
    };

    const handleRemoveFile = (index) => {
        setFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
    };
    // 첫 로딩 시, 한 페이지만 가져옴
    useEffect(() => {
        BoardList(1);
        setHeaders({
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        });
    }, []);
    // 페이징 보여주기
    const changePage = async (event, value) => {
        setPage(value)
        await BoardList(value); // 페이지 파라미터를 전달
    };
    const resizeImage = (file, maxWidth) => {
        return new Promise((resolve, reject) => {
            const image = new Image();
            image.src = URL.createObjectURL(file);
            image.onload = () => {
                let width = image.width;
                let height = image.height;

                if (width > maxWidth) {
                    height = height * (maxWidth / width);
                    width = maxWidth;
                }

                const canvas = document.createElement('canvas');
                canvas.width = width;
                canvas.height = height;
                const ctx = canvas.getContext('2d');
                ctx.drawImage(image, 0, 0, width, height);
                canvas.toBlob((blob) => {
                    resolve(blob);
                }, file.type, 1); // Adjust image quality here
            };
            image.onerror = () => {
                reject(new Error('Image load error'));
            };
        });
    };
    const createBoard = async () => {
        if (!auth) {
            alert("로그인 한 사용자만 게시글을 작성할 수 있습니다!");
            return;
        }

        // Prepare the request for creating a board
        const req = {
            title: title,
            content: content
        };

        try {
            // Create a new board
            const resp = await axios.post("http://localhost:8080/board/write", req, { headers: headers });
            console.log("Board creation success:", resp.data);
            const boardId = resp.data.boardId;

            // Resize images before uploading
            const resizedImages = await Promise.all(files.map(file => resizeImage(file, 360)));

            // Prepare form data for file upload
            const fd = new FormData();
            resizedImages.forEach((resizedBlob, index) => {
                fd.append("file", resizedBlob, files[index].name); // Use the original file name
            });

            // Upload files to the newly created board
            await axios.post(`http://localhost:8080/board/${boardId}/file/upload`, fd);
            console.log("File upload success");
            alert("파일 업로드 성공 :D");

            changePage(); // Some function to change the page or update the state
        } catch (err) {
            console.error("Error during the board creation or file upload:", err);
            // alert("An error occurred. Please try again.");
        }
    };


    const BoardList = async (page) => {
        try {
            const response = await axios.get(`http://localhost:8080/board/list`, {
                params: { page: page - 1 },
            });
            // Directly use the response data since it now includes imageBase64Data
            setBoardList(response.data.content || []);
            setPageSize(response.data.pageSize);
            setTotalPages(response.data.totalPages);
            setTotalCnt(response.data.totalElements);
        } catch (error) {
            console.log(error);
            setBoardList([]);
        }
    };

    return (
        <div className="py-4">
            <div className="container mx-auto">
                <Grid container spacing={2}>
                    {/* 첫 번째 카드 (1/4 너비) */}
                    <Grid item xs={3}>
                        <Card>
                            <CardContent className="p-4">
                                <div className="border p-4 mb-4">
                                    <form>
                                        <p><Input type="text" className="form-control" value={title}
                                                  onChange={changeTitle} size="50px" placeholder="제목을 입력하세요"/></p>
                                        <p><Textarea type="text" className="form-control" value={content}
                                                     onChange={changeContent} rows="10" minRows={5}
                                                     placeholder="본문을 입력하세요" variant="soft"/></p>
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
                                                        stroke="currentColor">
                                                        <path
                                                            strokeLinecap="round"
                                                            strokeLinejoin="round"
                                                            d="M12 16.5V9.75m0 0l3 3m-3-3l-3 3M6.75 19.5a4.5 4.5 0 01-1.41-8.775 5.25 5.25 0 0110.233-2.33 3 3 0 013.758 3.848A3.752 3.752 0 0118 19.5H6.75z"
                                                        />
                                                    </svg>
                                                </SvgIcon>
                                            }>Upload a file <VisuallyHiddenInput type="file" onChange={handleChangeFile}
                                                                                 multiple/>
                                        </Button>
                                    </form>
                                    <div>
                                        {files.map((file, index) => (
                                            <div key={index} style={{display: "flex", alignItems: "center"}}>
                                                <p>
                                                    <strong>FileName:</strong> {file.name}
                                                </p>
                                                <img src={URL.createObjectURL(file)} alt="Uploaded File"
                                                     style={{width: '50px', height: '50px'}}/>
                                                <button className="delete-button" type="button"
                                                        onClick={() => handleRemoveFile(index)}>
                                                    x
                                                </button>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                                <div className="flex justify-between">
                                    <Button type="Sumbit" size="md" variant={variant} color="success"
                                            onClick={createBoard}>저장</Button>
                                    <Button size="md" variant={variant} color="danger">취소</Button>
                                </div>
                            </CardContent>
                        </Card>
                    </Grid>
                    {/* 두 번째 카드 (3/4 너비) */}
                    <Grid item xs={9}>

                        <Card>
                            <CardContent className="p-4">
                                <div className="flex justify-between items-center mb-4">
                                    <Typography variant="h5" component="h2">나의 인증</Typography>
                                </div>
                                <div className="board-list">
                                    <Grid container spacing={2}>
                                        {AllBoard.map((boardItem) => (
                                            <Grid item xs={4} key={boardItem.boardId}>
                                                <Card>
                                                    <CardContent>
                                                        <Typography variant="h6">{boardItem.title}</Typography>
                                                        <Typography variant="body2">{boardItem.content}</Typography>
                                                        {/* Iterate over imageBase64Data to display each image */}
                                                        {boardItem.imageBase64Data && boardItem.imageBase64Data[0] && (
                                                            <img src={`data:image/jpeg;base64,${boardItem.imageBase64Data[0]}`} alt="Board" style={{ width: '100%', height: 'auto' }} />
                                                        )}
                                                        <Button variant="outlined" onClick={() => navigate(`/board/${boardItem.boardId}`)}>Explore</Button>
                                                    </CardContent>
                                                </Card>
                                            </Grid>
                                        ))}
                                    </Grid>
                                </div>
                                <Stack spacing={2}>
                                    <Pagination
                                        totalPages={totalPages}
                                        // activePage={page} // 현재 페이지 번호를 activePage prop에 전달
                                        count={totalPages}
                                        // totalItemsCount={totalCnt}
                                        onChange={changePage}
                                        variant="outlined"
                                        color="secondary"
                                    />
                                </Stack>

                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </div>
        </div>
    );
}
