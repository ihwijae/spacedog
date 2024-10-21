// src/Login.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // useHistory를 useNavigate로 변경

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate(); // useNavigate로 history 객체 생성

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/login', {
                email,
                password,
            });

            // 로그인 성공 시 처리
            console.log('Login successful:', response.data);
            localStorage.setItem('token', response.data.token); // JWT 토큰 저장

            // 여기에 로그인 성공 후 처리 추가 (예: 리다이렉트)
            navigate('/main');
        } catch (err) {
            console.error('Login error:', err);
            setError('로그인 실패. 이메일 또는 비밀번호를 확인하세요.'); // 에러 메시지 설정
        }
    };

    return (
        <div style={{ maxWidth: '400px', margin: 'auto', padding: '20px', border: '1px solid #ccc', borderRadius: '5px' }}>
            <h2>로그인</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>} {/* 에러 메시지 표시 */}
            <form onSubmit={handleLogin}>
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="email">이메일:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    />
                </div>
                <div style={{ marginBottom: '15px' }}>
                    <label htmlFor="password">비밀번호:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    />
                </div>
                <button type="submit" style={{ width: '100%', padding: '10px' }}>
                    로그인
                </button>
            </form>
        </div>
    );
};

export default Login;