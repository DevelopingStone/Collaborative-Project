package com.zerobase.oriticket.domain.report.entity;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.report.constants.ReportPostType;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
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
public class ReportPost {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportPostId;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Post salePost;

    private ReportPostType reason;

    @CreatedDate
    private LocalDateTime reportedAt;

    private ReportReactStatus status;

    private LocalDateTime reactedAt;

    private String note;
}
