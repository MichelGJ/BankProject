package com.example.bank.account.controller;

import com.example.bank.account.model.Cuenta;
import com.example.bank.account.service.CuentaService; // Changed
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService; // Changed

    @GetMapping
    public List<Cuenta> getAllCuentas() {
        return cuentaService.getAllCuentas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.getCuentaById(id);
        return ResponseEntity.ok(cuenta);
    }

    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody Cuenta cuenta) {
        return new ResponseEntity<>(cuentaService.createCuenta(cuenta), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> updateCuenta(@PathVariable Long id, @RequestBody Cuenta cuentaDetails) {
        return ResponseEntity.ok(cuentaService.updateCuenta(id, cuentaDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }
}