package com.zerobase.oriticket.domain.post.service;

import com.zerobase.oriticket.domain.post.dto.RegisterStadiumRequest;
import com.zerobase.oriticket.domain.post.entity.Sports;
import com.zerobase.oriticket.domain.post.entity.Stadium;
import com.zerobase.oriticket.domain.post.repository.SportsRepository;
import com.zerobase.oriticket.domain.post.repository.StadiumRepository;
import com.zerobase.oriticket.domain.post.repository.TicketRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.oriticket.global.constants.PostExceptionStatus.CANNOT_DELETE_STADIUM_EXIST_TICKET;
import static com.zerobase.oriticket.global.constants.PostExceptionStatus.SPORTS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private final SportsRepository sportsRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public Stadium register(RegisterStadiumRequest request) {
        Sports sports = sportsRepository.findById(request.getSportsId())
                .orElseThrow(() -> new CustomException(SPORTS_NOT_FOUND.getCode(), SPORTS_NOT_FOUND.getMessage()));

        return stadiumRepository.save(request.toEntity(sports));
    }

    @Transactional(readOnly = true)
    public Stadium get(Long stadiumId) {

        return stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new CustomException(SPORTS_NOT_FOUND.getCode(), SPORTS_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<Stadium> getAll() {

        return stadiumRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Stadium> getBySportsId(Long sportsId) {

        return stadiumRepository.findAllBySports_SportsId(sportsId);
    }

    @Transactional
    public Long delete(Long stadiumId){
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new CustomException(SPORTS_NOT_FOUND.getCode(), SPORTS_NOT_FOUND.getMessage()));

        if(ticketRepository.existsByStadium(stadium)){
            throw new CustomException(CANNOT_DELETE_STADIUM_EXIST_TICKET.getCode(), CANNOT_DELETE_STADIUM_EXIST_TICKET.getMessage());
        }

        stadiumRepository.delete(stadium);

        return stadium.getStadiumId();
    }
}
