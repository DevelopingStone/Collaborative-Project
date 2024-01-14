package com.zerobase.oriticket.domain.post.service;

import com.zerobase.oriticket.domain.post.dto.RegisterAwayTeamRequest;
import com.zerobase.oriticket.domain.post.entity.AwayTeam;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.repository.AwayTeamRepository;
import com.zerobase.oriticket.domain.post.repository.SportsRepository;
import com.zerobase.oriticket.domain.post.repository.TicketRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.oriticket.global.constants.PostExceptionStatus.AWAY_TEAM_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.PostExceptionStatus.CANNOT_DELETE_AWAY_TEAM_EXIST_TICKET;

@Service
@RequiredArgsConstructor
public class AwayTeamService {

    private final AwayTeamRepository awayTeamRepository;
    private final SportsRepository sportsRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public AwayTeam register(RegisterAwayTeamRequest request) {
        Sports sports = sportsRepository.findById(request.getSportsId())
                .orElseThrow(() -> new CustomException(AWAY_TEAM_NOT_FOUND.getCode(), AWAY_TEAM_NOT_FOUND.getMessage()));


        return awayTeamRepository.save(request.toEntity(sports));
    }

    public AwayTeam get(Long awayTeamId) {

        return awayTeamRepository.findById(awayTeamId)
                .orElseThrow(() -> new CustomException(AWAY_TEAM_NOT_FOUND.getCode(), AWAY_TEAM_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<AwayTeam> getAll() {

        return awayTeamRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AwayTeam> getBySportsId(Long sportsId) {

        return awayTeamRepository.findAllBySports_SportsId(sportsId);
    }

    @Transactional
    public Long delete(Long awayTeamId){
        AwayTeam awayTeam = awayTeamRepository.findById(awayTeamId)
                .orElseThrow(() -> new CustomException(AWAY_TEAM_NOT_FOUND.getCode(), AWAY_TEAM_NOT_FOUND.getMessage()));

        if(ticketRepository.existsByAwayTeam(awayTeam)){
            throw new CustomException(CANNOT_DELETE_AWAY_TEAM_EXIST_TICKET.getCode(),
                    CANNOT_DELETE_AWAY_TEAM_EXIST_TICKET.getMessage());
        }

        awayTeamRepository.delete(awayTeam);

        return awayTeam.getAwayTeamId();
    }
}
