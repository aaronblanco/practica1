package com.practica1.practica1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practica1.practica1.model.Producto;
import com.practica1.practica1.repository.ProductoRepo;

import com.practica1.practica1.model.Producto;
import com.practica1.practica1.repository.ProductoRepo;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepo productRepo;

    public List<Producto> getAllProducts() {
        return productRepo.findAll();
    }

    public Producto getProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    public void saveProduct(Producto product) {
        productRepo.save(product);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

}
