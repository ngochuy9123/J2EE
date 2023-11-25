package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username")
    private String sender;

    @Override
    public void sendSimpleEmail(String email,String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(email);

        String content = "Ban da hoan tat thu tuc dang ki. De co the dang nhan ban can xac nhan ma OTP: \n"+otp;


        mailMessage.setText(content);

        mailMessage.setSubject("Thong bao Dang ki thanh cong");

        javaMailSender.send(mailMessage);
    }
}
