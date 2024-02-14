import React, {useEffect, useState} from "react";
import Button from '@mui/joy/Button';
import {Card, CardContent, Grid, Input, styled, Table, Typography,Pagination} from '@mui/material'; // Import Card and Typography components
import "../../css/home.css";
import * as PropTypes from "prop-types";
import {Alert, Link, SvgIcon} from "@mui/joy";
import axios from "axios";
import Textarea from "@mui/joy/Textarea";
import {useNavigate} from "react-router-dom";
import Router from "../router/Router";
// import Pagination from "react-js-pagination";


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
    const [title, setTitle] = useState("");
    const [content,setContent] = useState("");
    const navigate = useNavigate();


    const changeTitle =(event) =>{
        setTitle(event.target.value);
    };
    const changeContent =(event) =>{
        setContent(event.target.value);
    };
    const createBoard = async () => {
        const req = {
            title: title,
            content: content
        };
        await axios
            .post("http://localhost:8080/board/write", req)
            .then((resp) => {
                console.log(("success"));
                console.log(resp.data);
                alert("새로운게시글이 작성되었습니다.");
                navigate("/");
            }).catch((err) => {
                console.log(err);
            });
    }
    //Paging
    const [page, setpage] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [totalCnt, setTotalCnt] = useState(0);
    const [AllBoard, setBoardList] = useState([]);
    // 게시글 전체조회
    const BoardList = async(page) =>{
        try{
            const response = await axios.get("http://localhost:8080/board/list",{
                params: {"page":page-1},
            });
             console.log(response.data);

            setBoardList(response.data.content);
            setPageSize(response.data.pageSize);
            setTotalPages(response.data.totalPages);
            setTotalCnt(response.data.totalElements);
        }catch (error){
            console.log(error);
        }
    };
    // 첫 로딩 시, 한 페이지만 가져옴
    useEffect(() => {
        BoardList(1);
    }, []);
    // 페이징 보여주기
    const changePage = (page) => {
        setpage(page);
        BoardList(page); // 페이지 파라미터를 전달
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
                    {/* 두 번째 카드 (3/4 너비) */}
                    <Grid item xs={9}>
                        <Card>
                            <CardContent className="p-4">
                                <div className="flex justify-between items-center mb-4">
                                    <Typography variant="h5" component="h2">나의 인증</Typography>
                                </div>
                                <Table aria-label="basic table">
                                    <thead>
                                    <tr>
                                        <th className="col-1">번호</th>
                                        <th className="col-4">제목</th>
                                        <th className="col-3">내용</th>
                                        <th className="col-3">작성일</th>
                                        <th className="col-3">수정일</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {AllBoard.map(boardItem => (
                                        <tr key={boardItem.id}>
                                            <td>{boardItem.boardId}</td>
                                            <td> <Link to={`"http://localhost:8080/board/${boardItem.boardId}`}>{boardItem.title}</Link></td>
                                            <td>{boardItem.content}</td>
                                            <td>{boardItem.createdDate}</td>
                                            <td>{boardItem.modifiedDate}</td>
                                            {/* 게시판 데이터의 필드를 맞게 추가하세요. */}
                                        </tr>
                                    ))}


                                    </tbody>
                                </Table>
                                <Pagination className="pagination"
                                            activePage={page}
                                            itemsCountPerPage={pageSize}
                                            totalItemsCount={totalCnt}
                                            pageRangeDisplayed={totalPages}
                                            prevPageText={"‹"}
                                            nextPageText={"›"}
                                            onChange={changePage} variant="outlined" shape="rounded" />
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </div>
        </div>
    );
}
