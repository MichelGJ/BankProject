package com.example.bank.client.service;

import com.example.bank.client.exception.ResourceNotFoundException;
import com.example.bank.client.model.Cliente;
import com.example.bank.client.model.Persona;
import com.example.bank.client.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clientService;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        Persona persona = new Persona();
        persona.setId(1L);
        persona.setNombre("Jose Lema");
        persona.setIdentificacion("1234567890");

        cliente = new Cliente();
        cliente.setClienteid(1L);
        cliente.setContrasena("1234");
        cliente.setEstado(true);
        cliente.setPersona(persona);
    }

    @DisplayName("Test get client by ID - Success")
    @Test
    void givenClientId_whenGetClientById_thenReturnClientObject() {
        given(clienteRepository.findById(1L)).willReturn(Optional.of(cliente));

        Cliente foundCliente = clientService.getClienteById(1L);

        assertNotNull(foundCliente);
        assertEquals("Jose Lema", foundCliente.getPersona().getNombre());
    }

    @DisplayName("Test get client by ID - Not Found")
    @Test
    void givenInvalidClientId_whenGetClientById_thenThrowsException() {
        given(clienteRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClienteById(1L));
    }

    @DisplayName("Test delete client by ID - Success")
    @Test
    void givenClientId_whenDeleteClient_thenNothing() {
        given(clienteRepository.findById(1L)).willReturn(Optional.of(cliente));
        willDoNothing().given(clienteRepository).delete(cliente);

        clientService.deleteCliente(1L);

        verify(clienteRepository, times(1)).delete(cliente);
    }
}