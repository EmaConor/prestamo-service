package co.edu.pascualbravo.banco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clienteId;
    private double monto;
    private double tasaInteres;
    private String estado;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public double getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(double tasaInteres) { this.tasaInteres = tasaInteres; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}