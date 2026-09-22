package co.edu.pascualbravo.banco.service;

import co.edu.pascualbravo.banco.model.Prestamo;
import co.edu.pascualbravo.banco.repository.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PrestamoService {

    private static final String ESTADO_APROBADO = "APROBADO";
    private static final String ESTADO_RECHAZADO = "RECHAZADO";

    private static final String TIPO_PREMIUM = "PREMIUM";
    private static final String TIPO_REGULAR = "REGULAR";

    private static final double MONTO_MAXIMO_APROBADO = 50000;
    private static final double MONTO_DESCUENTO_ALTO = 10000;
    private static final double MONTO_DESCUENTO_MEDIO = 5000;

    private static final double TASA_PREMIUM = 3.5;
    private static final double TASA_REGULAR = 5.0;
    private static final double TASA_ESTANDAR = 6.0;

    private static final double TASA_DESCUENTO_ALTO = 0.05;
    private static final double TASA_DESCUENTO_MEDIO = 0.02;

    private final PrestamoRepository prestamoRepository;

    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    public Prestamo procesarSolicitud(Long clienteId, double monto, String tipoCliente) {
        validarSolicitud(clienteId, monto);

        Prestamo prestamo = construirPrestamo(clienteId, monto, tipoCliente);

        return prestamoRepository.save(prestamo);
    }

    private void validarSolicitud(Long clienteId, double monto) {
        if (clienteId == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
    }

    private Prestamo construirPrestamo(Long clienteId, double monto, String tipoCliente) {
        Prestamo prestamo = new Prestamo();
        prestamo.setClienteId(clienteId);
        prestamo.setMonto(monto);

        boolean aprobado = monto <= MONTO_MAXIMO_APROBADO;
        prestamo.setEstado(aprobado ? ESTADO_APROBADO : ESTADO_RECHAZADO);
        prestamo.setTasaInteres(aprobado ? resolverTasaInteres(tipoCliente) : 0.0);

        return prestamo;
    }

    private double resolverTasaInteres(String tipoCliente) {
        if (TIPO_PREMIUM.equals(tipoCliente)) {
            return TASA_PREMIUM;
        }
        if (TIPO_REGULAR.equals(tipoCliente)) {
            return TASA_REGULAR;
        }
        return TASA_ESTANDAR;
    }



    public double calcularDescuentoCartera(List<Prestamo> prestamos) {
        if (prestamos == null || prestamos.isEmpty()) {
            return 0.0;
        }

        return prestamos.stream()
                .filter(Objects::nonNull)
                .filter(this::esPrestamoAprobado)
                .mapToDouble(this::calcularDescuentoPorPrestamo)
                .sum();
    }

    private boolean esPrestamoAprobado(Prestamo prestamo) {
        return ESTADO_APROBADO.equals(prestamo.getEstado());
    }

    private double calcularDescuentoPorPrestamo(Prestamo prestamo) {
        if (prestamo.getMonto() > MONTO_DESCUENTO_ALTO) {
            return prestamo.getMonto() * TASA_DESCUENTO_ALTO;
        }
        if (prestamo.getMonto() > MONTO_DESCUENTO_MEDIO) {
            return prestamo.getMonto() * TASA_DESCUENTO_MEDIO;
        }
        return 0.0;
    }
}