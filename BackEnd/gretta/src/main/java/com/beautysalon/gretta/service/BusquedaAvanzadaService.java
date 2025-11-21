package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.busqueda.BusquedaCriteria;
import com.beautysalon.gretta.dto.busqueda.ResultadoBusquedaResponse;
import com.beautysalon.gretta.dto.cita.CitaResponse;
import com.beautysalon.gretta.dto.producto.ProductoResponse;
import com.beautysalon.gretta.dto.usuario.UsuarioResponse;
import com.beautysalon.gretta.dto.venta.VentaResponse;
import com.beautysalon.gretta.entity.*;
import com.beautysalon.gretta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusquedaAvanzadaService {

    private final CitaRepository citaRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public ResultadoBusquedaResponse<CitaResponse> buscarCitas(BusquedaCriteria criteria) {
        List<Cita> citas = citaRepository.findAll();

        // Aplicar filtros
        if (criteria.getIdCliente() != null) {
            citas = citas.stream()
                    .filter(c -> c.getCliente().getIdCliente().equals(criteria.getIdCliente()))
                    .collect(Collectors.toList());
        }

        if (criteria.getIdEstilista() != null) {
            citas = citas.stream()
                    .filter(c -> c.getEstilista().getIdEstilista().equals(criteria.getIdEstilista()))
                    .collect(Collectors.toList());
        }

        if (criteria.getIdServicio() != null) {
            citas = citas.stream()
                    .filter(c -> c.getServicio().getIdServicio().equals(criteria.getIdServicio()))
                    .collect(Collectors.toList());
        }

        if (criteria.getEstadoCita() != null) {
            citas = citas.stream()
                    .filter(c -> c.getEstado() == criteria.getEstadoCita())
                    .collect(Collectors.toList());
        }

        if (criteria.getFechaInicio() != null && criteria.getFechaFin() != null) {
            citas = citas.stream()
                    .filter(c -> !c.getFechaCita().isBefore(criteria.getFechaInicio()) &&
                               !c.getFechaCita().isAfter(criteria.getFechaFin()))
                    .collect(Collectors.toList());
        }

        if (criteria.getPalabraClave() != null && !criteria.getPalabraClave().isEmpty()) {
            String keyword = criteria.getPalabraClave().toLowerCase();
            citas = citas.stream()
                    .filter(c -> c.getServicio().getNombre().toLowerCase().contains(keyword) ||
                               c.getCliente().getUsuario().getNombre().toLowerCase().contains(keyword) ||
                               c.getEstilista().getUsuario().getNombre().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        // Ordenar
        citas = ordenarCitas(citas, criteria.getOrdenarPor(), criteria.getDireccion());

        // Paginar
        return paginarResultados(
                citas.stream().map(this::convertirCitaAResponse).collect(Collectors.toList()),
                criteria.getPagina(),
                criteria.getTamanoPagina()
        );
    }

    @Transactional(readOnly = true)
    public ResultadoBusquedaResponse<VentaResponse> buscarVentas(BusquedaCriteria criteria) {
        List<Venta> ventas = ventaRepository.findAll();

        // Aplicar filtros
        if (criteria.getIdCliente() != null) {
            ventas = ventas.stream()
                    .filter(v -> v.getCliente().getIdCliente().equals(criteria.getIdCliente()))
                    .collect(Collectors.toList());
        }

        if (criteria.getFechaInicio() != null && criteria.getFechaFin() != null) {
            ventas = ventas.stream()
                    .filter(v -> !v.getFechaVenta().isBefore(criteria.getFechaInicio()) &&
                               !v.getFechaVenta().isAfter(criteria.getFechaFin()))
                    .collect(Collectors.toList());
        }

        if (criteria.getMontoMinimo() != null) {
            ventas = ventas.stream()
                    .filter(v -> v.getTotal().compareTo(criteria.getMontoMinimo()) >= 0)
                    .collect(Collectors.toList());
        }

        if (criteria.getMontoMaximo() != null) {
            ventas = ventas.stream()
                    .filter(v -> v.getTotal().compareTo(criteria.getMontoMaximo()) <= 0)
                    .collect(Collectors.toList());
        }

        if (criteria.getPalabraClave() != null && !criteria.getPalabraClave().isEmpty()) {
            String keyword = criteria.getPalabraClave().toLowerCase();
            ventas = ventas.stream()
                    .filter(v -> v.getCliente().getUsuario().getNombre().toLowerCase().contains(keyword) ||
                               v.getCliente().getUsuario().getApellido().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        // Ordenar
        ventas = ordenarVentas(ventas, criteria.getOrdenarPor(), criteria.getDireccion());

        // Paginar
        return paginarResultados(
                ventas.stream().map(this::convertirVentaAResponse).collect(Collectors.toList()),
                criteria.getPagina(),
                criteria.getTamanoPagina()
        );
    }

    @Transactional(readOnly = true)
    public ResultadoBusquedaResponse<ProductoResponse> buscarProductos(BusquedaCriteria criteria) {
        List<Producto> productos = productoRepository.findAll();

        // Aplicar filtros
        if (criteria.getPalabraClave() != null && !criteria.getPalabraClave().isEmpty()) {
            String keyword = criteria.getPalabraClave().toLowerCase();
            productos = productos.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(keyword) ||
                               (p.getDescripcion() != null && p.getDescripcion().toLowerCase().contains(keyword)))
                    .collect(Collectors.toList());
        }

        if (criteria.getPrecioMinimo() != null) {
            productos = productos.stream()
                    .filter(p -> p.getPrecio().compareTo(criteria.getPrecioMinimo()) >= 0)
                    .collect(Collectors.toList());
        }

        if (criteria.getPrecioMaximo() != null) {
            productos = productos.stream()
                    .filter(p -> p.getPrecio().compareTo(criteria.getPrecioMaximo()) <= 0)
                    .collect(Collectors.toList());
        }

        if (criteria.getStockMinimo() != null) {
            productos = productos.stream()
                    .filter(p -> p.getStock() >= criteria.getStockMinimo())
                    .collect(Collectors.toList());
        }

        if (criteria.getStockMaximo() != null) {
            productos = productos.stream()
                    .filter(p -> p.getStock() <= criteria.getStockMaximo())
                    .collect(Collectors.toList());
        }

        // Ordenar
        productos = ordenarProductos(productos, criteria.getOrdenarPor(), criteria.getDireccion());

        // Paginar
        return paginarResultados(
                productos.stream().map(this::convertirProductoAResponse).collect(Collectors.toList()),
                criteria.getPagina(),
                criteria.getTamanoPagina()
        );
    }

    @Transactional(readOnly = true)
    public ResultadoBusquedaResponse<UsuarioResponse> buscarUsuarios(BusquedaCriteria criteria) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Aplicar filtros
        if (criteria.getPalabraClave() != null && !criteria.getPalabraClave().isEmpty()) {
            String keyword = criteria.getPalabraClave().toLowerCase();
            usuarios = usuarios.stream()
                    .filter(u -> u.getNombre().toLowerCase().contains(keyword) ||
                               u.getApellido().toLowerCase().contains(keyword) ||
                               u.getCorreo().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        if (criteria.getActivo() != null) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getActivo().equals(criteria.getActivo()))
                    .collect(Collectors.toList());
        }

        if (criteria.getRol() != null) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getRol().name().equalsIgnoreCase(criteria.getRol()))
                    .collect(Collectors.toList());
        }

        // Ordenar
        usuarios = ordenarUsuarios(usuarios, criteria.getOrdenarPor(), criteria.getDireccion());

        // Paginar
        return paginarResultados(
                usuarios.stream().map(this::convertirUsuarioAResponse).collect(Collectors.toList()),
                criteria.getPagina(),
                criteria.getTamanoPagina()
        );
    }

    // Métodos de ordenamiento
    private List<Cita> ordenarCitas(List<Cita> citas, String ordenarPor, String direccion) {
        if (ordenarPor == null) ordenarPor = "fecha";
        if (direccion == null) direccion = "DESC";

        Comparator<Cita> comparator;
        switch (ordenarPor.toLowerCase()) {
            case "fecha":
                comparator = Comparator.comparing(Cita::getFechaCita);
                break;
            case "cliente":
                comparator = Comparator.comparing(c -> c.getCliente().getUsuario().getNombre());
                break;
            case "estilista":
                comparator = Comparator.comparing(c -> c.getEstilista().getUsuario().getNombre());
                break;
            default:
                comparator = Comparator.comparing(Cita::getFechaCita);
        }

        if ("DESC".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }

        return citas.stream().sorted(comparator).collect(Collectors.toList());
    }

    private List<Venta> ordenarVentas(List<Venta> ventas, String ordenarPor, String direccion) {
        if (ordenarPor == null) ordenarPor = "fecha";
        if (direccion == null) direccion = "DESC";

        Comparator<Venta> comparator;
        switch (ordenarPor.toLowerCase()) {
            case "fecha":
                comparator = Comparator.comparing(Venta::getFechaVenta);
                break;
            case "total":
                comparator = Comparator.comparing(Venta::getTotal);
                break;
            case "cliente":
                comparator = Comparator.comparing(v -> v.getCliente().getUsuario().getNombre());
                break;
            default:
                comparator = Comparator.comparing(Venta::getFechaVenta);
        }

        if ("DESC".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }

        return ventas.stream().sorted(comparator).collect(Collectors.toList());
    }

    private List<Producto> ordenarProductos(List<Producto> productos, String ordenarPor, String direccion) {
        if (ordenarPor == null) ordenarPor = "nombre";
        if (direccion == null) direccion = "ASC";

        Comparator<Producto> comparator;
        switch (ordenarPor.toLowerCase()) {
            case "nombre":
                comparator = Comparator.comparing(Producto::getNombre);
                break;
            case "precio":
                comparator = Comparator.comparing(Producto::getPrecio);
                break;
            case "stock":
                comparator = Comparator.comparing(Producto::getStock);
                break;
            default:
                comparator = Comparator.comparing(Producto::getNombre);
        }

        if ("DESC".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }

        return productos.stream().sorted(comparator).collect(Collectors.toList());
    }

    private List<Usuario> ordenarUsuarios(List<Usuario> usuarios, String ordenarPor, String direccion) {
        if (ordenarPor == null) ordenarPor = "nombre";
        if (direccion == null) direccion = "ASC";

        Comparator<Usuario> comparator;
        switch (ordenarPor.toLowerCase()) {
            case "nombre":
                comparator = Comparator.comparing(Usuario::getNombre);
                break;
            case "fecha":
                comparator = Comparator.comparing(Usuario::getFechaCreacion);
                break;
            default:
                comparator = Comparator.comparing(Usuario::getNombre);
        }

        if ("DESC".equalsIgnoreCase(direccion)) {
            comparator = comparator.reversed();
        }

        return usuarios.stream().sorted(comparator).collect(Collectors.toList());
    }

    // Método genérico de paginación
    private <T> ResultadoBusquedaResponse<T> paginarResultados(List<T> resultados, Integer pagina, Integer tamanoPagina) {
        if (pagina == null) pagina = 0;
        if (tamanoPagina == null) tamanoPagina = 20;

        int totalResultados = resultados.size();
        int totalPaginas = (int) Math.ceil((double) totalResultados / tamanoPagina);
        
        int inicio = pagina * tamanoPagina;
        int fin = Math.min(inicio + tamanoPagina, totalResultados);
        
        List<T> resultadosPaginados = resultados.subList(
                Math.min(inicio, totalResultados),
                Math.min(fin, totalResultados)
        );

        return ResultadoBusquedaResponse.<T>builder()
                .resultados(resultadosPaginados)
                .totalResultados((long) totalResultados)
                .paginaActual(pagina)
                .totalPaginas(totalPaginas)
                .tamanoPagina(tamanoPagina)
                .build();
    }

    // Métodos de conversión (simplificados)
    private CitaResponse convertirCitaAResponse(Cita cita) {
        return CitaResponse.builder()
                .idCita(cita.getIdCita())
                .idCliente(cita.getCliente().getIdCliente())
                .nombreCliente(cita.getCliente().getUsuario().getNombre())
                .idEstilista(cita.getEstilista().getIdEstilista())
                .nombreEstilista(cita.getEstilista().getUsuario().getNombre())
                .idServicio(cita.getServicio().getIdServicio())
                .nombreServicio(cita.getServicio().getNombre())
                .fechaCita(cita.getFechaCita())
                .horaCita(cita.getHoraCita().toString())
                .estado(cita.getEstado())
                .build();
    }

    private VentaResponse convertirVentaAResponse(Venta venta) {
        return VentaResponse.builder()
                .idVenta(venta.getIdVenta())
                .idCliente(venta.getCliente().getIdCliente())
                .nombreCliente(venta.getCliente().getUsuario().getNombre())
                .fechaVenta(venta.getFechaVenta())
                .total(venta.getTotal())
                .build();
    }

    private ProductoResponse convertirProductoAResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();
    }

    private UsuarioResponse convertirUsuarioAResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}
