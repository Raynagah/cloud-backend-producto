package com.backend.producto.events;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Publica eventos de comportamiento del usuario (clickstream) en el topic Kafka "user-events".
 * Los consume ms-analytics-recommendations.
 *
 * Es "best effort": si Kafka no responde se registra una advertencia y el flujo de negocio sigue.
 */
@Slf4j
@Component
public class EventoUsuarioPublisher {

    public static final String TOPIC = "user-events";
    private static final String ORIGEN = "ms-producto";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public EventoUsuarioPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicar(String tipo, String usuarioId, String usuarioNombre,
                         Long productoId, String productoNombre, BigDecimal precio, Integer cantidad) {
        try {
            Map<String, Object> evento = new LinkedHashMap<>();
            evento.put("eventId", UUID.randomUUID().toString());
            evento.put("tipo", tipo);
            evento.put("usuarioId", usuarioId);
            evento.put("usuarioNombre", usuarioNombre);
            evento.put("productoId", productoId);
            evento.put("productoNombre", productoNombre);
            evento.put("precio", precio);
            evento.put("cantidad", cantidad);
            evento.put("origen", ORIGEN);
            evento.put("timestamp", System.currentTimeMillis());

            // La clave = usuario => todos los eventos de un usuario van a la misma partición (orden garantizado)
            String clave = usuarioId != null ? usuarioId : "anonimo";
            kafkaTemplate.send(TOPIC, clave, mapper.writeValueAsString(evento));
        } catch (Exception e) {
            log.warn("No se pudo publicar el evento {} en Kafka: {}", tipo, e.getMessage());
        }
    }
}
