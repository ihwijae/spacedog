# 📌 Spedok
> 강아지 용품 쇼핑몰 프로젝트 (1인)

<br>

## 🔍 제작 기간 & 참여 인원
- 2024.09 ~ 2024.11
- 개인 프로젝트

<br>

## 🔍 사용 기술 :
- Java 21
- Spring Boot 3.2.10
- Spring Security
- JPA 
- QueryDSL 5.0.0
- Rest API
- MySQL 8.0
- H2 DB Embedded
- Docker

<br>

## 🔍 ERD
![erd.png](./erd.png)


<br>


## 🔍 핵심 기술 :


<details>
<summary>주문 API</summary>

### 주문 로직 전체흐름
<img src="order.png" alt="주문 전체 로직">

- 주문 생성  
  - 배송지 생성 
  - 재고 확인 (재고가 없거나, 부족하면 예외 처리)
  - 주문 번호 생성 (UUID 조합)
  - 장바구니에 존재하는 상품인지 검증 
  - 최종 주문 생성

#### 주문생성
> 1. 배송지 생성
> 2. 재고 확인
> 3. 재고 처리
> 4. 주문 번호 생성 (UUID 조합)
> 5. 장바구니 존재 유무 검증
> 6. 최종 주문 생성

<br>

#### 주문취소
> 1. 주문 상태 검증
> 2. 주문 상태 변경
> 3. 재고 복원


</details>

