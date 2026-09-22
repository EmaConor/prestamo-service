package co.edu.pascualbravo.banco.service;

import co.edu.pascualbravo.banco.model.Prestamo;
import co.edu.pascualbravo.banco.repository.PrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PrestamoServiceTest {

    @InjectMocks
    private PrestamoService prestamoService;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Test
    @DisplayName("Rechaza créditos superiores a $50.000")
    void rechazaCreditosSuperioresA50000() {
        // Given
        Long clienteId = 1L;
        double monto = 60000;
        String tipoCliente = "REGULAR";

        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Prestamo actual = prestamoService.procesarSolicitud(clienteId, monto, tipoCliente);

        // Then
        then(actual.getEstado()).isEqualTo("RECHAZADO");
        then(actual.getTasaInteres()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Aplica tasa preferencial del 3.5% a cliente PREMIUM")
    void aplicaTasaPreferencialClientePremium() {
        // Given
        Long clienteId = 1L;
        double monto = 20000;
        String tipoCliente = "PREMIUM";

        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Prestamo actual = prestamoService.procesarSolicitud(clienteId, monto, tipoCliente);

        // Then
        then(actual.getEstado()).isEqualTo("APROBADO");
        then(actual.getTasaInteres()).isEqualTo(3.5);
    }

    @Test
    @DisplayName("Aplica tasa del 5.0% a cliente REGULAR")
    void aplicaTasaClienteRegular() {
        // Given
        Long clienteId = 1L;
        double monto = 20000;
        String tipoCliente = "REGULAR";

        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Prestamo actual = prestamoService.procesarSolicitud(clienteId, monto, tipoCliente);

        // Then
        then(actual.getTasaInteres()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("Aplica tasa del 6.0% a cliente sin categoría conocida")
    void aplicaTasaClienteSinCategoriaConocida() {
        // Given
        Long clienteId = 1L;
        double monto = 20000;
        String tipoCliente = "OTRO";

        when(prestamoRepository.save(any(Prestamo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Prestamo actual = prestamoService.procesarSolicitud(clienteId, monto, tipoCliente);

        // Then
        then(actual.getTasaInteres()).isEqualTo(6.0);
    }

    @Test
    @DisplayName("Lanza excepción cuando el cliente es nulo")
    void lanzaExcepcionSiClienteEsNulo() {
        // Given
        Long clienteId = null;
        double monto = 10000;
        String tipoCliente = "REGULAR";

        // When / Then
        thenThrownBy(() -> prestamoService.procesarSolicitud(clienteId, monto, tipoCliente))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El cliente no puede ser nulo");
    }

    @Test
    @DisplayName("Lanza excepción cuando el monto es negativo")
    void lanzaExcepcionSiMontoEsNegativo() {
        // Given
        Long clienteId = 1L;
        double monto = -500;
        String tipoCliente = "REGULAR";

        // When / Then
        thenThrownBy(() -> prestamoService.procesarSolicitud(clienteId, monto, tipoCliente))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El monto debe ser mayor a cero");
    }

    @Test
    @DisplayName("Calcula el descuento de una cartera con préstamos aprobados y rechazados")
    void calculaDescuentoParaCarteraAprobada() {
        // Given
        Prestamo p1 = new Prestamo();
        p1.setEstado("APROBADO");
        p1.setMonto(15000); // > 10000 -> 5%

        Prestamo p2 = new Prestamo();
        p2.setEstado("APROBADO");
        p2.setMonto(6000); // > 5000 -> 2%

        Prestamo p3 = new Prestamo();
        p3.setEstado("RECHAZADO");
        p3.setMonto(20000); // no cuenta

        List<Prestamo> prestamos = Arrays.asList(p1, p2, p3);

        // When
        double actual = prestamoService.calcularDescuentoCartera(prestamos);

        // Then
        then(actual).isEqualTo(15000 * 0.05 + 6000 * 0.02);
    }

    @Test
    @DisplayName("Calcula descuento de cartera - lista vacía retorna 0")
    void calculaDescuentoListaVacia() {
        // Given
        List<Prestamo> prestamos = List.of();

        // When
        double actual = prestamoService.calcularDescuentoCartera(prestamos);

        // Then
        then(actual).isZero();
    }
}