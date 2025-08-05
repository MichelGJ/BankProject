package com.example.bank.account.service;

import com.example.bank.account.dto.ReporteEstadoCuentaDTO;
import com.example.bank.account.exception.InsufficientFundsException;
import com.example.bank.account.exception.ResourceNotFoundException;
import com.example.bank.account.model.Cuenta;
import com.example.bank.account.model.Movimiento;
import com.example.bank.account.repository.CuentaRepository;
import com.example.bank.account.repository.MovimientoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Transactional
    public Movimiento createMovimiento(Movimiento movimiento) {
        Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + movimiento.getCuenta().getId()));

        BigDecimal newSaldo = cuenta.getSaldoInicial().add(movimiento.getValor());
        if (newSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Saldo no disponible");
        }
        cuenta.setSaldoInicial(newSaldo);

        movimiento.setFecha(LocalDateTime.now());
        movimiento.setSaldoDisponible(newSaldo);
        movimiento.setCuenta(cuenta);

        cuentaRepository.save(cuenta);
        return movimientoRepository.save(movimiento);
    }

    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    public Cuenta getCuentaById(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

    public Cuenta createCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public Cuenta updateCuenta(Long id, Cuenta cuentaDetails) {
        Cuenta cuenta = getCuentaById(id);
        cuenta.setNumeroCuenta(cuentaDetails.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaDetails.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDetails.getSaldoInicial());
        cuenta.setEstado(cuentaDetails.isEstado());
        cuenta.setClienteId(cuentaDetails.getClienteId());
        return cuentaRepository.save(cuenta);
    }

    public void deleteCuenta(Long id) {
        cuentaRepository.delete(getCuentaById(id));
    }

}