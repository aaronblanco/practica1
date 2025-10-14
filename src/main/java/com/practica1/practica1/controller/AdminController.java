package com.practica1.practica1.controller;

import com.practica1.practica1.service.DatabaseExportService;
import com.practica1.practica1.service.ProductoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class AdminController {

    private final ProductoService productoService;
    private final DatabaseExportService exportService;

    public AdminController(ProductoService productoService,
            DatabaseExportService exportService) {
        this.productoService = productoService;
        this.exportService = exportService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productoService.getAllProducts());
        return "admin";
    }

    @GetMapping("/admin/download-db-sql")
    public ResponseEntity<ByteArrayResource> downloadDbSql() {
        byte[] data = exportService.exportDatabaseToSql();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String filename = "productos-" + timestamp + ".sql";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/sql"))
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
