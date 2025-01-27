package com.example.ecommerce.Util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    public Boolean sendMail(String url, String recipientEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper =new MimeMessageHelper(mimeMessage);

        helper.setFrom("shreeghodke123@gmail.com","shopping cart");
        helper.setTo(recipientEmail);

        String content = "<p>Hello,</p>"
                +"<p>you have requested to reset your password </p>"
                +"<p>Click the below link, to change your password</p>"
                +"<p><a href=\""+url+"\">Change my password</a></p>";
        helper.setSubject("password reset");
        helper.setText(content, true);
        mailSender.send(mimeMessage);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
