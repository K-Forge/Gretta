package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.venta.DetalleVentaDTO;
import com.beautysalon.gretta.dto.venta.DetalleVentaResponse;
import com.beautysalon.gretta.dto.venta.VentaRequest;
import com.beautysalon.gretta.dto.venta.VentaResponse;
import com.beautysalon.gretta.entity.*;
import com.beautysalon.gretta.repository.*;
import com.beautysalon.gretta.service.validation.VentaValidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaValidacionService validacionService;

    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerTodas() {
        return ventaRepository.findAllOrderByFechaDesc().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerPorCliente(Integer idCliente) {
        return ventaRepository.findByCliente_IdCliente(idCliente).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaResponse obtenerPorId(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        return convertirAResponse(venta);
    }

    @Transactional(readOnly = true)
    public Double obtenerTotalVentasPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = ventaRepository.getTotalVentasPorPeriodo(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }

    @Transactional
    public VentaResponse crear(VentaRequest request) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getIdCliente()));

        // Validar que el cliente esté activo
        validacionService.validarClienteActivo(cliente);

        // Crear venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setTotal(BigDecimal.ZERO);

        // Procesar cada detalle
        for (DetalleVentaDTO detalleDTO : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleDTO.getIdProducto()));

            // Validar stock disponible
            validacionService.validarStockDisponible(producto, detalleDTO.getCantidad());

            // Crear detalle de venta
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.calcularSubtotal();

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);

            // Agregar detalle a la venta
            venta.agregarDetalle(detalle);
        }

        // Calcular total de la venta
        venta.calcularTotal();

        // Validar límites de compra
        validacionService.validarLimitesVenta(venta);

        // Guardar venta con sus detalles
        venta = ventaRepository.save(venta);
        return convertirAResponse(venta);
    }

    @Transactional
    public void anular(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        // Validar que se puede anular (no más de 30 días)
        validacionService.validarAnulacionVenta(id);

        // Devolver stock de los productos
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        // Eliminar venta
        ventaRepository.delete(venta);
    }

    @Transactional(readOnly = true)
    public List<Object[]> obtenerProductosMasVendidos() {
        return detalleVentaRepository.findProductosMasVendidos();
    }

    private VentaResponse convertirAResponse(Venta venta) {
        List<DetalleVentaResponse> detallesResponse = venta.getDetalles().stream()
                .map(this::convertirDetalleAResponse)
                .collect(Collectors.toList());

        return VentaResponse.builder()
                .idVenta(venta.getIdVenta())
                .idCliente(venta.getCliente().getIdCliente())
                .nombreCliente(venta.getCliente().getUsuario().getNombre() + " " + 
                              venta.getCliente().getUsuario().getApellido())
                .fechaVenta(venta.getFechaVenta())
                .total(venta.getTotal())
                .detalles(detallesResponse)
                .build();
    }

    private DetalleVentaResponse convertirDetalleAResponse(DetalleVenta detalle) {
        return DetalleVentaResponse.builder()
                .idDetalle(detalle.getIdDetalle())
                .idProducto(detalle.getProducto().getIdProducto())
                .nombreProducto(detalle.getProducto().getNombre())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(detalle.getSubtotal())
                .build();
    }
}
