package com.backend.producto.repository;

import com.backend.producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Retorna únicamente los productos que no han sido eliminados de forma lógica
    List<Producto> findByActivoTrue();
    
}