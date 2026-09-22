package co.edu.pascualbravo.banco.repository;

import co.edu.pascualbravo.banco.model.Prestamo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class PrestamoRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Test
    @DisplayName("Encuentra préstamos filtrando por estado APROBADO")
    void encuentraPrestamosPorEstado() {
        // Given
        Prestamo p1 = new Prestamo();
        p1.setClienteId(1L);
        p1.setMonto(10000);
        p1.setTasaInteres(5.0);
        p1.setEstado("APROBADO");
        testEntityManager.persist(p1);

        Prestamo p2 = new Prestamo();
        p2.setClienteId(2L);
        p2.setMonto(60000);
        p2.setTasaInteres(0.0);
        p2.setEstado("RECHAZADO");
        testEntityManager.persist(p2);

        Prestamo p3 = new Prestamo();
        p3.setClienteId(3L);
        p3.setMonto(20000);
        p3.setTasaInteres(3.5);
        p3.setEstado("APROBADO");
        testEntityManager.persist(p3);

        testEntityManager.flush();

        // When
        List<Prestamo> aprobados = prestamoRepository.findByEstado("APROBADO");

        // Then
        then(aprobados).hasSize(2);
        then(aprobados).allMatch(p -> "APROBADO".equals(p.getEstado()));
    }

    @Test
    @DisplayName("No encuentra préstamos si ninguno tiene el estado buscado")
    void noEncuentraPrestamosSiEstadoNoExiste() {
        // Given
        Prestamo p1 = new Prestamo();
        p1.setClienteId(1L);
        p1.setMonto(10000);
        p1.setTasaInteres(5.0);
        p1.setEstado("RECHAZADO");
        testEntityManager.persist(p1);
        testEntityManager.flush();

        // When
        List<Prestamo> aprobados = prestamoRepository.findByEstado("APROBADO");

        // Then
        then(aprobados).isEmpty();
    }
}