import * as React from 'react';
import Button from '@mui/joy/Button';
import Textarea from '@mui/joy/Textarea';
import { Table, Grid, Input } from '@mui/material'; // Import Table components from the correct library
import axios from 'axios';
import { useEffect, useState } from "react";

function App() {
    //const [users, setUsers] = useState([]);

    // useEffect(() => {
    //     axios.get('http://localhost:8080/')
    //         .then(response => {
    //             setUsers(response.data);
    //         })
    //         .catch(error => {
    //             console.error('Error fetching users: ', error);
    //         });
    // }, []);

    return (
        <div className="container mx-auto">
            <h1 className="text-3xl font-bold">수익 인증 게시판</h1>
            <Grid container spacing={2}>
                {/* 첫 번째 컬럼 (1/4 너비) */}
                <Grid item xs={3}>
                    <div className="p-4">
                        <div className="border p-4 mb-4">
                            <Input className="mb-4" placeholder="제목을 입력하세요"/>
                            <Textarea className="mb-4" minRows={5} placeholder="본문을 입력하세요" variant="soft"/>
                            <Button className="bg-blue-200">이미지 첨부</Button>
                        </div>
                        <div className="flex justify-between">
                            <Button className="bg-green-200" variant="outline">
                                그린
                            </Button>
                            <Button className="bg-blue-200">저장</Button>
                            <Button className="bg-blue-200">취소</Button>
                        </div>
                    </div>
                </Grid>
                {/* 두 번째 컬럼 (3/4 너비) */}
                <Grid item xs={9}>
                    <div className="p-4">
                        <div className="flex justify-between items-center p-4">
                            <h3 className="text-xl font-bold">나의 인증</h3>
                            <Button className="bg-green-500">회원가입</Button>
                        </div>
                        <div className="flex justify-end space-x-2 p-4">
                            <Button className="bg-green-200">리스트</Button>
                            <Button className="bg-green-200">카드</Button>
                        </div>
                        <Table aria-label="basic table">
                            <thead>
                            <tr>
                                <th style={{width: '40%'}}>Dessert (100g serving)</th>
                                <th>제목</th>
                                <th>닉네임</th>
                                <th>작성일</th>
                                <th>수정일</th>
                                <th className="w-[100px]"/>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td className="font-medium">4</td>
                                <td>두번째 테스트 제목</td>
                                <td>네네</td>
                                <td>2024-01-14T06:56:46.415244</td>
                                <td>2024-01-14T06:56:46.415244</td>
                                <td>
                                    <Button className="bg-blue-500">수정</Button>
                                </td>
                            </tr>
                            </tbody>
                        </Table>
                    </div>
                </Grid>
            </Grid>
        </div>
    );
}

export default App;
