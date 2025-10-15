package com.practica1.practica1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practica1.practica1.model.Producto;
import com.practica1.practica1.service.ProductoService;

@Controller
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    private ProductoService productService;

    @GetMapping
    public String getProductsPage(Model model, @RequestParam(value = "search", required = false) String keyword) {
        List<Producto> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProducts(keyword);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Producto());
        return "product_form"; // templates/product_form.html
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String name, @RequestParam double price) {
        Producto product = new Producto();
        product.setName(name);
        product.setPrice(price);
        productService.saveProduct(product);
        return "redirect:/admin";  // Redirigir correctamente a /admin
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam double price) {
        Producto product = productService.getProductById(id);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            productService.saveProduct(product);
        }
        return "redirect:/admin";  // Redirigir correctamente a /admin
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin";  // Redirigir correctamente a /admin
    }

}
