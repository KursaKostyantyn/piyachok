package com.example.piyachok.services;

import com.example.piyachok.models.User;
import com.example.piyachok.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;
    private JwtUtils jwtUtils;

    public void sendMail(User user) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            String jwt=jwtUtils.generateTokenFromUsernameForActivationUser(user.getLogin());
            helper.setFrom("forjava2022@gmail.com");
            helper.setTo(user.getEmail());
            helper.setText("to activate account visit <a href='http://localhost:3000/activate?activateToken=" + jwt + "'>this</a> link", true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mimeMessage);

    }

    public void sendResetPasswordTokenMail(User user) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setFrom("forjava2022@gmail.com");
            helper.setTo(user.getEmail());
            helper.setText("for reset your password visit <a href='http://localhost:3000/resetPassword?userLogin=" + user.getLogin() + "&resetPasswordToken=" + user.getResetPasswordToken() + "'>this</a> link", true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

}
