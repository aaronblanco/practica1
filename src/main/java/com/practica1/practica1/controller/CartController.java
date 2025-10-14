package com.practica1.practica1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practica1.practica1.model.Producto;
import com.practica1.practica1.service.CartService;
import com.practica1.practica1.service.ProductoService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductoService productService;
    private final CartService cartService;

    public CartController(ProductoService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping
    public String cart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long id,
            @RequestParam(name = "qty", defaultValue = "1") int qty,
            RedirectAttributes ra) {

        Producto p = productService.getProductById(id);
        if (p != null) {
            cartService.add(p, Math.max(1, qty));
            ra.addFlashAttribute("msg", "Añadido al carrito");
        } else {
            ra.addFlashAttribute("error", "Producto no encontrado");
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long id, RedirectAttributes ra) {
        cartService.remove(id);
        ra.addFlashAttribute("msg", "Eliminado del carrito");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(RedirectAttributes ra) {
        cartService.clear();
        ra.addFlashAttribute("msg", "Carrito vaciado");
        return "redirect:/cart";
    }
}
