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

    const navigate = useNavigate();

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
    /* 아이디 중복 체크 */
    const checkEmailDuplicate = async () => {

        await axios.get("http://localhost:8080/user/checkId", {params: {email: email}})
            .then((resp) => {
                console.log("[Join.js] checkEmailDuplicate() success :D");
                console.log(resp.data);

                if (resp.status === 200) {
                    alert("사용 가능한 이메일입니다.");
                }
            })
            .catch((err) => {
                console.log("[Join.js] checkEmailDuplicate() error :<");
                console.log(err);

                const resp = err.response;
                if (resp.status === 400) {
                    alert(resp.data);
                }
            });

    }

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
                            <Grid item xs={3}>
                                <button id="checkDuplicateButton" className="btn btn-outline-danger" onClick={checkEmailDuplicate}>
                                    <i className="fas fa-check" ></i> 이메일 중복 확인
                                </button>
                            </Grid>
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