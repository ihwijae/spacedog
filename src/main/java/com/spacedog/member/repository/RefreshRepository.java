package com.spacedog.member.repository;

import com.spacedog.member.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Boolean existsByRefreshToken(String refreshToken);


    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
