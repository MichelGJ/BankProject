package com.example.bank.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movimiento")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    @NotBlank(message = "El tipoMovimiento no puede estar vacío.")
    private String tipoMovimiento;
    @NotNull(message = "El valor no puede estar vacío.")
    private BigDecimal valor;
    private BigDecimal saldoDisponible;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    @NotNull(message = "La cuenta no puede estar vacío.")
    private Cuenta cuenta;
}