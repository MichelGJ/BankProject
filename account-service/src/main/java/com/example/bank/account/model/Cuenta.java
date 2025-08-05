package com.example.bank.account.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long numeroCuenta; // Corrected to Long
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private boolean estado;
    private Long clienteId;

}