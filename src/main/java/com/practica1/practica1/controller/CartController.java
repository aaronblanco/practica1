package com.practica1.practica1.controller;

import com.practica1.practica1.model.Producto;
import com.practica1.practica1.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@SessionAttributes("cartItems")
public class CartController {

    private final ProductoService productService;

    public CartController(ProductoService productService) {
        this.productService = productService;
    }

    @ModelAttribute("cartItems")
    public List<Producto> cartItems() {
        return new ArrayList<>();
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long id,
            @ModelAttribute("cartItems") List<Producto> cartItems,
            RedirectAttributes ra) {
        Producto p = productService.getProductById(id); // o getProductById(id)
        if (p != null) {
            cartItems.add(p);
            ra.addFlashAttribute("msg", "Añadido al carrito");
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long id,
            @ModelAttribute("cartItems") List<Producto> cartItems,
            RedirectAttributes ra) {
        for (Iterator<Producto> it = cartItems.iterator(); it.hasNext();) {
            if (it.next().getId().equals(id)) {
                it.remove();
                break;
            }
        }
        ra.addFlashAttribute("msg", "Eliminado del carrito");
        return "redirect:/cart";
    }
}
