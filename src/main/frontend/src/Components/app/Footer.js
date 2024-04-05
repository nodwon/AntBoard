import "../../css/footer.css"
import Cookies from "js-cookie";

function Footer() {
    // 특정 쿠키 읽기
    const myCookie = Cookies.get('myCookieName');
    console.log(myCookie);

    // 모든 쿠키 읽기
    const allCookies = Cookies.get();
    console.log(allCookies);
    return (
        <footer>
            <p>노다원 포트폴리오 by
                <a href="https://nodwon.tistory.com/">
                    <img alt="Tistory" src="https://img.shields.io/badge/Tistory-Orange.svg?&style=for-the-badge"/>
                </a>
                <a href="https://github.com/nodwon/AntBoard">
                    <img alt="GitHub"
                         src="https://img.shields.io/badge/GitHub-181717.svg?&style=for-the-badge&logo=GitHub&logoColor=white"/>
                </a>
            </p>
            <div>
                {/*<h1>쿠키 데이터 확인</h1>*/}
                {/*<p>특정 쿠키 값: {myCookie}</p>*/}
                {/*<p>모든 쿠키 값 확인: {JSON.stringify(allCookies)}</p>*/}
            </div>
        </footer>

    )
}

export default Footer;