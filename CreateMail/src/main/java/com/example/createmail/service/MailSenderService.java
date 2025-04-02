package com.example.createmail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    @Autowired
    JavaMailSender javamailSender;

    /**
     * メールを送信するメソッド
     * 
     * @param email メールアドレス
     * @param subject 件名
     * @param message 本文
     */
    public void sendMail(String email, String subject, String message) {

        SimpleMailMessage simplemailmessage = new SimpleMailMessage();
        //送信元アドレスをセット
        //simplemailmessage.setFrom("yuhei23491518@gmail.com");
        //送信先アドレスをセット
        simplemailmessage.setTo(email);
        //件名をセット
        simplemailmessage.setSubject(subject);
        //本文をセット
        simplemailmessage.setText(message);

        //メールを送信
        javamailSender.send(simplemailmessage);
    }
    
}
