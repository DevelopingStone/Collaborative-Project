package com.zerobase.oriticket.domain.members.entity;

import com.zerobase.oriticket.domain.members.constants.MemberStatus;
import com.zerobase.oriticket.domain.members.constants.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long memberId;

    @Column(length = 30, unique = true)
    private String email;

    @Column(length = 30)
    private String name;

    @Column(length = 30, unique = true)
    private String nickname;

    @Column( length = 100)
    private String password;

    @Column()
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime birthDate;

    @Column(length = 30, unique = true)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER;

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @CreationTimestamp
    private Timestamp registeredAt;

    @CreationTimestamp
    private Timestamp modifiedAt;

    private String oauth; // kakao

}
