package com.practica1.practica1.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.practica1.practica1.model.CartModel;
import com.practica1.practica1.model.Producto;

@Service
@SessionScope
public class CartService implements Serializable {

    private final Map<Long, CartModel> items = new LinkedHashMap<>();

    public void add(Producto p, int qty) {
        if (p == null) {
            return;
        }
        int toAdd = Math.max(1, qty);

        Long id = p.getId();
        BigDecimal unitPrice = BigDecimal.valueOf(p.getPrice());

        CartModel existing = items.get(id);
        if (existing == null) {
            CartModel ci = new CartModel(id, p.getName(), unitPrice, toAdd);
            items.put(id, ci);
        } else {
            existing.setQuantity(existing.getQuantity() + toAdd);
        }
    }

    public void remove(Long productId) {
        items.remove(productId);
    }

    public Collection<CartModel> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .map(CartModel::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear() {
        items.clear();
    }

    public int getItemCount() {
        return items.values().stream().mapToInt(CartModel::getQuantity).sum();
    }

    public int getUniqueItemCount() {
        return items.size();
    }
}
