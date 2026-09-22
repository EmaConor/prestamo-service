package co.edu.pascualbravo.banco.controller;

import co.edu.pascualbravo.banco.model.Prestamo;
import co.edu.pascualbravo.banco.service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Prestamo> solicitarPrestamo(
            @RequestParam Long clienteId,
            @RequestParam double monto,
            @RequestParam String tipoCliente) {

        Prestamo prestamo = prestamoService.procesarSolicitud(clienteId, monto, tipoCliente);
        return ResponseEntity.ok(prestamo);
    }

    @PostMapping("/descuentos")
    public ResponseEntity<Double> calcularDescuentos(@RequestBody List<Prestamo> cartera) {
        double totalDescuento = prestamoService.calcularDescuentoCartera(cartera);
        return ResponseEntity.ok(totalDescuento);
    }
}