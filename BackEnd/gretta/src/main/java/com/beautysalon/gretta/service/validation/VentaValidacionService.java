package com.beautysalon.gretta.service.validation;

import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.DetalleVenta;
import com.beautysalon.gretta.entity.Producto;
import com.beautysalon.gretta.entity.Venta;
import com.beautysalon.gretta.repository.ClienteRepository;
import com.beautysalon.gretta.repository.ProductoRepository;
import com.beautysalon.gretta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaValidacionService {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    public void validarNuevaVenta(Integer idCliente, List<DetalleVenta> detalles) {
        // Validar cliente existe y está activo
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getUsuario().getActivo()) {
            throw new RuntimeException("El cliente no está activo");
        }

        // Validar que hay productos en la venta
        if (detalles == null || detalles.isEmpty()) {
            throw new RuntimeException("La venta debe contener al menos un producto");
        }

        // Validar cada detalle
        for (DetalleVenta detalle : detalles) {
            Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + 
                            detalle.getProducto().getIdProducto()));

            // Validar stock suficiente
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException(String.format(
                        "Stock insuficiente para el producto '%s'. Disponible: %d, Solicitado: %d",
                        producto.getNombre(),
                        producto.getStock(),
                        detalle.getCantidad()
                ));
            }

            // Validar cantidad positiva
            if (detalle.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }

            // Validar precio unitario correcto
            if (detalle.getPrecioUnitario().compareTo(producto.getPrecio()) != 0) {
                throw new RuntimeException(String.format(
                        "El precio unitario del producto '%s' no coincide con el precio actual",
                        producto.getNombre()
                ));
            }

            // Validar subtotal correcto
            BigDecimal subtotalCalculado = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            
            if (detalle.getSubtotal().compareTo(subtotalCalculado) != 0) {
                throw new RuntimeException(String.format(
                        "El subtotal del producto '%s' es incorrecto",
                        producto.getNombre()
                ));
            }
        }
    }

    public void validarTotalVenta(BigDecimal total, List<DetalleVenta> detalles) {
        BigDecimal totalCalculado = detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(totalCalculado) != 0) {
            throw new RuntimeException(String.format(
                    "El total de la venta no coincide. Esperado: %s, Recibido: %s",
                    totalCalculado,
                    total
            ));
        }

        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El total de la venta debe ser mayor a 0");
        }
    }

    public void validarAnulacionVenta(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // No se puede anular una venta muy antigua (más de 30 días)
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(30);
        if (venta.getFechaVenta().isBefore(fechaLimite)) {
            throw new RuntimeException("No se puede anular una venta con más de 30 días de antigüedad");
        }

        // Verificar que no esté ya anulada (esto requeriría un campo adicional en la entidad)
        // Por ahora solo validamos que existe
    }

    public void validarLimiteComprasDiarias(Integer idCliente, Integer idVentaExcluir) {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        List<Venta> ventasHoy = ventaRepository.findByCliente_IdClienteAndFechaVentaBetween(
                idCliente, inicioDia, finDia);

        long cantidadVentas = ventasHoy.stream()
                .filter(v -> idVentaExcluir == null || !v.getIdVenta().equals(idVentaExcluir))
                .count();

        if (cantidadVentas >= 10) {
            throw new RuntimeException("El cliente ha alcanzado el límite de compras diarias (10)");
        }
    }

    public void validarMontoMaximoVenta(BigDecimal total) {
        BigDecimal MONTO_MAXIMO = new BigDecimal("10000.00");
        
        if (total.compareTo(MONTO_MAXIMO) > 0) {
            throw new RuntimeException(String.format(
                    "El monto de la venta excede el límite máximo permitido de $%s",
                    MONTO_MAXIMO
            ));
        }
    }

    public void validarClienteActivo(Cliente cliente) {
        if (cliente == null) {
            throw new RuntimeException("Cliente no puede ser null");
        }
        
        if (!cliente.getUsuario().getActivo()) {
            throw new RuntimeException("El cliente no está activo");
        }
    }

    public void validarStockDisponible(Producto producto, Integer cantidad) {
        if (producto == null) {
            throw new RuntimeException("Producto no puede ser null");
        }
        
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
        
        if (producto.getStock() < cantidad) {
            throw new RuntimeException(String.format(
                    "Stock insuficiente para el producto '%s'. Disponible: %d, Solicitado: %d",
                    producto.getNombre(),
                    producto.getStock(),
                    cantidad
            ));
        }
    }

    public void validarLimitesVenta(Venta venta) {
        if (venta == null) {
            throw new RuntimeException("Venta no puede ser null");
        }
        
        // Validar límite de compras diarias
        validarLimiteComprasDiarias(venta.getCliente().getIdCliente(), venta.getIdVenta());
        
        // Validar monto máximo
        validarMontoMaximoVenta(venta.getTotal());
    }
}
