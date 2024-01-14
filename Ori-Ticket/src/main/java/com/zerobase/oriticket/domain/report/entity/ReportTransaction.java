package com.zerobase.oriticket.domain.report.entity;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.constants.ReportTransactionType;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ReportTransaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportTransactionId;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Transaction transaction;

    private ReportTransactionType reason;

    @CreatedDate
    private LocalDateTime reportedAt;

    private ReportReactStatus status;

    private LocalDateTime reactedAt;

    private String note;
}
