package com.example.buchladen.Controller;

import com.example.buchladen.Enums.RequestStatus;
import com.example.buchladen.Enums.SenderType;
import com.example.buchladen.Model.*;
import com.example.buchladen.Service.*;
import com.example.buchladen.web.dto.ChatMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

   private final UserService userService;

    private final EmailService emailService;

    private final CustomerRequestServiceImpl customerRequestService;

    public AdminController(UserService userService, EmailService emailService, CustomerRequestServiceImpl customerRequestService) {
        this.userService = userService;
        this.emailService = emailService;
        this.customerRequestService = customerRequestService;
    }

    @GetMapping("/dashboard")
    public String getAdminDashboard() {
        return "admin_dashboard";
    }

    @GetMapping("users")
    public String getAllUsers (Model model) {

        List<User> users = userService.findAllUsers();

        model.addAttribute("users", users);
        return "admin-users_list";
    }

   @PostMapping("/users/{id}/enabled")
    public ResponseEntity<?> getUserDisabled(@PathVariable Long id,
                                             @RequestBody Map<String, Boolean> payload) {

        boolean enabled = payload.get("enabled");

        User user = userService.findById(id);

        userService.enabledUserActive(id, enabled);

       if (!enabled) {

           String text = "Sehr geehrte(r)" + user.getFirstName() + "\n\n" +
                   "Ihr Konto bei BookShop wurde vom Administrator deaktiviert.\n\n" +
                   "Sie können sich erst wieser anmelden, wenn Ihr Konto reaktiviert wurde.\n\n" +
                   "Wenn Sie glauben, dass dies ein Fehler ist, oder weitere Infomation benötigen,\n\n" +
                   "wenden Sie sich an unseren mybookshop20245@gmail.com\n\n" +
                   "Vielen Dank,\n\n" +
                   "Ihr MyBookShop-SupportTeam";

           emailService.sendSimpleEmailTo(user.getEmail(), "Dein Konto ist deaktiviert", text );
       }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user-requests")
    public String listRequest(Model model) {

        model.addAttribute("requests", customerRequestService.getAllRequests());
        return "admin-user-requests";
    }

    @PostMapping("/user-requests/{requestId}/status")
    public String changeStatus(@PathVariable Long requestId,
                               @RequestParam RequestStatus status,
                               @RequestParam (required = false) String answer
                               ) {
        customerRequestService.updateStatus(requestId, status);

        User admin = userService.findByCustomUsername("admin");

        if (answer != null && !answer.isEmpty()) {
            customerRequestService.saveAdminMessage(requestId, answer, admin);
        }
        return "redirect:/admin/user-requests";
    }

    @GetMapping("/user-requests/{requestId}/chat")
    public String viewRequestChat(@PathVariable Long requestId, Model model) {

        CustomerRequest request = customerRequestService.findById(requestId);

        List<ChatMessageDto> replies = request.getMessages().stream()
                        .map(msg -> {
                            ChatMessageDto dto = new ChatMessageDto();

                            boolean isAdmin = msg.getSender().getRoles().stream()
                                    .anyMatch(r -> r.getName().equalsIgnoreCase("ROLE_ADMIN"));

                            if(isAdmin) {
                               dto.setSenderName("Admin");
                            } else {
                               dto.setSenderName(
                                       msg.getSender().getFirstName() != null
                                       ? msg.getSender().getFirstName()
                                               : msg.getSender().getCustomUsername()
                               );
                            }

                            dto.setText(msg.getText());
                            dto.setSenderType(SenderType.ADMIN);
                            dto.setCreatedAt(msg.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                            return dto;
                        })
                .toList();

        model.addAttribute("request", request);
        model.addAttribute("messages", replies);
        model.addAttribute("requests", customerRequestService.getAllRequests());
        return "admin-user-request-chat";
    }
}


