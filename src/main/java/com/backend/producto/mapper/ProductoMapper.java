package com.backend.producto.mapper;

import com.backend.producto.dto.ProductoRequestDTO;
import com.backend.producto.dto.ProductoResponseDTO;
import com.backend.producto.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return Producto.builder()
                .nombre(requestDTO.nombre())
                .descripcion(requestDTO.descripcion())
                .precio(requestDTO.precio())
                .stock(requestDTO.stock())
                // El id y las fechas son gestionados por la BD/Hibernate
                .activo(true) 
                .build();
    }

    public ProductoResponseDTO toResponseDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getActivo()
        );
    }
}