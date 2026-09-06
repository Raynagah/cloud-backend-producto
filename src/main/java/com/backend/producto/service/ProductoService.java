package com.backend.producto.service;

import com.backend.producto.dto.ProductoRequestDTO;
import com.backend.producto.dto.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {
    ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO);
    List<ProductoResponseDTO> obtenerProductosActivos();
    ProductoResponseDTO obtenerPorId(Long id);
    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO);
    void eliminarProducto(Long id);
    void actualizarStock(Long id, Integer cantidadVariacion);
}
