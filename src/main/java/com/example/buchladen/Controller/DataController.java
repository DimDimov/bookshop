package com.example.buchladen.Controller;


import com.example.buchladen.Enums.RequestReason;
import com.example.buchladen.Enums.RequestStatus;
import com.example.buchladen.Enums.RequestType;
import com.example.buchladen.Enums.SenderType;
import com.example.buchladen.Mapper.UserMapper;
import com.example.buchladen.Model.*;
import com.example.buchladen.Repositories.*;
import com.example.buchladen.Service.*;
import com.example.buchladen.web.dto.ChatMessageDto;
import com.example.buchladen.web.dto.UserDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import java.util.stream.Collectors;

@Controller
@RequestMapping("/my_account")
public class DataController {


    private final CustomerRequestServiceImpl customerRequestService;
    private final UserService userService;
    private final OrderService orderService;
    private final BookService bookService;
    private final RequestMessageService requestMessageService;

    private final UserMapper userMapper;

    public DataController(CustomerRequestServiceImpl customerRequestService, UserService userService, OrderService orderService, BookService bookService, RequestMessageService requestMessageService, UserMapper userMapper) {

        this.customerRequestService = customerRequestService;
        this.userService = userService;
        this.orderService = orderService;
        this.bookService = bookService;
        this.requestMessageService = requestMessageService;
        this.userMapper = userMapper;
    }

