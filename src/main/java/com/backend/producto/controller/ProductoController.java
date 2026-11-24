package com.backend.producto.controller;

import com.backend.producto.dto.ProductoRequestDTO;
import com.backend.producto.dto.ProductoResponseDTO;
import com.backend.producto.service.ProductoService;
import com.backend.producto.events.EventoUsuarioPublisher;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final EventoUsuarioPublisher eventos;

    public ProductoController(ProductoService productoService, EventoUsuarioPublisher eventos) {
        this.productoService = productoService;
        this.eventos = eventos;
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(requestDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductosActivos() {
        return ResponseEntity.ok(productoService.obtenerProductosActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProducto(@PathVariable Long id,
                                                               @AuthenticationPrincipal Jwt jwt) {
        ProductoResponseDTO producto = productoService.obtenerPorId(id);

        // Evento de clickstream: el usuario vio el detalle de un producto
        String nombreUsuario = jwt != null ? jwt.getClaimAsString("name") : null;
        eventos.publicar("PRODUCTO_VISTO", jwt != null ? jwt.getSubject() : null, nombreUsuario,
                producto.id(), producto.nombre(), producto.precio(), null);

        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id, 
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> actualizarStock(
            @PathVariable Long id, 
            @RequestParam Integer cantidadVariacion) {
        productoService.actualizarStock(id, cantidadVariacion);
        return ResponseEntity.ok().build();
    }
}