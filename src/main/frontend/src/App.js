import * as React from 'react';
import Button from '@mui/joy/Button';
import Textarea from '@mui/joy/Textarea';
import { Table, Grid, Input } from '@mui/material'; // Import Table components from the correct library
// import axios from 'axios';
// import { useEffect, useState } from "react";
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
