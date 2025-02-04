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
![erd.png](docs/erd.png)


<br>


## 🔍 아키텍처
![아키텍처](docs/architecture.png)

Business Layer (Service Layer) 을 두 레이어로 나누었습니다 <br>
Compoent 계층이 Data 계층 (Repository) 계층과 통신하고 <br>
비지니스 레이어는 Compoent 계층을 조립하도록 아키텍처를 설계하였습니다 <br>

**이와 같은 설계를 한 이유는 두 가지 이유가 있습니다.**

* 하나의 비지니스 레이어에 모든 로직을 넣으면 전체적인 로직의 흐름을 한 눈에 보기 어렵다는 점이 있었습니다
* 단위 테스트의 의미를 생각해 봤을때 각 API의 전체적인 흐름을 단위별로 쪼개서 테스트 함이라고 생각합니다
* 그렇다면 Compoent 계층에서 작은 단위의 로직으로 구성하고 이 단위를 테스트하는게 좋은 테스트라고 생각하였습니다. 


<br>



## 🔍 핵심 기술 :


<details>
<summary>주문 API</summary>

### 주문 로직 전체흐름
<img src="docs/order.png" alt="주문 전체 로직">


#### 주문생성
> 1. 배송지 생성
> 2. 재고 확인
> 3. 재고 처리
> 4. 주문 번호 생성 (UUID 조합)
> 5. 장바구니 존재 유무 검증
> 6. 최종 주문 생성
<img src="docs/orderLogic.png">


<br>

#### 주문취소
> 1. 주문 상태 검증
> 2. 주문 상태 변경
> 3. 재고 복원
<img src="docs/orderCancelLogic.png">
</details>


<details>
<summary>상품 API</summary>

### 상품 로직 전체 흐름
<img src="docs/ItemLogic.png" alt="상품 전체 로직">


####  상품 등록
> 1. 상품 생성을 누르면 임시 상품 DB에 저장 후 PK 반환
> 2. 상품 이름 검증 (중복 검증)
> 3. Request DTO 내부에 ID를 꺼내 임시 상품 조회
> 4. 해당 상품에 각종 정보 저장 (등록자, 이름, 가격 등등)
> 5. 요청 정보에서 카테고리 ID를 조회해 카테고리 등록
> 6. 요청 정보에서 옵션 ID를 조회해 옵션 정보 등록

<img src="docs/itemCreateV1.png" alt="상품 생성 로직1">
<img src="docs/itemCreateV2.png" alt="상품 생성 로직2">

<br>
<br>

#### 상품 전체 조회
- 페이징 기능
- 상품을 조회는 빈번히 일어난다고 생각하여 캐시 처리
- 커버링 인덱스 사용하여 페이징 성능 최적화

<img src="docs/ItemSelectQuery.png" alt="">

<br>
<br>

#### 상품 검색
- 상품 검색 기능

<img src="docs/ItemSearch.png">

<br>
<br>

#### 상품 수정
> 1. 상품 이름 검증
> 2. 파라미터로 넘어온 ID로 DB에서 상품과 사용자를 조회
> 3. 상품을 등록한 사용자인지 검증
> 4. 상품 수정
> 5. 상품 옵션 수정

<img src="docs/ItemEdit.png" alt="상품 수정 로직">

<br>
<br>

#### 상품 삭제
> 1. 상품을 등록한 사용자 인지 검증
> 2. 카테고리 상품 삭제
> 3. 상품 옵션 삭제
> 4. 상품 삭제

<img src="docs/ItemDelete.png" alt="상품삭제로직">

<br>
<br>

#### 상품 상세 조회
- 컬렉션 조회 시 N+1 문제를 해결하기 위해 최적화


```java
   public ItemDetailResponse itemDetail(Long itemId) {

        ItemDetailResponse itemDetail = repository.findItemDetail(itemId);


        // 컬렉션 조회
        Map<Long, List<OptionGroupResponse>> optionGroups = repository.findOptionGroups(itemDetail.getId());

        List<Long> optionGroupIds = optionGroups.values().stream()
                .flatMap(List::stream)
                .map(OptionGroupResponse::getId)
                .collect(Collectors.toList());

        Map<Long, List<OptionSpecsResponse>> optionSpecs = repository.findOptionSpecs(optionGroupIds);

        Map<Long, List<CategoryResponse>> categories = repository.findCategories(itemDetail.getId());


        optionGroups.values()
                .forEach(g ->
                        g.forEach(group -> group.setOptionSpecs(optionSpecs.get(group.getId()))));

        itemDetail.setCategory(categories.get(itemDetail.getId()));
        itemDetail.setOptionGroup(optionGroups.get(itemDetail.getId()));

        return itemDetail;
    }
```

</details>

<details>
<summary>리뷰 API</summary>

### 리뷰 로직 전체 흐름
<img src="docs/ReviewLogic.png" alt="리뷰 로직 전체 흐름">


#### 리뷰 전체조회 (댓글포함)
- 댓글, 대댓글을 따로 조회해서 서비스 단에서 조립하는 방식 채택

<img src="docs/ReviewFinderAll.png" alt="리뷰 전체 조회">


<br>

#### 리뷰 작성
> 1. 상품을 주문한 회원이 맞는지 검증
> 2. 상품 임시 생성 후 pk 반환
> 3. 반환 받은 pk로 리뷰 생성

<img src="docs/createReviewTemp.png" alt="리뷰 생성">
<img src="docs/ReviewSave.png" alt="리뷰 저장">


<br>

#### 리뷰 수정
> 1. 리뷰를 작성한 회원이 맞는지 검증
> 2. 파라미터로 받은 reviewId로 Review 객체 조회
> 3. 반환받은 Review 객체 수정

<img src="docs/ReviewEdit.png" alt="리뷰수정">

<br>

#### 리뷰 삭제
> 1. 인자로 받은 reviewId로 Review 객체 조회
> 2. 리뷰를 작성한 회원인지 검증
> 3. 인자로 받은 reviewId로 ReviewReply 객체 조회
> 4. Review, ReviewReply 삭제

<img src="docs/ReviewDelete.png" alt="리뷰 삭제">

</details>


<details>
<summary>카테고리 API</summary>

#### 카테고리 별 상품 조회

- 카레고리별 상품을 조회할때 커버링 인덱스를 활용하여 조회성능 최적화


<img src="docs/CategorySelect.png">

<img src="docs/CategorySelectV2.png">



</details>

<details>
<summary>파일 API</summary>

#### 파일 API 전체 흐름
<img src="docs/FileLogic.png">

<br>

#### 단일 이미지 업로드
<img src="docs/FileUpload.png">

<br>

#### 여러 이미지 업로드
<img src="docs/FileUploads.png">


</details>


<details>
<summary>옵션 API</summary>

#### 상품을 생성할때 옵션이 없을때 기본 옵션 생성
<img src="docs/DefaultOption.png">

<br>


#### 옵션에는 옵션그룹, 옵션스펙이 존재한다

#### 상품을 생성할때 옵션이 있는 경우 옵션 생셩
- 옵션 그룹 생성

<img src="docs/OptionGroupCreate.png">

- 옵션 상세 내용 생성

<img src="docs/OptionSpecsCreate.png">


</details>




