package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.chat.*;
import com.beautysalon.gretta.entity.ChatConversacion;
import com.beautysalon.gretta.entity.ChatMensaje;
import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.enums.TipoMensaje;
import com.beautysalon.gretta.repository.ChatConversacionRepository;
import com.beautysalon.gretta.repository.ChatMensajeRepository;
import com.beautysalon.gretta.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatConversacionRepository conversacionRepository;
    private final ChatMensajeRepository mensajeRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public ChatConversacionResponse iniciarConversacion(ChatConversacionRequest request) {
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getIdCliente()));

        ChatConversacion conversacion = new ChatConversacion();
        conversacion.setCliente(cliente);
        conversacion.setFechaInicio(LocalDateTime.now());

        conversacion = conversacionRepository.save(conversacion);

        // Mensaje de bienvenida automático del chatbot
        ChatMensaje mensajeBienvenida = new ChatMensaje();
        mensajeBienvenida.setConversacion(conversacion);
        mensajeBienvenida.setTipoMensaje(TipoMensaje.CHATBOT);
        mensajeBienvenida.setContenido("¡Hola " + cliente.getUsuario().getNombre() + 
                "! Bienvenido a Gretta. ¿En qué puedo ayudarte hoy?");
        mensajeBienvenida.setFechaMensaje(LocalDateTime.now());
        mensajeRepository.save(mensajeBienvenida);

        return convertirAConversacionResponse(conversacion);
    }

    @Transactional
    public ChatMensajeResponse enviarMensaje(ChatMensajeRequest request) {
        ChatConversacion conversacion = conversacionRepository.findById(request.getIdConversacion())
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada con ID: " + request.getIdConversacion()));

        ChatMensaje mensaje = new ChatMensaje();
        mensaje.setConversacion(conversacion);
        mensaje.setTipoMensaje(request.getTipoMensaje());
        mensaje.setContenido(request.getContenido());
        mensaje.setFechaMensaje(LocalDateTime.now());

        mensaje = mensajeRepository.save(mensaje);

        // Si es mensaje del cliente, generar respuesta automática del chatbot
        if (request.getTipoMensaje() == TipoMensaje.CLIENTE) {
            generarRespuestaChatbot(conversacion, request.getContenido());
        }

        return convertirAMensajeResponse(mensaje);
    }

    @Transactional(readOnly = true)
    public List<ChatConversacionResponse> obtenerConversacionesPorCliente(Integer idCliente) {
        return conversacionRepository.findByClienteOrderByFechaDesc(idCliente).stream()
                .map(this::convertirAConversacionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatConversacionResponse> obtenerTodasConversaciones() {
        return conversacionRepository.findAllOrderByFechaDesc().stream()
                .map(this::convertirAConversacionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConversacionDetalleResponse obtenerConversacionDetalle(Integer idConversacion) {
        ChatConversacion conversacion = conversacionRepository.findById(idConversacion)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada con ID: " + idConversacion));

        List<ChatMensajeResponse> mensajes = mensajeRepository.findByConversacionOrderByFecha(idConversacion).stream()
                .map(this::convertirAMensajeResponse)
                .collect(Collectors.toList());

        return ConversacionDetalleResponse.builder()
                .idConversacion(conversacion.getIdConversacion())
                .idCliente(conversacion.getCliente().getIdCliente())
                .nombreCliente(conversacion.getCliente().getUsuario().getNombre() + " " + 
                              conversacion.getCliente().getUsuario().getApellido())
                .fechaInicio(conversacion.getFechaInicio())
                .totalMensajes((long) mensajes.size())
                .mensajes(mensajes)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ChatMensajeResponse> obtenerMensajesPorConversacion(Integer idConversacion) {
        return mensajeRepository.findByConversacionOrderByFecha(idConversacion).stream()
                .map(this::convertirAMensajeResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarConversacion(Integer idConversacion) {
        if (!conversacionRepository.existsById(idConversacion)) {
            throw new RuntimeException("Conversación no encontrada con ID: " + idConversacion);
        }
        conversacionRepository.deleteById(idConversacion);
    }

    private void generarRespuestaChatbot(ChatConversacion conversacion, String mensajeCliente) {
        String respuesta = procesarMensajeCliente(mensajeCliente);

        ChatMensaje mensajeChatbot = new ChatMensaje();
        mensajeChatbot.setConversacion(conversacion);
        mensajeChatbot.setTipoMensaje(TipoMensaje.CHATBOT);
        mensajeChatbot.setContenido(respuesta);
        mensajeChatbot.setFechaMensaje(LocalDateTime.now());

        mensajeRepository.save(mensajeChatbot);
    }

    private String procesarMensajeCliente(String mensaje) {
        String mensajeLower = mensaje.toLowerCase();

        if (mensajeLower.contains("hola") || mensajeLower.contains("buenos") || mensajeLower.contains("buenas")) {
            return "¡Hola! ¿En qué puedo ayudarte? Puedo ayudarte con información sobre servicios, citas, promociones o productos.";
        } else if (mensajeLower.contains("servicio") || mensajeLower.contains("corte") || mensajeLower.contains("peinado")) {
            return "Ofrecemos varios servicios como cortes de cabello, peinados, tintes, tratamientos capilares y más. ¿Te gustaría agendar una cita?";
        } else if (mensajeLower.contains("cita") || mensajeLower.contains("agendar") || mensajeLower.contains("reservar")) {
            return "Para agendar una cita, puedes hacerlo a través de nuestra plataforma seleccionando el servicio, estilista y horario de tu preferencia. ¿Necesitas ayuda con algo específico?";
        } else if (mensajeLower.contains("promocion") || mensajeLower.contains("descuento") || mensajeLower.contains("oferta")) {
            return "Tenemos promociones vigentes en varios servicios. Consulta nuestra sección de promociones para ver las ofertas actuales. ¿Te interesa algún servicio en particular?";
        } else if (mensajeLower.contains("producto") || mensajeLower.contains("comprar") || mensajeLower.contains("shampoo")) {
            return "Contamos con una línea de productos profesionales para el cuidado del cabello. Puedes consultarlos en nuestra tienda. ¿Buscas algo específico?";
        } else if (mensajeLower.contains("precio") || mensajeLower.contains("costo") || mensajeLower.contains("cuanto")) {
            return "Los precios varían según el servicio. Puedes consultar nuestra lista de servicios con precios detallados. ¿Te gustaría información sobre algún servicio específico?";
        } else if (mensajeLower.contains("horario") || mensajeLower.contains("hora") || mensajeLower.contains("cuando")) {
            return "Nuestro horario de atención es de lunes a sábado. Puedes agendar tu cita en el horario que mejor te convenga. ¿Necesitas verificar disponibilidad?";
        } else if (mensajeLower.contains("gracias") || mensajeLower.contains("perfecto") || mensajeLower.contains("ok")) {
            return "¡De nada! Estoy aquí para ayudarte. Si necesitas algo más, no dudes en preguntar.";
        } else if (mensajeLower.contains("adios") || mensajeLower.contains("chao") || mensajeLower.contains("hasta")) {
            return "¡Hasta pronto! Que tengas un excelente día. ¡Esperamos verte pronto en Gretta!";
        } else {
            return "Entiendo. ¿Puedes darme más detalles sobre lo que necesitas? Estoy aquí para ayudarte con servicios, citas, promociones o productos.";
        }
    }

    private ChatConversacionResponse convertirAConversacionResponse(ChatConversacion conversacion) {
        Long totalMensajes = mensajeRepository.countByConversacion(conversacion.getIdConversacion());

        return ChatConversacionResponse.builder()
                .idConversacion(conversacion.getIdConversacion())
                .idCliente(conversacion.getCliente().getIdCliente())
                .nombreCliente(conversacion.getCliente().getUsuario().getNombre() + " " + 
                              conversacion.getCliente().getUsuario().getApellido())
                .fechaInicio(conversacion.getFechaInicio())
                .totalMensajes(totalMensajes)
                .build();
    }

    private ChatMensajeResponse convertirAMensajeResponse(ChatMensaje mensaje) {
        return ChatMensajeResponse.builder()
                .idMensaje(mensaje.getIdMensaje())
                .idConversacion(mensaje.getConversacion().getIdConversacion())
                .tipoMensaje(mensaje.getTipoMensaje())
                .contenido(mensaje.getContenido())
                .fechaMensaje(mensaje.getFechaMensaje())
                .build();
    }
}
