// src/Main.js
import React from 'react';
import './MainPage.css'; // CSS 파일을 추가하여 스타일링

const Main = () => {
    const pets = [
        {
            id: 1,
            name: '코코',
            breed: '말티즈',
            image: 'https://via.placeholder.com/150', // 이미지 URL (예시)
        },
        {
            id: 2,
            name: '루이',
            breed: '푸들',
            image: 'https://via.placeholder.com/150',
        },
        {
            id: 3,
            name: '미미',
            breed: '고양이',
            image: 'https://via.placeholder.com/150',
        },
        {
            id: 4,
            name: '치치',
            breed: '비숑',
            image: 'https://via.placeholder.com/150',
        },
    ];

    return (
        <div className="main-container">
            <h1>우리의 소중한 반려동물</h1>
            <div className="pet-list">
                {pets.map((pet) => (
                    <div className="pet-card" key={pet.id}>
                        <img src={pet.image} alt={pet.name} className="pet-image" />
                        <h2>{pet.name}</h2>
                        <p>품종: {pet.breed}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Main;