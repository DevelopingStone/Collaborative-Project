package com.zerobase.oriticket.domain.post.entity;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sale_post")
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salePostId;

    @ManyToOne
    private Member member;

    @OneToOne
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @CreatedDate
    private LocalDateTime createdAt;

}
