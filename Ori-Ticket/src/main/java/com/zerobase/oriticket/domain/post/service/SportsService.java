package com.zerobase.oriticket.domain.post.service;

import com.zerobase.oriticket.domain.post.dto.RegisterSportsRequest;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.repository.SportsRepository;
import com.zerobase.oriticket.domain.post.repository.TicketRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.oriticket.global.constants.PostExceptionStatus.CANNOT_DELETE_SPORTS_EXIST_TICKET;
import static com.zerobase.oriticket.global.constants.PostExceptionStatus.SPORTS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SportsService {

    private final SportsRepository sportsRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public Sports register(RegisterSportsRequest request) {

        return sportsRepository.save(request.toEntity());
    }

    @Transactional(readOnly = true)
    public Sports get(Long sportsId) {

        return sportsRepository.findById(sportsId)
                .orElseThrow(() -> new CustomException(SPORTS_NOT_FOUND.getCode(), SPORTS_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<Sports> getAll() {
        return sportsRepository.findAll();
    }

    @Transactional
    public Long delete(Long sportsId){
        Sports sports = sportsRepository.findById(sportsId)
                .orElseThrow(() -> new CustomException(SPORTS_NOT_FOUND.getCode(), SPORTS_NOT_FOUND.getMessage()));

        if(ticketRepository.existsBySports(sports)){
            throw new CustomException(CANNOT_DELETE_SPORTS_EXIST_TICKET.getCode(),
                    CANNOT_DELETE_SPORTS_EXIST_TICKET.getMessage());
        }

        sportsRepository.delete(sports);

        return sports.getSportsId();
    }

}
