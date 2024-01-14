package com.zerobase.oriticket.domain.post.service;

import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.dto.RegisterPostRequest;
import com.zerobase.oriticket.domain.post.dto.UpdateStatusToReportedPostRequest;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.repository.*;
import com.zerobase.oriticket.domain.transaction.repository.TransactionRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.oriticket.global.constants.MemberExceptionStatus.MEMBER_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.PostExceptionStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final TransactionRepository transactionRepository;
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final TicketRepository ticketRepository;
    private final SportsRepository sportsRepository;
    private final StadiumRepository stadiumRepository;
    private final AwayTeamRepository awayTeamRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post registerPost(RegisterPostRequest request) {

        Member member = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND.getCode(), MEMBER_NOT_FOUND.getMessage()));

        Ticket ticket = registerTicket(request);

        Post post = postRepository.save(request.toEntityPost(member, ticket));

        postSearchRepository.save(PostSearchDocument.fromEntity(post));

        return post;
    }

    @Transactional(readOnly = true)
    public Post get(Long salePostId) {

        return postRepository.findById(salePostId)
                .orElseThrow(() -> new CustomException(SALE_POST_NOT_FOUND.getCode(), SALE_POST_NOT_FOUND.getMessage()));
    }

    @Transactional
    public Long delete(Long salePostId) {
        Post salePost = postRepository.findById(salePostId)
                .orElseThrow(() -> new CustomException(SALE_POST_NOT_FOUND.getCode(), SALE_POST_NOT_FOUND.getMessage()));

        if (transactionRepository.existsBySalePost(salePost)){
            throw new CustomException(CANNOT_DELETE_POST_EXIST_TRANSACTION.getCode(),
                    CANNOT_DELETE_POST_EXIST_TRANSACTION.getMessage());
        }

        postRepository.delete(salePost);
        ticketRepository.delete(salePost.getTicket());

        postSearchRepository.delete(PostSearchDocument.fromEntity(salePost));

        return salePost.getSalePostId();
    }

    @Transactional
    public Post updateToReported(UpdateStatusToReportedPostRequest request) {
        Post salePost = postRepository.findById(request.getSalePostId())
                .orElseThrow(() -> new CustomException(SALE_POST_NOT_FOUND.getCode(), SALE_POST_NOT_FOUND.getMessage()));

        validateCanUpdateToReportedStatus(salePost.getSaleStatus());

        salePost.setSaleStatus(SaleStatus.REPORTED);
        Post updatedPost = postRepository.save(salePost);
        postSearchRepository.save(PostSearchDocument.fromEntity(updatedPost));

        return updatedPost;
    }

    private Ticket registerTicket(RegisterPostRequest request) {

        Sports sports = sportsRepository.findById(request.getSportsId())
                .orElseThrow(() -> new CustomException(SPORTS_NOT_FOUND.getCode(), SPORTS_NOT_FOUND.getMessage()));

        Stadium stadium = stadiumRepository.findById(request.getStadiumId())
                .orElseThrow(() -> new CustomException(STADIUM_NOT_FOUND.getCode(), STADIUM_NOT_FOUND.getMessage()));

        AwayTeam awayTeam = awayTeamRepository.findById(request.getAwayTeamId())
                .orElseThrow(() -> new CustomException(AWAY_TEAM_NOT_FOUND.getCode(), AWAY_TEAM_NOT_FOUND.getMessage()));

        return ticketRepository.save(request.toEntityTicket(sports, stadium, awayTeam));
    }

    private void validateCanUpdateToReportedStatus(SaleStatus status){
        if(status == SaleStatus.REPORTED){
            throw new CustomException(CANNOT_MODIFY_POST_STATE_OF_REPORTED.getCode(),
                    CANNOT_MODIFY_POST_STATE_OF_REPORTED.getMessage());
        }
        if(status == SaleStatus.SOLD){
            throw new CustomException(CANNOT_MODIFY_POST_STATE_OF_SOLD.getCode(),
                    CANNOT_MODIFY_POST_STATE_OF_SOLD.getMessage());
        }
    }
}
