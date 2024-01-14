package com.zerobase.oriticket.domain.post.repository;

import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import com.zerobase.oriticket.domain.post.entity.Sports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwayTeamRepository extends JpaRepository<AwayTeam, Long> {

    List<AwayTeam> findAllBySports_SportsId(Long sportsId);
}
