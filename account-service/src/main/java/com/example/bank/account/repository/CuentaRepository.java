package com.example.bank.account.repository;

import com.example.bank.account.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(Long numeroCuenta);
    List<Cuenta> findByClienteId(Long clienteId);
}