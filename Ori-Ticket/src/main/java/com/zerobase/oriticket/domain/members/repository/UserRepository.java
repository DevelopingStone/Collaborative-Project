package com.zerobase.oriticket.domain.members.repository;

import com.zerobase.oriticket.domain.members.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Member findByMemberId(long id);

    Member findByEmail(String email);
}