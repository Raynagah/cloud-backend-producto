package com.backend.producto.dto;

import java.math.BigDecimal;

public record ProductoResponseDTO(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    Boolean activo
) {}