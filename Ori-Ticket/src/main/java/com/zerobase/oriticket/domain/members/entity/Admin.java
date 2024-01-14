package com.zerobase.oriticket.domain.members.entity;

import com.zerobase.oriticket.domain.members.constants.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Timestamp;
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
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long adminId;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private RoleType roles = RoleType.ROLE_ADMIN;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp registeredAt;

}
