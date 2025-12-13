package com.practica1.practica1.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.practica1.practica1.model.CartModel;
import com.practica1.practica1.model.Producto;
import com.practica1.practica1.service.CartService;
import com.practica1.practica1.service.ProductoService;

@Controller
@RequestMapping
public class ProductoController {

    @Autowired
    private ProductoService productService;
    @Autowired
    private CartService cartService;

    // ===== WEB ENDPOINTS =====
    @GetMapping("/products")
    public String getProductsPage(Model model, @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Producto> allProducts;
        if (keyword != null && !keyword.isEmpty()) {
            allProducts = productService.searchProducts(keyword);
        } else if (minPrice != null && maxPrice != null) {
            allProducts = productService.filterProducts(keyword, minPrice, maxPrice);
        } else {
            allProducts = productService.getAllProducts();
        }

        // Filtrar productos que ya están en el carrito
        Set<Long> inCartIds = cartService.getItems().stream()
                .map(CartModel::getProductId)
                .collect(Collectors.toSet());
        List<Producto> products = allProducts.stream()
                .filter(p -> !inCartIds.contains(p.getId()))
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Producto());
        return "product_form";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String name, @RequestParam double price) {
        Producto product = new Producto();
        product.setName(name);
        product.setPrice(price);
        product.setImageUrl("/images/default-product.jpg");
        productService.saveProduct(product);
        return "redirect:/admin";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam double price) {
        Producto product = productService.getProductById(id);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            productService.saveProduct(product);
        }
        return "redirect:/admin";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    // ===== REST API ENDPOINTS =====
    @GetMapping("/api/products")
    @ResponseBody
    public List<Producto> getAllProductsApi() {
        return productService.getAllProducts();
    }

    @GetMapping("/api/products/{id}")
    @ResponseBody
    public Producto getProductByIdApi(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/api/products")
    @ResponseBody
    public Producto createProductApi(@RequestBody Producto producto) {
        if (producto.getImageUrl() == null || producto.getImageUrl().isEmpty()) {
            producto.setImageUrl("/images/default-product.jpg");
        }
        productService.saveProduct(producto);
        return producto;
    }

    @PutMapping("/api/products/{id}")
    @ResponseBody
    public Producto updateProductApi(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        productService.saveProduct(producto);
        return producto;
    }

    @DeleteMapping("/api/products/{id}")
    @ResponseBody
    public void deleteProductApi(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
