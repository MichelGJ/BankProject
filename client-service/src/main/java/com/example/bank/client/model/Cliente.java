package com.example.bank.client.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteid;
    private String contrasena;
    private boolean estado;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    private Persona persona;
}