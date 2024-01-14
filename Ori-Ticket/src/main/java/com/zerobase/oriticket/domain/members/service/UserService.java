package com.zerobase.oriticket.domain.members.service;

import com.zerobase.oriticket.domain.members.dto.user.UserRequest;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final String NON_EXISTENT_MEMBER = "존재하지 않는 회원 입니다.";
    private final String EXISTENT_MEMBER = "이미 가입되어 있는 회원 입니다.";
    private UserRepository userRepository;

    @Transactional
    public void updateUserByEmail(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException(EXISTENT_MEMBER);
        }
        userRepository.save(userRequest.toEntityKakao());
    }

    @Transactional(readOnly = true)
    public boolean findByEmail(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }

    public long findById(Boolean userEmail, String email) {
        if (userEmail) {
            Member member = userRepository.findByEmail(email);
            return member.getMemberId();
        }
        return -1;
    }

    public Member updateUser(UserRequest userRequest) {
        Member byId = userRepository.findByMemberId(userRequest.getId());
        if (byId == null) {
            throw new RuntimeException(NON_EXISTENT_MEMBER);
        }
        if (userRequest.getNickname() != null) {
            byId.setNickname(userRequest.getNickname());
        }
        userRepository.save(byId);
        return byId;
    }

    public Member checkUser(long id) {
        Member byId = userRepository.findByMemberId(id);
        if (byId == null) {
            throw new RuntimeException(NON_EXISTENT_MEMBER);
        }
        return byId;
    }

    public Member deleteUser(long id) {
        Member byId = userRepository.findByMemberId(id);
        if (byId == null) {
            throw new RuntimeException(NON_EXISTENT_MEMBER);
        }
        userRepository.delete(byId);
        return byId;
    }
}
