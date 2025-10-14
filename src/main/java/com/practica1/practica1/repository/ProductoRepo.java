package com.practica1.practica1.repository;

import com.practica1.practica1.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepo extends JpaRepository<Producto, Long> {

}
