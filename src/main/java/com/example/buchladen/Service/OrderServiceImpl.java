package com.example.buchladen.Service;


import com.example.buchladen.Enums.OrderStatus;
import com.example.buchladen.Model.*;
import com.example.buchladen.Repositories.*;
import com.example.buchladen.web.dto.ShippingRegistrationDto;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ShippingRepository shippingRepository;
    private final StockServiceImpl stockServiceImpl;
    private final StockRepository stockRepository;
    private final OrderItemsRepository orderItemsRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, CartService cartService, ShippingRepository shippingRepository, StockServiceImpl stockServiceImpl, StockRepository stockRepository, OrderItemsRepository orderItemsRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.shippingRepository = shippingRepository;
        this.stockServiceImpl = stockServiceImpl;
        this.stockRepository = stockRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public List<Order> findOrdersByUser(User user){
        return orderRepository.findByUser(user);
    }

    public User getCurrentUser( ){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmailOrCustomUsername(login, login)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
    }

    @Override
    public Order findOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElseThrow(() ->
                new RuntimeException ("Der Order nicht gefunden"));
    }

    @Transactional
    @Override
    public Order createOrUpdateOrder(User user, ShippingDetails shipping) {
        Order order = orderRepository.findOpenOrderByUser(user)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(OrderStatus.NEW);
                    newOrder.setOrderDate(LocalDateTime.now());
                    return orderRepository.save(newOrder);
                });

        order.setShippingDetails(shipping);

        Cart cart = cartService.getCartForUser(user);
        for(CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());

            Stock stock = stockRepository.findByBook(cartItem.getBook())
                            .orElseThrow(() -> new RuntimeException("Kein Buch im Lager: " + cartItem.getBook().getTitle()));
            orderItem.setStock(stock);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setOrder(order);

            order.getItems().add(orderItem);
        }
        order.calculateTotalAmount();

        orderRepository.save(order);

        return order;
    }


/*
@Transactional
    public Order setDeliveryAsBilling() {

        User user = getCurrentUser();

        ShippingDetails  shipping = user.getShippingDetailsList().getFirst();

       return createOrUpdateOrder(user, shipping);
    }

    @Transactional
    public void saveOrUpdateDelivery(ShippingRegistrationDto dto) {
        User user = getCurrentUser();

        ShippingDetails shipping;

        if(dto.getId() != null) {
            shipping = shippingRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Adresse nicht gefunden"));

            shipping.setFirstName(dto.getFirstName());
            shipping.setLastName(dto.getLastName());
            shipping.setStreet(dto.getStreet());
            shipping.setHouseNumber(dto.getHouseNumber());
            shipping.setPostcode(dto.getPostcode());
            shipping.setTown(dto.getTown());
            shipping.setCountry(dto.getCountry());
        } else {

            Optional<ShippingDetails> existing = user.getShippingDetailsList().stream()
                    .filter(a -> a.getFirstName().equals(dto.getFirstName())
                            && a.getLastName().equals(dto.getLastName())
                            && a.getStreet().equals(dto.getStreet())
                            && a.getHouseNumber().equals(dto.getHouseNumber())
                            && a.getPostcode().equals(dto.getPostcode())
                            && a.getTown().equals(dto.getTown())
                            && a.getCountry().equals(dto.getCountry()))
                    .findFirst();

            if (existing.isPresent()) {
                shipping = existing.get();
            } else {

                shipping = new ShippingDetails();
                shipping.setFirstName(dto.getFirstName());
                shipping.setLastName(dto.getLastName());
                shipping.setStreet(dto.getStreet());
                shipping.setHouseNumber(dto.getHouseNumber());
                shipping.setPostcode(dto.getPostcode());
                shipping.setTown(dto.getTown());
                shipping.setCountry(dto.getCountry());
                shipping.setUser(user);
            }
        }
        shippingRepository.save(shipping);
        createOrUpdateOrder(user, shipping);
    }
*/


    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }


    @Override
    public void completeOrder(Order order) {
        order.setPaid(true);
        order.setOrderDate(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        stockServiceImpl.reduceStock(savedOrder);
    }

   /* public void returnOrder(Order order) {
        order.setReturned(true);
        orderRepository.save(order);
        stockServiceImpl.restoreStock(order);
    }

    public double calculateCostAllBoughtBooks() {
        List<OrderItem> items = orderItemsRepository.findAll();
        double boughtBooksPrice = 0.0;
        for(OrderItem item : items) {
            boughtBooksPrice += item.getQuantity() * item.getBook().getPrice().doubleValue();
        }
        return boughtBooksPrice;
    }*/

    @Override
    public double calculateProfit() {
        List<OrderItem> allItems = orderItemsRepository.findAll();
        return allItems.stream()
                .filter(item -> !item.isReturned())
                .mapToDouble(item -> item.getPrice().doubleValue())
                .sum();
    }

    @Override
    public boolean existsByShippingDetails(ShippingDetails shippingDetails) {
        return orderRepository.existsByShippingDetails(shippingDetails);
    }

}
