package com.practica1.practica1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practica1.practica1.model.Producto;

public interface ProductoRepo extends JpaRepository<Producto, Long> {

    List<Producto> findByNameContainingIgnoreCase(String keyword);
}
