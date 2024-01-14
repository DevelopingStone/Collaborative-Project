package com.zerobase.oriticket.domain.transaction.dto;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTransactionRequest {
    private Long salePostId;
    private Long memberId;

    public Transaction toEntity(Post salePost, Member member) {
        return Transaction.builder()
                .salePost(salePost)
                .member(member)
                .status(TransactionStatus.PENDING)
                .startedAt(LocalDateTime.now())
                .build();
    }
}
