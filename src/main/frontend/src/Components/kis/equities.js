import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getEquityDetails } from '../../services/api';
import './equities.css';

const Equities = () => {
    const { id } = useParams(); // React Router의 `:id` 파라미터 가져오기
    const [equity, setEquity] = useState(null);
    const [jobDate, setJobDate] = useState('');

    useEffect(() => {
        getEquityDetails(id)
            .then((response) => {
                setEquity(response.data.equity);
                setJobDate(response.data.jobDate);
            })
            .catch((error) => {
                console.error('Failed to fetch equity details:', error);
            });
    }, [id]);

    const getColorStyle = (value) => {
        if (value < 0) return { color: 'red' };
        if (value > 0) return { color: 'blue' };
        return { color: 'black' };
    };

    if (!equity) return <div>Loading...</div>;

    return (
        <div className="equities-container">
            <h2>오늘의 주가: {equity.stck_shrn_iscd}</h2>
            <div>
                조회시간: <span>{jobDate}</span>
            </div>
            <div className="equity-details">
                <div className="equity-price">
                    <span>{equity.stck_prpr}원</span>
                    <span style={getColorStyle(equity.prdy_vrss_sign)}>{equity.prdy_vrss}</span>
                    <span style={getColorStyle(equity.prdy_vrss_sign)}>{equity.prdy_ctrt}</span>
                </div>
                <table className="table">
                    <tbody>
                    <tr>
                        <td>PER</td>
                        <td>{equity.per}</td>
                        <td>EPS</td>
                        <td>{equity.eps}</td>
                    </tr>
                    <tr>
                        <td>PBR</td>
                        <td>{equity.pbr}</td>
                        <td>BPS</td>
                        <td>{equity.bps}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Equities;
