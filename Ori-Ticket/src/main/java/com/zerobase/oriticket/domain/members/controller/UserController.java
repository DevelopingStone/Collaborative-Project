package com.zerobase.oriticket.domain.members.controller;


import com.zerobase.oriticket.domain.members.dto.user.UserRequest;
import com.zerobase.oriticket.domain.members.dto.user.UserResponse;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.model.KakaoProfile;
import com.zerobase.oriticket.domain.members.model.OAuthToken;
import com.zerobase.oriticket.domain.members.service.KakaoAuthService;
import com.zerobase.oriticket.domain.members.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/members")
public class UserController {

    private KakaoAuthService kakaoAuthService;
    private UserService userService;
    private UserResponse userResponse;

    @GetMapping("/kakao/login")
    public ResponseEntity<KakaoProfile> handleKakao(@RequestParam String code) {
        OAuthToken oAuthToken = kakaoAuthService.requestKakaoToken(code);
        ResponseEntity<String> kakaoProfileResponse = kakaoAuthService.requestKakaoProfile(oAuthToken);
        KakaoProfile kakaoProfile = kakaoAuthService.registerOrUpdateKakaoUser(kakaoProfileResponse);
        return ResponseEntity.ok(kakaoProfile);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserRequest> signup(@RequestBody UserRequest userRequest) {
        System.out.println("회원가입");
        userService.updateUserByEmail(userRequest);
        return ResponseEntity.ok(userRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserResponse> signin(@RequestBody UserRequest userRequest) {
        System.out.println("로그인");
        boolean byEmail = userService.findByEmail(userRequest.getEmail());
        userRequest.setExistsByEmail(byEmail);
        userRequest.setId(userService.findById(byEmail,userRequest.getEmail()));
        return ResponseEntity.ok(userResponse.toEntity(userRequest));
    }

    @PatchMapping("/modify")
    public ResponseEntity<Member> updateUser(@RequestBody UserRequest userRequest) {
        System.out.println("회원수정");
        Member member = userService.updateUser(userRequest);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/withdraw/{id}")
    public ResponseEntity<Member> deleteUser(@PathVariable long id) {
        System.out.println("회원탈퇴");
        Member member = userService.deleteUser(id);
        return ResponseEntity.ok(member);
    }
}

