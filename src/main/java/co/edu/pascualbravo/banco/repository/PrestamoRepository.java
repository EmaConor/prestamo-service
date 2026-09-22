package co.edu.pascualbravo.banco.repository;

import co.edu.pascualbravo.banco.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // Método a probar en la capa de persistencia (H2)
    List<Prestamo> findByEstado(String estado);
}