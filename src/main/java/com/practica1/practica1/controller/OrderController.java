package com.practica1.practica1.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody(required = false) OrderRequest body, Authentication authentication) {
        Collection<CartModel> sessionItems = cartService.getItems();

        Collection<CartModel> items;
        BigDecimal total;

        if (sessionItems != null && !sessionItems.isEmpty()) {
            items = new ArrayList<>(sessionItems);
            total = cartService.getTotal();
        } else if (body != null && body.getProducts() != null && !body.getProducts().isEmpty()) {
            items = buildItemsFromPayload(body.getProducts());
            total = calculateTotal(items);
        } else {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "No se ha podido crear el pedido porque el carrito está vacío"));
        }

        OrderResponse response = new OrderResponse(
                UUID.randomUUID().toString(),
                resolveUser(authentication),
                "pendiente",
                items,
                total);

        cartService.clear();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private Collection<CartModel> buildItemsFromPayload(List<ProductPayload> products) {
        List<CartModel> items = new ArrayList<>();
        for (ProductPayload p : products) {
            if (p == null || p.getProductoId() == null) {
                continue;
            }
            BigDecimal unitPrice = BigDecimal.valueOf(p.getPrice() != null ? p.getPrice() : 0.0d);
            String name = p.getProductoNombre() != null ? p.getProductoNombre() : "Producto";
            items.add(new CartModel(p.getProductoId(), name, unitPrice, 1));
        }
        return items;
    }

    private BigDecimal calculateTotal(Collection<CartModel> items) {
        return items.stream()
                .map(CartModel::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public static class OrderRequest {

        private List<ProductPayload> products;

        public List<ProductPayload> getProducts() {
            return products;
        }

        public void setProducts(List<ProductPayload> products) {
            this.products = products;
        }
    }

    public static class ProductPayload {

        private Long productoId;
        private String productoNombre;
        private Double price;
        private String imageUrl;

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public String getProductoNombre() {
            return productoNombre;
        }

        public void setProductoNombre(String productoNombre) {
            this.productoNombre = productoNombre;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
