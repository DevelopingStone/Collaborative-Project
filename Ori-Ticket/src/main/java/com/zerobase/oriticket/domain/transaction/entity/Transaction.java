package com.zerobase.oriticket.domain.transaction.entity;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    private Post salePost;

    @ManyToOne
    private Member member;

    private Integer payAmount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime receivedAt;

    @CreatedDate
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

}
