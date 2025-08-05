package com.example.bank.account.service;

import com.example.bank.account.dto.*;
import com.example.bank.account.exception.InsufficientFundsException;
import com.example.bank.account.exception.ResourceNotFoundException;
import com.example.bank.account.model.Cuenta;
import com.example.bank.account.model.Movimiento;
import com.example.bank.account.repository.CuentaRepository;
import com.example.bank.account.repository.MovimientoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.web.client.RestTemplate;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${client-service.url}")
    private String clientServiceUrl;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Movimiento> getAllMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento getMovimientoById(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

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


    public ReporteEstadoCuentaDTO generateReporteEstadoCuenta(Long clienteId, Date fechaInicio, Date fechaFin) {

        ReporteEstadoCuentaDTO reporte = new ReporteEstadoCuentaDTO();

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        if (cuentas.isEmpty()) {
            throw new RuntimeException("Cliente con ID " + clienteId + " no tiene cuentas.");
        }

        Cuenta cuenta = cuentas.get(0);

        String clientServiceUrl = "http://client-service:8080/clientes/" + clienteId;
        ClienteDTO clienteDto = restTemplate.getForObject(clientServiceUrl, ClienteDTO.class);

        if (clienteDto == null) {
            throw new RuntimeException("No se pudo encontrar el cliente con ID " + clienteId);
        }

        reporte.setClienteNombre(clienteDto.getPersona().getNombre());
        reporte.setNumeroCuenta(cuenta.getNumeroCuenta());
        reporte.setTipoCuenta(cuenta.getTipoCuenta());
        reporte.setSaldoInicial(cuenta.getSaldoInicial().doubleValue());

        List<Movimiento> movimientos = movimientoRepository.findByCuentaClienteIdAndFechaBetween(clienteId, fechaInicio, fechaFin);
        reporte.setMovimientos(movimientos);

        BigDecimal saldoFinal = cuenta.getSaldoInicial();
        for (Movimiento movimiento : movimientos) {
            saldoFinal = saldoFinal.add(movimiento.getValor());
        }
        reporte.setSaldoFinal(saldoFinal.doubleValue());

        return reporte;
    }
}