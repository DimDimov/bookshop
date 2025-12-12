package com.example.buchladen.Controller;


import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.ShippingRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.Service.CartService;
import com.example.buchladen.Service.CartServiceImpl;
import com.example.buchladen.Service.EmailService;
import com.example.buchladen.Service.UserService;
import com.example.buchladen.web.dto.ChangePasswordDto;
import com.example.buchladen.web.dto.ChangeUserNameDto;
import com.example.buchladen.web.dto.EmailChangeDto;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Controller
@RequestMapping("/my_account")
public class ChangeEmailUsernamePasswordController {


    private final UserService userService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final CartService cartService;

    private final MessageSource messageSource;


    public ChangeEmailUsernamePasswordController(UserService userService, EmailService emailService,
                                                 PasswordEncoder passwordEncoder, CartService cartService, MessageSource messageSource) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
        this.messageSource = messageSource;
    }

    @GetMapping("/change_options")
    public String enterAccount( Principal principal, Model model) {

        User user = userService.findByLoginIdentifier(principal.getName());

        if(!model.containsAttribute("changeUsernameDto")) {
            ChangeUserNameDto changeUserNameDto = new ChangeUserNameDto();
          changeUserNameDto.setUseEmailAsUsername(user.getUseEmailAsUsername());
            if(Boolean.TRUE.equals(user.getUseEmailAsUsername())) {
                changeUserNameDto.setEmail(user.getEmail());
            }
            else
                changeUserNameDto.setEmail(user.getCustomUsername());
            model.addAttribute("changeUsernameDto", changeUserNameDto);
        }

        if (!model.containsAttribute("changeEmailDto")){
            EmailChangeDto emailDto = new EmailChangeDto();
            model.addAttribute("changeEmailDto", emailDto);
        }

        if (!model.containsAttribute("changePasswordDto")) {
            ChangePasswordDto changePasswordDto = new ChangePasswordDto();
            model.addAttribute("changePasswordDto", changePasswordDto);
        }

        if (!model.containsAttribute("activeForm")) {
            model.addAttribute("activeForm", "");
        }

        boolean isAdmin = user.getRoles().stream().anyMatch(role-> role
                .getName().equals("ROLE_ADMIN"));

        int total = cartService.getTotalQuantityByUser(user);

model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("total", total);
        model.addAttribute("vorname", user.getFirstName());
        return "change_options";
    }

    @PostMapping("/change_email")
    public String processChangeEmail(@ModelAttribute("emailDto") @Valid EmailChangeDto emailDto,

                                     BindingResult result,
                                     Principal principal,
                                     Model model, Locale locale, RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByLoginIdentifier(principal.getName());

        if (user == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(emailDto.getPassword(), user.getPassword())) {

            String pass = messageSource.getMessage("password.invalid", null, locale);

            result.rejectValue("password", "error.emailDto", pass);
        }


        if (!emailDto.isMatchingEmails()) {

            String email = messageSource.getMessage("email.notCoincide", null, locale);

            result.rejectValue("confirmEmail", "error.emailDto", email);
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changeEmailDto", result);
            redirectAttributes.addFlashAttribute("changeEmailDto", emailDto);
            redirectAttributes.addFlashAttribute("activeForm", "form1");
            return "redirect:/my_account/change_options";
        }

        String newEmail = emailDto.getNewEmail();

        String email = messageSource.getMessage("email.success", null, locale);

        String text = "Deine E-Mail Adresse wurde erfolgreich geändert!\n\n" +
                "Neue E-Mail Adresse: " + newEmail + "\n\n" +
                "Falls du das nicht gemacht hast, kontaktiere bitte sofort mit unseren Support.\n\n" +
                "Viele Grüße, \nDein Buchladen-Team!";
        model.addAttribute("emailDto", emailDto);
        user.setEmail(emailDto.getNewEmail());
        userService.save(user);

        emailService.sendSimpleEmail("Bestätigung", text);
        redirectAttributes.addFlashAttribute("success_message", email);

        return "redirect:/login";

    }

    @PostMapping("/change_username")
    public String changeUsername(@ModelAttribute("changeUsernameDto") @Valid ChangeUserNameDto changeUsernameDto,
                                 BindingResult result,
                                 Principal principal,
                                 Model model, Locale locale,
                                 RedirectAttributes redirectAttributes)
    {

        if (principal == null) {
            return "redirect:/login";
        }


        User user = userService.findByLoginIdentifier(principal.getName());

        if (!passwordEncoder.matches(changeUsernameDto.getPassword(), user.getPassword())) {

            String pass = messageSource.getMessage("password.invalid", null, locale);

            result.rejectValue("password", "error.changeUsernameDto", pass);
        }

        if (Boolean.TRUE.equals(changeUsernameDto.getUseEmailAsUsername())) {
            user.setUseEmailAsUsername(true);
            user.setCustomUsername(null);
        }
        else {
            user.setUseEmailAsUsername(false);
            user.setCustomUsername(changeUsernameDto.getCustomUsername());
        }

        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changeUsernameDto", result);
            redirectAttributes.addFlashAttribute("changeUsernameDto", changeUsernameDto);
            redirectAttributes.addFlashAttribute("activeForm", "form2");
            changeUsernameDto.setUseEmailAsUsername(true);
            return "redirect:/my_account/change_options";
        }

        String username = messageSource.getMessage("username.success", null, locale);

        model.addAttribute("changeUsernameDto", changeUsernameDto);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success_message", username);
        return "redirect:/login";

    }

    @PostMapping("/change_password")
    public String changePassword(@ModelAttribute("changePasswordDto") @Valid ChangePasswordDto changePasswordDto,
                                 BindingResult result,
                                 Principal principal,
                                 Model model, Locale locale,
                                 RedirectAttributes redirectAttributes)

    {

        User user = userService.findByLoginIdentifier(principal.getName());

        if (!passwordEncoder.matches(changePasswordDto.getPassword(), user.getPassword())) {

            String pass = messageSource.getMessage("password.invalid", null, locale);

            result.rejectValue("password", "error.changePasswordDto", pass);
        }

        if (!changePasswordDto.isMatchingPassword()) {

            String pass1 = messageSource.getMessage("password.notCoincide", null, locale);
            result.rejectValue("confirmNewPassword", "error.changePasswordDto", pass1);
        }


        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordDto", result);
            redirectAttributes.addFlashAttribute("changePasswordDto", changePasswordDto);
            redirectAttributes.addFlashAttribute("activeForm", "form3");
            return "redirect:/my_account/change_options";
        }

        String text = "Dein Passwort wurde erfolgreich geändert!\n\n" +
            "Falls du das nicht gemacht hast, kontaktiere bitte sofort mit unseren Support.\n\n" +
            "Viele Grüße, \nDein Buchladen-Team!";

        String pass2 = messageSource.getMessage("password.success", null, locale);

        model.addAttribute("changePasswordDto", changePasswordDto);
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userService.save(user);
        emailService.sendSimpleEmail("Bestätigung", text);
        redirectAttributes.addFlashAttribute("success_message", pass2);
        return "redirect:/login";
    }
}
