package com.example.bank.account.dto;

import java.util.List;

public class ReporteClienteDTO {
    private String clienteNombre;
    private List<ReporteEstadoCuentaDTO> cuentas;

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public List<ReporteEstadoCuentaDTO> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<ReporteEstadoCuentaDTO> cuentas) {
        this.cuentas = cuentas;
    }
}
