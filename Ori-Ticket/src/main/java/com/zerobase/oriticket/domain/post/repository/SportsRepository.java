package com.zerobase.oriticket.domain.post.repository;

import com.zerobase.oriticket.domain.post.entity.Sports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportsRepository extends JpaRepository<Sports, Long> {
}
