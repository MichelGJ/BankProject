package com.example.bank.account.repository;

import java.util.Date;
import java.util.List;

import com.example.bank.account.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.account.model.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaClienteIdAndFechaBetween(Long clienteId, Date fechaInicio, Date fechaFin);

}