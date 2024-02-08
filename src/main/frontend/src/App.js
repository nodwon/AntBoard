import * as React from 'react';
import Footer from "./Components/app/Footer"
import Header from "./Components/app/Header"
import Main from "./Components/app/Home";


function App() {

    return (
        <div>
            <Header/>
            <div className="container">
            <Main/>
            </div>
            <Footer/>
        </div>

    );
}

export default App;
