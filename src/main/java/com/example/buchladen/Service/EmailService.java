package com.example.buchladen.Service;

import com.example.buchladen.Model.Order;
import com.example.buchladen.Utilities.PdfInvoiceGenerator;
import com.example.buchladen.web.dto.CartDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail (String subject, String text) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mybookshop20245@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendSimpleEmailTo(String recipientEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mybookshop20245@gmail.com");
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendInvoicePdf(Order order) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(order.getUser().getEmail());
            helper.setSubject("Rechnung fuer die Bestellung #" + order.getId());
            helper.setText("Hallo, \n\n Anbei finden Sie Ihre Rechnung.\n\nVielen Dank!");

            byte[] pdfBytes = PdfInvoiceGenerator.generate(order);
            helper.addAttachment("Rechnung-" + order.getId() + ".pdf",
                    () -> new java.io.ByteArrayInputStream(pdfBytes));

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw  new RuntimeException("Eine Fehler bei der Sendung der Email", e);
        }
    }
}
