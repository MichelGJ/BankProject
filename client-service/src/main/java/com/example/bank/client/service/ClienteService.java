package com.example.bank.client.service;

import com.example.bank.client.exception.ResourceNotFoundException;
import com.example.bank.client.model.Cliente;
import com.example.bank.client.model.Persona;
import com.example.bank.client.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente createCliente(Cliente cliente) {
        // Hash the password here in a real-world scenario
        return clienteRepository.save(cliente);
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
    }

    public Cliente updateCliente(Long id, Cliente clienteDetails) {
        Cliente existingCliente = getClienteById(id);

        existingCliente.setContrasena(clienteDetails.getContrasena());
        existingCliente.setEstado(clienteDetails.isEstado());

        Persona existingPersona = existingCliente.getPersona();
        Persona updatedPersonaDetails = clienteDetails.getPersona();

        existingPersona.setNombre(updatedPersonaDetails.getNombre());
        existingPersona.setGenero(updatedPersonaDetails.getGenero());
        existingPersona.setEdad(updatedPersonaDetails.getEdad());
        existingPersona.setDireccion(updatedPersonaDetails.getDireccion());
        existingPersona.setTelefono(updatedPersonaDetails.getTelefono());

        return clienteRepository.save(existingCliente);
    }

    public void deleteCliente(Long id) {
        clienteRepository.delete(getClienteById(id));
    }
}