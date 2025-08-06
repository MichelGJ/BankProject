package com.example.bank.account.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank.account.model.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    /**
     * Finds all movements for a given account within a specific date range.
     * The method parameters (LocalDateTime) must match the entity field type.
     */
    List<Movimiento> findByCuentaIdAndFechaBetween(
            Long cuentaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
}