package com.backend.producto.service.impl;

import com.backend.producto.dto.ProductoRequestDTO;
import com.backend.producto.dto.ProductoResponseDTO;
import com.backend.producto.model.Producto;
import com.backend.producto.mapper.ProductoMapper;
import com.backend.producto.repository.ProductoRepository;
import com.backend.producto.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    // Inyección de dependencias por constructor (mejor práctica sobre @Autowired)
    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        Producto producto = productoMapper.toEntity(requestDTO);
        Producto productoGuardado = productoRepository.save(producto);
        return productoMapper.toResponseDTO(productoGuardado);
    }

    @Override
    public List<ProductoResponseDTO> obtenerProductosActivos() {
        return productoRepository.findByActivoTrue()
                .stream()
                .map(productoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = buscarProductoPorIdOpcional(id);
        return productoMapper.toResponseDTO(producto);
    }

    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO) {
        Producto productoExistente = buscarProductoPorIdOpcional(id);

        productoExistente.setNombre(requestDTO.nombre());
        productoExistente.setDescripcion(requestDTO.descripcion());
        productoExistente.setPrecio(requestDTO.precio());
        productoExistente.setStock(requestDTO.stock());

        Producto productoActualizado = productoRepository.save(productoExistente);
        return productoMapper.toResponseDTO(productoActualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto productoExistente = buscarProductoPorIdOpcional(id);
        productoExistente.setActivo(false); // Borrado lógico
        productoRepository.save(productoExistente);
    }

    // Método privado auxiliar para reutilizar la lógica de búsqueda y manejo de errores
    private Producto buscarProductoPorIdOpcional(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con el ID: " + id));
    }
}