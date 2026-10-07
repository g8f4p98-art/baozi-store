package br.com.baozistore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private java.time.LocalDate clienteDesde;

    public Cliente() {
    }

    public Cliente(String nome, java.time.LocalDate clienteDesde) {
        this.nome = nome;
        this.clienteDesde = clienteDesde;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public java.time.LocalDate getClienteDesde() {
        return clienteDesde;
    }

    public void setClienteDesde(java.time.LocalDate clienteDesde) {
        this.clienteDesde = clienteDesde;
    }
}