package com.practica1.practica1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practica1.practica1.model.Producto;
import com.practica1.practica1.service.ProductoService;

@Controller
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    private ProductoService productService;

    // ===== WEB ENDPOINTS =====
    @GetMapping
    public String getProductsPage(Model model, @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Producto> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProducts(keyword);
        } else if (minPrice != null && maxPrice != null) {
            products = productService.filterProducts(keyword, minPrice, maxPrice);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Producto());
        return "product_form";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String name, @RequestParam double price) {
        Producto product = new Producto();
        product.setName(name);
        product.setPrice(price);
        productService.saveProduct(product);
        return "redirect:/admin";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam double price) {
        Producto product = productService.getProductById(id);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            productService.saveProduct(product);
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    // ===== REST API ENDPOINTS =====
    @GetMapping("/api")
    @ResponseBody
    public List<Producto> getAllProductsApi() {
        return productService.getAllProducts();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Producto getProductByIdApi(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/api")
    @ResponseBody
    public Producto createProductApi(@RequestBody Producto producto) {
        productService.saveProduct(producto);
        return producto;
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Producto updateProductApi(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        productService.saveProduct(producto);
        return producto;
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deleteProductApi(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
