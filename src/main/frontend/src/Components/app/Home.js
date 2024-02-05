import React from "react";
import Button from '@mui/joy/Button';
import Textarea from '@mui/joy/Textarea';
import {Card, CardContent, Grid, Input, styled, Table, Typography} from '@mui/material'; // Import Card and Typography components
import "../../css/home.css";
import * as PropTypes from "prop-types";
import {SvgIcon} from "@mui/joy";
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
    const CreateBoard = async () =>{

    }
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
                                        <p><Input type="text" name="title" placeholder="제목을 입력하세요"/></p>
                                        <p><Textarea  type="text" name="body" minRows={5} placeholder="본문을 입력하세요" variant="soft"/></p>
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
                                    <Button type="Sumbit" size="md" variant={variant} color="success">저장</Button>
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
                                        <td className="font-medium">1</td>
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
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </div>
        </div>
    );
}
