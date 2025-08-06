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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

        // Use saldoActual for the calculation
        BigDecimal newSaldoActual = cuenta.getSaldoActual().add(movimiento.getValor());
        if (newSaldoActual.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Saldo no disponible");
        }

        // Update the new saldo actual
        cuenta.setSaldoActual(newSaldoActual);

        movimiento.setFecha(LocalDateTime.now());
        movimiento.setSaldoDisponible(newSaldoActual); // saldoDisponible should reflect the new actual balance
        movimiento.setCuenta(cuenta);

        cuentaRepository.save(cuenta);
        return movimientoRepository.save(movimiento);
    }


    public ReporteClienteDTO generateReporteEstadoCuenta(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {

        ReporteClienteDTO reporteCliente = new ReporteClienteDTO();

        String clientServiceUrl = "http://client-service:8080/clientes/" + clienteId;
        ClienteDTO clienteDto = restTemplate.getForObject(clientServiceUrl, ClienteDTO.class);
        if (clienteDto != null) {
            reporteCliente.setClienteNombre(clienteDto.getPersona().getNombre());
        } else {
            throw new RuntimeException("No se pudo encontrar el cliente con ID " + clienteId);
        }

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        List<ReporteEstadoCuentaDTO> reportesCuentas = new ArrayList<>();
        LocalDateTime inicioDelDia = fechaInicio.atStartOfDay();
        LocalDateTime finDelDia = fechaFin.atTime(LocalTime.MAX);

        for (Cuenta cuenta : cuentas) {
            ReporteEstadoCuentaDTO reporteCuenta = new ReporteEstadoCuentaDTO();

            reporteCuenta.setNumeroCuenta(cuenta.getNumeroCuenta());
            reporteCuenta.setTipoCuenta(cuenta.getTipoCuenta());

            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                    cuenta.getId(),
                    inicioDelDia,
                    finDelDia
            );
            reporteCuenta.setMovimientos(movimientos);

            BigDecimal totalMovimientosEnPeriodo = movimientos.stream()
                    .map(Movimiento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal saldoInicialReporte = cuenta.getSaldoActual().subtract(totalMovimientosEnPeriodo);
            reporteCuenta.setSaldoInicial(saldoInicialReporte.doubleValue());

            BigDecimal saldoFinalReporte = cuenta.getSaldoActual();
            reporteCuenta.setSaldoFinal(saldoFinalReporte.doubleValue());

            reportesCuentas.add(reporteCuenta);
        }

        reporteCliente.setCuentas(reportesCuentas);

        return reporteCliente;
    }
}