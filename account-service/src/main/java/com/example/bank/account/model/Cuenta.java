package com.example.bank.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El numeroCuenta no puede estar vacío.")
    private Long numeroCuenta;
    @NotBlank(message = "El tipoCuenta no puede estar vacío.")
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private boolean estado;
    @NotNull(message = "El clienteId no puede estar vacío.")
    private Long clienteId;

    public Cuenta(long l, String number, String ahorros, BigDecimal bigDecimal, BigDecimal bigDecimal1, boolean b, long l1) {
    }
}