package com.example.listadesupermercado.model;

import java.io.Serializable;

public class Produto implements Serializable {

    //identificador do prodito no banco de dados
    private  Long id;
    //nome do produto
    private String nome;
    //preço do produto
    private double preco;
    //status que indica se ainda há ou não o produto em casa
    private boolean status;
    //categoria do item (ex.: frios, carne, frutas e etc.)
    private String categoria;
    //tipo de unidade (ex.: Kg, L, g, ml)
    private String unidade;
    //quantidade do produto
    private double quantidade;
    //custo é dado pela multiplicação de preço x quantidade
    private double custo;

    /*
        GETTERs and SETTERs
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }
}
