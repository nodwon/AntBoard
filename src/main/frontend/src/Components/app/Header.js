import React, {createContext, useContext, useEffect} from "react";
import {AppBar, Box, IconButton, InputBase, Menu, MenuItem, styled, Toolbar, Typography,} from "@mui/material";
import {AccountCircle, Menu as MenuIcon, MoreVert as MoreIcon, Search as SearchIcon,} from "@mui/icons-material";
import {alpha} from "@mui/material/styles";
import {useNavigate} from "react-router-dom";
import {HttpHeadersContext} from "../file/HttpHeadersProvider";
import AuthProvider from "../file/AuthProvider"; // AuthContext 경로에 맞게 조정
const Search = styled("div")(({theme}) => ({
    position: "relative",
    borderRadius: theme.shape.borderRadius,
    backgroundColor: alpha(theme.palette.common.white, 0.15),
    "&:hover": {
        backgroundColor: alpha(theme.palette.common.white, 0.25),
    },
    marginRight: theme.spacing(2),
    marginLeft: 0,
    width: "100%",
    [theme.breakpoints.up("sm")]: {
        marginLeft: theme.spacing(3),
        width: "auto",
    },
}));

const SearchIconWrapper = styled("div")(({theme}) => ({
    padding: theme.spacing(0, 2),
    height: "100%",
    position: "absolute",
    pointerEvents: "none",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
}));

const StyledInputBase = styled(InputBase)(({theme}) => ({
    color: "inherit",
    "& .MuiInputBase-input": {
        padding: theme.spacing(1, 1, 1, 0),
        // vertical padding + font size from searchIcon
        paddingLeft: `calc(1em + ${theme.spacing(4)})`,
        transition: theme.transitions.create("width"),
        width: "100%",
        [theme.breakpoints.up("md")]: {
            width: "20ch",
        },
    },
}));


function Header() {


// 초기 상태를 기본값으로 사용하여 Context 생성
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);
    const navigate = useNavigate();
    const isMenuOpen = Boolean(anchorEl);
    const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);
    const { auth, setAuth } = useContext(AuthProvider);
    const { headers, setHeaders } = useContext(HttpHeadersContext);



    const handleProfileMenuOpen = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMobileMenuClose = () => {
        setMobileMoreAnchorEl(null);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
        handleMobileMenuClose();
    };

    const handleMobileMenuOpen = (event) => {
        setMobileMoreAnchorEl(event.currentTarget);
    };
    useEffect(() => {
        // 컴포넌트가 마운트될 때 실행됩니다.
        const accessToken = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');

        if (accessToken && refreshToken) {
            setAuth(accessToken); // 또는 저장된 사용자 정보를 사용하여 auth 상태를 설정
            setHeaders({ ...headers, Authorization: `Bearer ${accessToken}` });
        }
    }, []); // 의존성 배열을 비워서 컴포넌트가 처음 마운트될 때만 실행되도록 합니다.
    const handleLogoutClick = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        handleMenuClose();
        navigate('/'); // 로그아웃 후 홈으로 리다이렉트
    };
    const menuId = "primary-search-account-menu";
    const renderMenu = (
        <Menu
            anchorEl={anchorEl}
            anchorOrigin={{
                vertical: "top",
                horizontal: "right",
            }}
            id={menuId}
            keepMounted
            transformOrigin={{
                vertical: "top",
                horizontal: "right",
            }}
            open={isMenuOpen}
            onClose={handleMenuClose}
        >
            {auth  ? (

                <MenuItem onClick={handleLogoutClick}>Logout</MenuItem>
            ) : (
                <>
                    <MenuItem onClick={() => navigate("/register")}>Register</MenuItem>
                    <MenuItem onClick={() => navigate("/login")}>Login</MenuItem>
                </>
            )}
        </Menu>
    );

    const mobileMenuId = "primary-search-account-menu-mobile";
    const renderMobileMenu = (
        <Menu
            anchorEl={mobileMoreAnchorEl}
            anchorOrigin={{
                vertical: "top",
                horizontal: "right",
            }}
            id={mobileMenuId}
            keepMounted
            transformOrigin={{
                vertical: "top",
                horizontal: "right",
            }}
            open={isMobileMenuOpen}
            onClose={handleMobileMenuClose}
        >
            <MenuItem onClick={handleProfileMenuOpen}>
                <IconButton
                    size="large"
                    aria-label="account of current user"
                    aria-controls="primary-search-account-menu"
                    aria-haspopup="true"
                    color="inherit"
                >
                    <AccountCircle/>
                </IconButton>
                <p>Profile</p>
            </MenuItem>
        </Menu>
    );

    return (
        <Box sx={{flexGrow: 1}}>
            <AppBar position="static">
                <Toolbar>
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        sx={{mr: 2}}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{display: {xs: "none", sm: "block"}}}
                        onClick={() => navigate('/')}
                    >
                        수익 인증 게시판
                    </Typography>
                    <p>{`안녕하세요, ${auth}님`}</p>
                    <p>{`안녕하세요, ${headers}님`}</p>
                    <Search>
                <SearchIconWrapper>
                            <SearchIcon/>
                        </SearchIconWrapper>
                        <StyledInputBase
                            placeholder="Search…"
                            inputProps={{"aria-label": "search"}}
                        />
                    </Search>
                    <Box sx={{flexGrow: 1}}/>
                    <Box sx={{display: {xs: "none", md: "flex"}}}>
                        <IconButton
                            size="large"
                            aria-label="show 4 new mails"
                            color="inherit"
                        >
                        </IconButton>
                        <IconButton
                            size="large"
                            edge="end"
                            aria-label="account of current user"
                            aria-controls={menuId}
                            aria-haspopup="true"
                            onClick={handleProfileMenuOpen}
                            color="inherit"
                        >
                            <AccountCircle/>
                        </IconButton>
                    </Box>
                    <Box sx={{display: {xs: "flex", md: "none"}}}>
                        <IconButton
                            size="large"
                            aria-label="show more"
                            aria-controls={mobileMenuId}
                            aria-haspopup="true"
                            onClick={handleMobileMenuOpen}
                            color="inherit"
                        >
                            <MoreIcon/>
                        </IconButton>
                    </Box>
                </Toolbar>
            </AppBar>
            {renderMobileMenu}
            {renderMenu}
        </Box>
    );
}

export default Header;