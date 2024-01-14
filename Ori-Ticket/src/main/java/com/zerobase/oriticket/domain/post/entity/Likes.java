package com.zerobase.oriticket.domain.post.entity;

import com.zerobase.oriticket.domain.members.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Likes {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likesId;

    @ManyToOne
    private Member member;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post salePost;
}
