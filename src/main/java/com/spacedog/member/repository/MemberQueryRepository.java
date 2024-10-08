package com.spacedog.member.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {



    private final JPAQueryFactory queryFactory;




    /** 이건 스프링 빈으로 JPAQueryFactory 등록 안했을 경우 이렇게 직접 생성해서 주입받는다 (EntityManager) 필요함**/
    //    public MemberQueryRepository(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }





//    @Transactional(readOnly = true)
//    public Boolean exist(Long ItemId) {
//        queryFactory
//                .selectOne()
//                .from(item)
//                .where(item.id.eq(itemId))
//                .fetchFirst();
//
//        return fetchOne != null;
//    }
//
}