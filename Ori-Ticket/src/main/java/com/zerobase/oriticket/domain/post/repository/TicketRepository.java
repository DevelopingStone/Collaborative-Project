package com.zerobase.oriticket.domain.post.repository;

import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.entity.Stadium;
import com.zerobase.oriticket.domain.post.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsBySports(Sports sports);
    boolean existsByStadium(Stadium stadium);
    boolean existsByAwayTeam(AwayTeam awayTeam);
}
