import React, { useEffect, useState } from 'react';
import { getIndices } from '../../services/api';
import './indices.css';

const Indices = () => {
    const [indicesKor, setIndicesKor] = useState([]);
    const [jobDate, setJobDate] = useState('');

    useEffect(() => {
        getIndices()
            .then((response) => {
                setIndicesKor(response.data.indicesKor);
                setJobDate(response.data.jobDate);
            })
            .catch((error) => {
                console.error('Failed to fetch indices:', error);
            });
    }, []);

    const getColorStyle = (value) => {
        if (value < 0) return { color: 'red' };
        if (value > 0) return { color: 'blue' };
        return { color: 'black' };
    };

    return (
        <div className="indices-container">
            <h2>국내 지수</h2>
            <div>
                조회시간: <span>{jobDate}</span>
            </div>
            <div className="indices-card">
                <table className="table">
                    <thead>
                    <tr>
                        <th>지수명</th>
                        <th>현재가</th>
                        <th>전일대비</th>
                        <th>등락률</th>
                    </tr>
                    </thead>
                    <tbody>
                    {indicesKor.map((indexData, idx) => (
                        <tr key={idx}>
                            <td>{indexData.hts_kor_isnm}</td>
                            <td>{indexData.bstp_nmix_prpr}</td>
                            <td style={getColorStyle(indexData.prdy_vrss_sign)}>
                                {indexData.bstp_nmix_prdy_vrss}
                            </td>
                            <td style={getColorStyle(indexData.prdy_vrss_sign)}>
                                {indexData.bstp_nmix_prdy_ctrt}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Indices;
