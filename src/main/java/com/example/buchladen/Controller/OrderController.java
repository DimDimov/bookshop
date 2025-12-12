package com.example.buchladen.Controller;

import com.example.buchladen.Enums.PaymentMethod;
import com.example.buchladen.Mapper.OrderMapper;
import com.example.buchladen.Mapper.UserMapper;
import com.example.buchladen.Model.*;
import com.example.buchladen.Service.*;
import com.example.buchladen.web.dto.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {


    private final OrderServiceImpl orderService;
    private final OrderMapper orderMapper;
    private final EmailService emailService;
    private final CartService cartService;
    private final PaypalService paypalService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ShippingService shippingService;
    private final MessageSource messageSource;

    public OrderController(OrderServiceImpl orderService,  OrderMapper orderMapper, EmailService emailService, CartService cartService, PaypalService paypalService, UserMapper userMapper,
                           UserService userService, ShippingService shippingService, MessageSource messageSource) {

        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.emailService = emailService;
        this.cartService = cartService;
       this.paypalService = paypalService;
        this.userMapper = userMapper;
        this.userService = userService;
        this.shippingService = shippingService;
        this.messageSource = messageSource;
    }

    @GetMapping("/order_address")
    public String getOrder(Model model, Authentication authentication){

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow();

        Cart cart = cartService.getCartForUser(user);

        CartDto cartDto = cartService.convertToCartDto(cart);

        DeliveryDto deliveryDto = new DeliveryDto();
       user.getShippingDetailsList().stream()
              .filter(ShippingDetails::isDefault)
              .findFirst()
              .ifPresent(addr -> {
                  deliveryDto.setStreet(addr.getStreet());
                  deliveryDto.setFirstName(addr.getFirstName());
                  deliveryDto.setLastName(addr.getLastName());
                  deliveryDto.setCountry(addr.getCountry());
                  deliveryDto.setPostcode(addr.getPostcode());
                  deliveryDto.setHouseNumber(addr.getHouseNumber());
                  deliveryDto.setTown(addr.getTown());
              });

        model.addAttribute("deliveryDto", deliveryDto);
        model.addAttribute("cartPrice", cartDto.getTotalPrice());
        model.addAttribute("cart", cart);
        model.addAttribute("step", 2);

        return "order-page";
    }

    @PostMapping("/order_address")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendOrder(@RequestBody Map<String, Boolean> request) {

        boolean useBilling = request.getOrDefault("useBillingAsDelivery", false);

        User user = orderService.getCurrentUser();

      ShippingDetails shipping = useBilling ? user.getShippingDetailsList().getFirst()
              : new ShippingDetails();

      Order order = orderService.createOrUpdateOrder(user, shipping);

      Map<String, Object> response = new HashMap<>();
      response.put("orderId", order.getId());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/delivery_new_or_saved_addresses")
    public String getDeliveryAddresses(Model model, Authentication authentication) {

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow( () -> new RuntimeException("Benutzer nicht gefunden."));

        Cart cart = cartService.getCartForUser(user);

        CartDto cartDto = cartService.convertToCartDto(cart);

        model.addAttribute("cartPrice", cartDto.getTotalPrice());
        model.addAttribute("step", 2);
        model.addAttribute("savedAddresses", user.getShippingDetailsList());
        return "delivery-addresses";
    }

    @GetMapping("/get_address/{id}")
    public ResponseEntity<ShippingDetails> getAddress(@PathVariable Long id) {
        ShippingDetails addr = shippingService.findById(id);
        return ResponseEntity.ok(addr);
    }

    @PostMapping("/save_address")
    @ResponseBody
    public ResponseEntity<?> saveAddress(@Valid @RequestBody ShippingRegistrationDto dto, BindingResult result, Authentication authentication) {


        if(result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
                    result.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow( () -> new RuntimeException("Benutzer nicht gefunden."));

        ShippingDetails details = new ShippingDetails();
        details.setFirstName(dto.getFirstName());
        details.setLastName(dto.getLastName());
        details.setCountry(dto.getCountry());
        details.setTown(dto.getTown());
        details.setPostcode(dto.getPostcode());
        shippingService.save(details);

        Order order = orderService.createOrUpdateOrder(user, details);

        return ResponseEntity.ok(order.getId());
    }

    @GetMapping("/choose-payment/{id}")

    public String getPayment(@PathVariable Long id, Model model, Authentication authentication) {

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));

        Order order = orderService.findOrderById(id);

       OrderDto orderDto = orderMapper.toOrderDto(order);

       if(orderDto.getPaymentMethod() == null) {
           orderDto.setPaymentMethod(PaymentMethod.RECHNUNG);
       }

        Cart cart = cartService.getCartForUser(user);

        CartDto cartDto = cartService.convertToCartDto(cart);


      model.addAttribute("cartPrice", cartDto.getTotalPrice());
        model.addAttribute("step", 3);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        model.addAttribute("order", orderDto);
        return "choose-payment-page";
    }

    @PostMapping("/choose-payment")
    public String choosePayment(@ModelAttribute("order") OrderDto orderDto) {

        Order order = orderService.findOrderById(orderDto.getId());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        orderService.save(order);

        return "redirect:/order/confirmation-page/" + order.getId();
    }

    @GetMapping("/confirmation-page/{id}")
    public String getConfirmation(@PathVariable Long id,  Model model, Authentication authentication){
        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));

        Cart cart = cartService.getCartForUser(user);

        CartDto cartDto = cartService.convertToCartDto(cart);

        Order order = orderService.findOrderById(id);

        List<OrderItem> items =  order.getItems();

        model.addAttribute("cartPrice", cartDto.getTotalPrice());
        model.addAttribute("step", 4);
        model.addAttribute("order", order);
        model.addAttribute("deliveryAddress", order.getShippingDetails());
        model.addAttribute("paymentMethod", PaymentMethod.values());
        model.addAttribute("invoiceAddress", user);
        model.addAttribute("items", items);

        return "confirmation-page";
    }

    @PostMapping("/confirmation-page")
    public String confirmPayment(@RequestParam Long orderId, @RequestParam String method,
    RedirectAttributes redirectAttributes) throws PayPalRESTException {

     /*   String login = authentication.getName();

        Optional<User> optionalUser = userService.findByEmailOrCustomUsername(login, login);
        if (optionalUser.isEmpty()) {
            throw  new RuntimeException("Benutzer nicht gefunden");
        }*/

        Order order = orderService.findOrderById(orderId);
        if ("RECHNUNG".equals(method)) {
            emailService.sendInvoicePdf(order);
            orderService.completeOrder(order);

            redirectAttributes.addFlashAttribute("send1", "Ihr Einkauf war erfolgreich!");
            redirectAttributes.addFlashAttribute("send4", order.getUser().getEmail());
            redirectAttributes.addFlashAttribute("send2", " Die PDF Rechnung wurde an folgende Adresse  ");
            redirectAttributes.addFlashAttribute("send3", " geschickt.");
            return "redirect:/order/payment-result";
        } else if ("PAYPAL".equals(method)) {
            Payment payment = paypalService.createPayment(
                    order.getTotalAmount(),
                    "EUR",
                    "paypal",
                    "sale",
                    "Order #" + order.getId(),
                    "http://localhost:8078/order/cancel",
                    "http://localhost:8078/order/payment-result"

            );

            for (Links link : payment.getLinks()) {
                if("approval_url".equals(link.getRel())) {
                    return  "redirect:" + link.getHref();
                }
            }
            orderService.completeOrder(order);
        }

return "redirect:/order/error";
    }

    @GetMapping("/payment-result")
    public String getSuccessPaymentPage(
            @RequestParam(value = "paymentId", required = false) String paymentId,
            @RequestParam(value = "PayerID", required = false) String payerId,
            Authentication authentication, Model model, Locale locale) throws PayPalRESTException {

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow( () -> new RuntimeException("Benutzer nicht gefunden"));

        Order order = orderService.findOrderById(user.getOrders().getLast().getId());

        if(order.getPaymentMethod() == PaymentMethod.PAYPAL) {
            if(payerId == null) {
                throw  new RuntimeException("paypal parameters missing!");
            }

            Payment executedPayment = paypalService.executePayment(paymentId, payerId);

            if (!"approved".equals(executedPayment.getState())) {
                throw new RuntimeException("Payment not approved!");
            }

            model.addAttribute("send2", " Ihre Zahlung mit PayPal wurde angeschlossen.");

            orderService.completeOrder(order);

        } else if (order.getPaymentMethod() == PaymentMethod.RECHNUNG) {

            String send2 = messageSource.getMessage("order.success1", null, locale);

            model.addAttribute("send2", send2);

        } else if (order.getPaymentMethod() == PaymentMethod.BANKKARTE) {
            model.addAttribute("send2", " Ihre Zahlung per Bankkarte abgeschlossen.");

        }

        UserDto userDto = userMapper.toDto(user);
      /*  emailService.sendInvoicePdf(order);*/

        cartService.clearCart(user);

        String send1 = messageSource.getMessage("order.success", null, locale);

        model.addAttribute("send1", send1);

        model.addAttribute("send3", "Transaktions-ID: " + order.getId());
        model.addAttribute("user", userDto);

        return "payment-result";
    }

    @GetMapping("/cancel")
    public String cancelPayment(Model model) {

        model.addAttribute("message", "die Bezahlung war abgesagt. Sie koennen noch einmal probieren.");
        model.addAttribute("orderId", orderService.getCurrentUser().getId());
        return "cancel-payment-page";
    }

}
