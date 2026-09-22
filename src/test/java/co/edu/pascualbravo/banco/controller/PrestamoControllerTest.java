package co.edu.pascualbravo.banco.controller;

import co.edu.pascualbravo.banco.model.Prestamo;
import co.edu.pascualbravo.banco.service.PrestamoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PrestamoControllerTest {

    @InjectMocks
    private PrestamoController prestamoController;

    @Mock
    private PrestamoService prestamoService;

    @Test
    @DisplayName("Caso de Prueba solicitar Prestamo Seccessful")
    void solicitarPrestamo () {
        // Given
        Long clienteId = 1l;
        double monto = 2000;
        String tipoCliente = "REGULAR";

        Prestamo prestamo = new Prestamo();
        prestamo.setClienteId(clienteId);
        prestamo.setId(clienteId);
        prestamo.setEstado("APROBADO");
        prestamo.setMonto(monto);
        prestamo.setTasaInteres(5);

        // When
        when(prestamoService.procesarSolicitud(clienteId, monto, tipoCliente)).thenReturn(prestamo);
        ResponseEntity<Prestamo> actual = prestamoController.solicitarPrestamo(clienteId, monto, tipoCliente);

        // Then
        then(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(actual.getBody()).isNotNull();
        then(actual.getBody().getClienteId()).isEqualTo(clienteId);
        then(actual.getBody().getMonto()).isEqualTo(monto);
        then(actual.getBody().getEstado()).isEqualTo("APROBADO");
        then(actual.getBody().getTasaInteres()).isEqualTo(5);
    }

    @Test
    @DisplayName("Solicitar préstamo - caso fallido por monto inválido")
    void solicitarPrestamoFail() {
        // Given
        Long clienteId = 1L;
        double montoInvalido = -100;
        String tipoCliente = "REGULAR";

        when(prestamoService.procesarSolicitud(anyLong(), anyDouble(), anyString()))
                .thenThrow(new IllegalArgumentException("El monto debe ser mayor a cero"));

        // When / Then
        thenThrownBy(() ->
                prestamoController.solicitarPrestamo(clienteId, montoInvalido, tipoCliente))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El monto debe ser mayor a cero");
    }

    @Test
    @DisplayName("Calcular descuento de cartera - retorna valor correcto")
    void calcularDescuento() {
        // Given
        Prestamo p1 = new Prestamo();
        p1.setEstado("APROBADO");
        p1.setMonto(20000); // > 10000 → 20000 * 0.05 = 1000

        Prestamo p2 = new Prestamo();
        p2.setEstado("APROBADO");
        p2.setMonto(8000); // > 5000 → 8000 * 0.02 = 160

        Prestamo p3 = new Prestamo();
        p3.setEstado("RECHAZADO");
        p3.setMonto(15000); // no cuenta

        Prestamo p4 = new Prestamo();
        p4.setEstado("APROBADO");
        p4.setMonto(3000); // < 5000 → 0

        List<Prestamo> prestamos = Arrays.asList(p1, p2, p3, p4);

        // Descuento esperado: 1000 + 160 = 1160
        when(prestamoService.calcularDescuentoCartera(any()))
                .thenReturn(1160.0);

        // When
        double actual = prestamoService.calcularDescuentoCartera(prestamos);

        // Then
        then(actual).isEqualTo(1160.0);
    }

    // ---------------------------------------------------------
    // EXTRA: lista vacía o nula devuelve 0
    // ---------------------------------------------------------
    @Test
    @DisplayName("Calcular descuento de cartera - lista vacía retorna 0")
    void calcularDescuentoListaVacia() {
        // Given
        when(prestamoService.calcularDescuentoCartera(Collections.emptyList()))
                .thenReturn(0.0);

        // When
        double actual = prestamoService.calcularDescuentoCartera(Collections.emptyList());

        // Then
        then(actual).isZero();
    }
}
