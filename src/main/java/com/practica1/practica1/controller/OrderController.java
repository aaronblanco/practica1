package com.practica1.practica1.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica1.practica1.model.CartModel;
import com.practica1.practica1.service.CartService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CartService cartService;

    public OrderController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(Authentication authentication) {
        if (cartService.getItems().isEmpty()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "No se ha podido crear el pedido porque el carrito está vacío"));
        }

        OrderResponse response = new OrderResponse(
                UUID.randomUUID().toString(),
                resolveUser(authentication),
                "pendiente",
                new ArrayList<>(cartService.getItems()),
                cartService.getTotal());

        cartService.clear();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private String resolveUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "guest";
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            return "guest";
        }
        return authentication.getName();
    }

    public static class OrderResponse {

        private String orderId;
        private String user;
        private String status;
        private Collection<CartModel> items;
        private BigDecimal total;

        public OrderResponse(String orderId, String user, String status, Collection<CartModel> items, BigDecimal total) {
            this.orderId = orderId;
            this.user = user;
            this.status = status;
            this.items = items;
            this.total = total;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Collection<CartModel> getItems() {
            return items;
        }

        public void setItems(Collection<CartModel> items) {
            this.items = items;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }
}
