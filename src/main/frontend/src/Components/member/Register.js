import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import Login from "./Login";
import "../../css/register.css";

function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" href="https://mui.com/">
                Your Website
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

// TODO remove, this demo shouldn't need to reset the theme.

const defaultTheme = createTheme();

function Register(){
    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [pwd, setPwd] = useState("");
    const [checkPwd, setCheckPwd] = useState("");
    const [verificationNumber, setVerificationNumber] = useState('');
    const [inputNumber, setInputNumber] = useState('');
    const [isVerified, setIsVerified] = useState(null);
    const navigate = useNavigate();
    const handleEmailChange = (event) => setEmail(event.target.value);
    const handleInputNumberChange = (event) => setInputNumber(event.target.value);

    const changeEmail = (event) => {
        setEmail(event.target.value);
    }

    const changeName = (event) => {
        setName(event.target.value);
    }

    const changePwd = (event) => {
        setPwd(event.target.value);
    }

    const changeCheckPwd = (event) => {
        setCheckPwd(event.target.value);
    }
    const [showInput, setShowInput] = useState(false); // 추가된 상태

    /* 아이디 중복 체크 */
    const checkEmailDuplicate = async (event) => {
        event.preventDefault(); // 기본 동작 막기

        try {
            const resp = await axios.get("http://localhost:8080/user/checkId", { params: { email: email } });
            console.log("[Register.js] checkEmailDuplicate() success :D");
            console.log(resp.data);

            if (resp.status === 200 && resp.data.available) {
                alert("사용 가능한 이메일입니다.");
            } else {
                alert("이미 사용 중인 이메일입니다.");
            }
        } catch (err) {
            console.log("[Register.js] checkEmailDuplicate() error :<");
            console.log(err);

            const resp = err.response;
            if (resp && resp.status === 400) {
                alert(resp.data.message || "이메일 중복 확인에 실패했습니다.");
            } else {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    }
    // 이메일 중복 확인 및 인증번호 전송
    const checkEmailAndSendVerification = async (event) => {
        event.preventDefault(); // 기본 동작 막기

        try {
            // 이메일 중복 체크
            const emailCheckResponse = await axios.get("http://localhost:8080/user/checkId", {
                params: { email: email }
            });

            console.log("[Register.js] checkEmailAndSendVerification() email check success");
            console.log(emailCheckResponse.data);

            if (emailCheckResponse.status === 200 && emailCheckResponse.data.available) {
                // 이메일 사용 가능할 경우 인증번호 전송
                alert("사용 가능한 이메일입니다. 인증번호를 발송합니다.");

                try {
                    const verificationResponse = await axios.post('http://localhost:8080/mailSend', { mail: email });

                    if (verificationResponse.data.success) {
                        alert('인증번호가 이메일로 전송되었습니다.');
                        setVerificationNumber(verificationResponse.data.number);
                    } else {
                        alert('이메일 전송에 실패했습니다.');
                    }
                } catch (sendError) {
                    console.error('Error sending email:', sendError);
                    alert('서버 오류가 발생했습니다.');
                }
            } else {
                alert("이미 사용 중인 이메일입니다.");
            }
        } catch (error) {
            console.log("[Register.js] checkEmailAndSendVerification() error");
            console.log(error);

            const resp = error.response;
            if (resp && resp.status === 400) {
                alert(resp.data.message || "이메일 중복 확인에 실패했습니다.");
            } else {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        }
    };

    /* 회원가입 */
    const join = async () => {
        const req = {
            email: email,
            password: pwd,
            passwordCheck: checkPwd,
            username: name,
        }
        try {
            const resp = await axios.post("http://localhost:8080/user/register", req);
            alert(resp.data.username + "님 회원가입을 축하드립니다 🎊");
            navigate("/"); // 메인 페이지로 이동
        } catch (err) {
            console.error("[Join.js] join() error :", err);
            const resp = err.response;
            if (resp.status === 400) {
                alert(resp.data);
            }
        }
    }
    // 이메일로 인증번호 전송
    // const sendVerificationNumber = async () => {
    //     try {
    //         const response = await axios.post('http://localhost:8080/mailSend', { mail: email });
    //         if (response.data.success) {
    //             alert('인증번호가 이메일로 전송되었습니다.');
    //             setVerificationNumber(response.data.number);
    //         } else {
    //             alert('이메일 전송에 실패했습니다.');
    //         }
    //     } catch (error) {
    //         console.error('Error sending email:', error);
    //         alert('서버 오류가 발생했습니다.');
    //     }
    // };

    // 인증번호 일치 여부 확인
    const verifyNumber = async () => {
        try {
            const response = await axios.get('http://localhost:8080/mailCheck', {
                params: { userNumber: inputNumber },
            });
            setIsVerified(response.data);

            if (response.data) {
                alert('인증번호가 일치합니다.');
            } else {
                alert('인증번호가 일치하지 않습니다.');
            }
        } catch (error) {
            console.error('Error verifying number:', error);
            alert('서버 오류가 발생했습니다.');
        }
    };


    const handleSubmit = async (event) => {
        event.preventDefault();
        join(); // Sign In 버튼 클릭 시 회원가입 완료
    };

    return (
        <ThemeProvider theme={defaultTheme}>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                        <LockOutlinedIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        Sign up
                    </Typography>
                    <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
                        <Grid container spacing={2}>
                            <Grid item xs={9}>
                                <TextField
                                    required
                                    fullWidth
                                    value={email} onChange={changeEmail}
                                    id="email"
                                    label="Email Address"
                                    name="email"
                                    autoComplete="email"
                                />
                            </Grid>
                            {/*<Grid item xs={3}>*/}
                            {/*    <button id="checkDuplicateButton" className="btn btn-outline-danger" onClick={checkEmailAndSendVerification}>*/}
                            {/*        <i className="fas fa-check" ></i> 이메일 확인*/}
                            {/*    </button>*/}
                            {/*</Grid>*/}
                            <Grid item xs={3}>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    fullWidth
                                    onClick={checkEmailAndSendVerification}
                                    sx={{ mt: 2 }}
                                >
                                    인증번호 받기
                                </Button>
                            </Grid>
                        </Grid>
                            {/*<Grid item xs={3}>*/}
                            {/*    <Button*/}
                            {/*        variant="contained"*/}
                            {/*        color="primary"*/}
                            {/*        fullWidth*/}
                            {/*        onClick={checkEmailAndSendVerification}*/}
                            {/*        sx={{ mt: 2 }}*/}
                            {/*    >*/}
                            {/*        인증번호 받기*/}
                            {/*    </Button>*/}
                            {/*</Grid>*/}
                        {showInput && (<Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label="인증번호 입력"
                                    value={inputNumber}
                                    onChange={handleInputNumberChange}
                                    required
                                />
                            </Grid>
                            )}
                        {showInput && (
                            <Grid item xs={12}>
                                <Button
                                    variant="contained"
                                    color="secondary"
                                    fullWidth
                                    onClick={verifyNumber}
                                    sx={{ mt: 2 }}>
                                    인증번호 확인
                                </Button>
                            </Grid>
                        )}
                {isVerified !== null && (
                    <Typography sx={{ mt: 2 }} color={isVerified ? 'green' : 'red'}>
                        {isVerified ? '인증 성공!' : '인증 실패!'}
                    </Typography>
                )}
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    name="name"
                                    value={name} onChange={changeName}
                                    label="name"
                                    type="name"
                                    id="name"
                                    autoComplete="new-name"
                                />
                            </Grid>
                            <Grid item xs={12}>
                            <TextField
                                    required
                                    fullWidth
                                    name="password"
                                    value={pwd} onChange={changePwd}
                                    label="Password"
                                    type="Checkpassword"
                                    id="password"
                                    autoComplete="new-password"
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    name="Checkpassword"
                                    value={checkPwd} onChange={changeCheckPwd}
                                    label="Password"
                                    type="Checkpassword"
                                    id="Checkpassword"
                                    autoComplete="new-Checkpassword"
                                />
                            </Grid>

                            <Grid item xs={12}>
                                <FormControlLabel
                                    control={<Checkbox value="allowExtraEmails" color="primary" />}
                                    label="I want to receive inspiration, marketing promotions and updates via email."
                                />
                            </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Sign Up
                        </Button>
                        <Grid container justifyContent="flex-end">
                            <Grid item>
                                <Link href="/Login" variant="body2">
                                    Already have an account? Sign in
                                </Link>
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
                <Copyright sx={{ mt: 5 }} />
            </Container>
        </ThemeProvider>
        );
}
export default Register;