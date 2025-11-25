package com.example.buchladen.Controller;


import com.example.buchladen.Model.User;
import com.example.buchladen.Service.CustomUserDetailsService;
import com.example.buchladen.Service.UserService;
import com.example.buchladen.Utilities.Utility;
import com.example.buchladen.web.dto.UserRegistrationDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import net.bytebuddy.utility.RandomString;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController  {

  @Autowired
    private  JavaMailSender mailSender;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

       try {
            customUserDetailsService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "Wir haben einen Link zum Zurücksetzen des Passworts an Ihre E-Mail geschickt. Bitte überprüfen Sie das.");
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
           model.addAttribute("error", "Fehler beim Senden einer E-Mail.");
       }
        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link)
        throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("mybookshop2024@gmail.com", "Setzen das Passwort zurück. ");
        helper.setTo(recipientEmail);

        String subject = "Here ist ein Link, um Ihr Passwort zurückzusetzen";

        String content = "<p>Hallo,</p>"
                + "<p>Sie haben beantragt, Ihr Passwort zurückzusetzen.</p>"
                + "Klicken Sie das Link unten, um Ihr Passwort zurückzusetzen:</p>"
                + "<p><a href=\"" + link + "\">Mein Passwort ändern</a></p>"
                + "<br>"
                + "<p>Ignorieren Sie diese E-Mail, wenn Sie sich an Ihr Passwort erinnern können, "
                + "oder Sie haben den Antrag nicht gestellt.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);

    }

    @GetMapping("/reset_password")
    public String showResetPassword(@Param(value = "token") String token, Model model) {

        User user = customUserDetailsService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        if (user == null) {
            model.addAttribute("message", "Ungültiges Token");
            return "reset_password";
        }
        return "reset_password";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = customUserDetailsService.getByResetPasswordToken(token);
        //model.addAttribute(/"title", "Reset your password");

        if(user == null) {
            model.addAttribute("message", "Ungültiges Token");
            return "reset_password";
        } else {
            customUserDetailsService.updatePassword(user, password);
            model.addAttribute("success_message", "Sie haben Ihr Passwort erfolgreich geändert");
        }
            return "reset_password";
    }


}
