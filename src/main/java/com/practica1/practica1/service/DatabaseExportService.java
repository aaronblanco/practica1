package com.practica1.practica1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import com.practica1.practica1.model.Producto;
import com.practica1.practica1.repository.ProductoRepo;

@Service
public class DatabaseExportService {

    private final ProductoRepo productoRepo;

    public DatabaseExportService(ProductoRepo productoRepo) {
        this.productoRepo = productoRepo;
    }


    public byte[] exportDatabaseToSql() {
        List<Producto> productos = productoRepo.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("-- Export de productos\n");
        sb.append("-- Registros: ").append(productos.size()).append('\n');
        sb.append("-- Generado por practica1\n\n");

        if (productos.isEmpty()) {
            sb.append("-- (sin datos)\n");
            return sb.toString().getBytes(StandardCharsets.UTF_8);
        }

        for (Producto p : productos) {
            Long id = p.getId();
            String nombre = escapeSql(p.getName());
            String precio = BigDecimal.valueOf(p.getPrice())
                    .setScale(2, RoundingMode.HALF_UP)
                    .toPlainString();

            sb.append("INSERT INTO producto (id, nombre, precio) VALUES (")
              .append(id != null ? id : "DEFAULT")
              .append(", '").append(nombre).append("', ")
              .append(precio).append(");\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeSql(String s) {
        if (s == null) return "";
        return s.replace("'", "''");
    }
}