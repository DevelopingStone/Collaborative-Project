package com.zerobase.oriticket.domain.members.controller;

import com.zerobase.oriticket.domain.members.dto.user.UserRequest;
import com.zerobase.oriticket.domain.members.service.SendSmsService;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendSMSController {

    private final DefaultMessageService messageService;
    private final SendSmsService sendSmsService;

    @Autowired
    public SendSMSController(SendSmsService sendSmsService) {
        this.sendSmsService = sendSmsService;
        this.messageService = NurigoApp.INSTANCE.initialize("NCSO8SAGQJZTJ7OP", "WWZMQAXBFO5AOIASJF9YNOFAGZOF5CBT",
                "https://api.coolsms.co.kr");
    }

    /**
     * 단일 메시지 발송
     */
    @PostMapping("/send-one")
    public String sendOne(@RequestBody UserRequest phoneNum) {
        Message message = sendSmsService.sendSms(phoneNum);
        this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return message.getSubject();
    }
}
