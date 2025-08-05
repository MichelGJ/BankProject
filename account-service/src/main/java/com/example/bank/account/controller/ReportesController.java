package com.example.bank.account.controller;


import com.example.bank.account.service.MovimientoService;
import com.example.bank.account.dto.ReporteEstadoCuentaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private MovimientoService movimientoService;

    // The endpoint path is now simply /reportes
    @GetMapping
    public ReporteEstadoCuentaDTO getReporteEstadoCuenta(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin) {

        return movimientoService.generateReporteEstadoCuenta(clienteId, fechaInicio, fechaFin);
    }
}