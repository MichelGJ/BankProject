package com.example.bank.account.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ReporteCuentaDTO {
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private boolean estado;
    private List<ReporteMovimientoDTO> movimientos;
}