    @GetMapping("/my_data")
    @PreAuthorize("hasRole('USER')")
    public String getData(Authentication authentication,  Model model) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return  "redirect:/login";
        }
        String email = authentication.getName();

       User user = userService.findByEmailOrCustomUsername(email, email)
               .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));


      /*  UserDto userDto = userMapper.toDto(user);// for test*/

       ShippingDetails shipping = null;
        if(!user.getShippingDetailsList().isEmpty()) {
            shipping = user.getShippingDetailsList().stream()
                            .filter(ShippingDetails::isDefault)
                                    .findFirst()
                                            .orElse(user.getShippingDetailsList(). getFirst());
        }

     /*   model.addAttribute("user", userDto); //use for test*/
        model.addAttribute("shipping", shipping);
        return "myData";
    }

    @GetMapping("/my_orders")
    public String getOrders(Model model, Authentication authentication) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return  "redirect:/login";
        }

        String email = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        List<Order> orders = orderService.findOrdersByUser(user);

        model.addAttribute("orders", orders);

        return "my_orders";
    }

    @GetMapping( "/change_data")
    public String getUserName (Authentication authentication,  Model model) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return  "redirect:/login";
        }

        String email = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        UserDto userDto = userMapper.toDto(user);//for test*/

       model.addAttribute("user", userDto);//for test;*/

        return "changeData";
    }

    @RequestMapping(value = "/change_data", method = RequestMethod.POST)
    public String saveUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,
                           Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String email = authentication.getName();


        Optional<User> otherUser = userService.findByEmailOrCustomUsername(email, email);

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "changeData";
        }

        if (otherUser.isPresent()) {

            User existingUser = otherUser.get();
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setStreet(userDto.getStreet());
            existingUser.setHouseNumber(userDto.getHouseNumber());
            existingUser.setTown(userDto.getTown());
            existingUser.setPostcode(userDto.getPostcode());
            existingUser.setCountry(userDto.getCountry());
            existingUser.setCustomUsername(userDto.getCustomUsername());

            userService.save(existingUser);
        } else {
            return "redirect:/login";
        }
        return "redirect:/my_account/my_data";
    }

    @GetMapping("/formular")
    public String getFormular(Authentication authentication, Model model) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return  "redirect:/login";
        }

        String email = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        List<Order> orders = orderService.findOrdersByUser(user);
       /* UserDto userDto = userMapper.toDto(user);//for test*/

        model.addAttribute("orders", orders);
     /*   model.addAttribute("user", userDto);//for test*/
        return "formular-page";
    }


    @PostMapping("/formular")
    public String sendRequest(Authentication authentication,
                              @RequestParam RequestType type, @RequestParam RequestReason reason, @RequestParam Long orderId,
                              @RequestParam String message)

    {
        if(authentication == null || !authentication.isAuthenticated()) {
            return  "redirect:/login";
        }


        String email = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        Order order = orderService.findOrderById(orderId);

       Optional <CustomerRequest> existingRequest = customerRequestService.findByCustomerAndOrder(user, order);

        Book book = order.getItems().getFirst().getBook();

       CustomerRequest request;

       if(existingRequest.isPresent()) {
           request = existingRequest.get();
           RequestMessage msg = new RequestMessage();
           msg.setRequest(request);
           msg.setSender(user);
           msg.setText(message);
           msg.setCreatedAt(LocalDateTime.now());
           msg.setId(book.getId());
           msg.setSenderType(SenderType.CUSTOMER);
           request.addMessage(msg);
       } else {

           request = new CustomerRequest();
           request.setType(type);
           request.setMessage(message);
           request.setCreatedAt(LocalDateTime.now());
           request.setStatus(RequestStatus.NEW);
           request.setCustomer(user);
           request.setReason(reason);
           request.setOrder(order);

           RequestMessage msg = new RequestMessage();
           msg.setRequest(request);
           msg.setSender(user);
           msg.setText(message);
           msg.setCreatedAt(LocalDateTime.now());
           msg.setBook(book);
           msg.setSenderType(SenderType.CUSTOMER);
           request.addMessage(msg);
       }

        customerRequestService.save(request);

        return "redirect:/my_account/formular";
    }

    @GetMapping("/result")
    @PreAuthorize("hasRole('USER')")
    String getResult(Authentication authentication, Model model) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return  "redirect:/login";
        }

        String email = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

  List<Order> orders = orderService.findOrdersByUser(user);
       /* UserDto userDto = userMapper.toDto(user);//for test*/

        model.addAttribute("orders", orders);

       /* model.addAttribute("user", userDto);//for test*/
        return "result";
    }

    @GetMapping({"/user-talks", "/user-talks/{requestId}"})
    String getUsersRequest(Model model, Authentication authentication, @PathVariable(required = false) Long requestId) {
        String email = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        List<Order> orders = orderService.findOrdersByUser(user);

        List<CustomerRequest> requests = customerRequestService.findByCustomer(user);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (CustomerRequest req : requests) {
            String bookSummary = req.getOrder().getItems().stream()
                    .map(item -> item.getBook().getTitle() + " , " + item.getBook().getAuthor().getName() + " (" + item.getOrder().getOrderDate().format(dtf) + ")")
                    .collect(Collectors.joining(", "));

            req.setBookSummary(bookSummary);
        }

        if (requestId != null) {
            CustomerRequest currentRequest = customerRequestService.findById(requestId);

            List<RequestMessage> messages = currentRequest.getMessages();
            customerRequestService.markMessageAsRead(user.getId(), requestId);



            model.addAttribute("openRequestId", requestId);

            model.addAttribute("messages", messages);
        } else {
            model.addAttribute("openRequestId", null);

            model.addAttribute("messages", Collections.emptyList());
        }
      /*  UserDto userDto = userMapper.toDto(user);//for test*/
        model.addAttribute("requests", requests);

        model.addAttribute("orders", orders);
      /*  model.addAttribute("user", userDto);// for test*/

        return "user_talks_request";
    }

    @PostMapping("/user/messages/mark-read/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public void markMessagesAsRead(@PathVariable Long requestId, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        customerRequestService.markMessageAsRead( user.getId(), requestId);
    }

    @GetMapping("/user/messages/{requestId}")
    @ResponseBody
    @Transactional
    public List<ChatMessageDto> getMessages(@PathVariable Long requestId, Authentication authentication) {

      String email = authentication.getName();
        User currentUser = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        CustomerRequest req = customerRequestService.findById(requestId);


      List<RequestMessage> toUpdate = req.getMessages().stream()
              .filter(msg -> !msg.isReadByUser() && msg.getSender() != null && !msg.getSender().equals(currentUser))
              .peek(msg -> msg.setReadByUser(true))
              .toList();

      if(!toUpdate.isEmpty()) {
          requestMessageService.saveAll(toUpdate);
      }

        List<ChatMessageDto> result = new ArrayList<>();

        Long customerId = req.getCustomer().getId();

        ChatMessageDto  chat = new ChatMessageDto();
        chat.setText(req.getMessage());
       chat.setSenderName(customerId.equals(currentUser.getId()) ? "You" : req.getCustomer().getCustomUsername());
        chat.setFromAdmin(false);
        chat.setId(req.getCustomer().getId());
        chat.setCreatedAt(req.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        result.add(chat);

        List<ChatMessageDto> replies = req.getMessages().stream()
                .map(msg -> {
                    ChatMessageDto dto = new ChatMessageDto();

                    Long senderId = msg.getSender().getId();

                    dto.setId(msg.getSender().getId());
                    dto.setText(msg.getText());
                    dto.setFromAdmin(msg.getSender().getRoles()
                            .stream()
                            .anyMatch(r -> r.getName().equalsIgnoreCase("ROLE_ADMIN")));

                    dto.setSenderName(
                            dto.isFromAdmin() ? "Admin" :

                                   msg.getSender() != null && senderId.equals(currentUser.getId()) ? "You" :
                                            msg.getSender() != null ? msg.getSender().getCustomUsername() :
                                            req.getCustomer() != null ? req.getCustomer().getCustomUsername()  : "Unknown"
                    );

                    dto.setCreatedAt(msg.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

                    return dto;
                })
                .toList();

        result.addAll(replies);

        return result.stream()

                .sorted(Comparator.comparing(ChatMessageDto::getCreatedAt))
                .collect(Collectors.toList());
    }

    @GetMapping("/preferences")
    public String viewMyBooks(Authentication authentication, Model model) {

        if(authentication == null || !authentication.isAuthenticated()) {
            return  "redirect:/login";
        }

        String email = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
      /*  UserDto userDto = userMapper.toDto(user);//for test*/
      /*  model.addAttribute("user", userDto); //for test*/
        model.addAttribute("myBooks", user.getPreferredBooks());
        return "preferences_books";
    }

}


