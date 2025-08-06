package com.example.bank.account.controller;

import com.example.bank.account.dto.ReporteClienteDTO;
import com.example.bank.account.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private MovimientoService movimientoService;

    // The endpoint path is now simply /reportes
    @GetMapping
    public ReporteClienteDTO getReporteEstadoCuenta(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return movimientoService.generateReporteEstadoCuenta(clienteId, fechaInicio, fechaFin);
    }
}