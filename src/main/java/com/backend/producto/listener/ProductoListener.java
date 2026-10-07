package com.backend.producto.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.backend.producto.config.RabbitMQConfig;
import com.backend.producto.dto.OrdenEventoDTO;
import com.backend.producto.service.ProductoService;

@Component
public class ProductoListener {

    private final ProductoService productoService;

    public ProductoListener(ProductoService productoService) {
        this.productoService = productoService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STOCK)
    public void procesarActualizacionStock(OrdenEventoDTO evento) {
        // Validación de seguridad para descartar "Poison Messages" y evitar bucles infinitos
        if (evento == null || evento.getItems() == null) {
            System.err.println("Mensaje descartado por formato incorrecto o items nulos: " + evento);
            return; // Termina la ejecución exitosamente para que RabbitMQ saque el mensaje de la cola
        }

        System.out.println("Evento recibido en ms-producto para la orden ID: " + evento.getOrdenId());

        for (OrdenEventoDTO.ItemDTO item : evento.getItems()) {
            try {
                // Validación adicional por si un item individual viene incompleto
                if (item.getProductoId() != null && item.getCantidad() != null) {
                    productoService.descontarStockAsincrono(item.getProductoId(), item.getCantidad());
                } else {
                    System.err.println("Item descartado por datos incompletos (productoId o cantidad nulos)");
                }
            } catch (Exception e) {
                System.err.println("Error al actualizar stock del producto " + item.getProductoId() + ": " + e.getMessage());
            }
        }
    }
}