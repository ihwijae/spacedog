import React from 'react';

const ProductList = ({ products, addToCart }) => {
    return (
        <div>
            <h2>상품 목록</h2>
            <ul>
                {products.map(product => (
                    <li key={product.id}>
                        <h3>{product.name}</h3>
                        <p>가격: {product.price}원</p>
                        <button onClick={() => addToCart(product)}>장바구니에 담기</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ProductList;