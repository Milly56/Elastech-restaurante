package org.restaurante.cardapio;

import java.math.BigDecimal;
import java.util.List;

public class Lanche implements ItemCardapio {
    private String nome;
    private BigDecimal preco;
    private final List<String> ingredientes;

    public Lanche(String nome, BigDecimal preco) {
        this(nome, preco, List.of());
    }

    public Lanche(String nome, BigDecimal preco, List<String> ingredientes) {
        setNome(nome);
        setPreco(preco);
        this.ingredientes = List.copyOf(ingredientes);
    }

    public String getNome() { return nome; }
    @Override
    public String getDescricao() { return nome; }
    @Override
    public BigDecimal getPreco() { return preco; }
    public List<String> getIngredientes() { return ingredientes; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do lanche não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.signum() < 0) {
            throw new IllegalArgumentException("Preço deve ser informado e não pode ser negativo.");
        }
        this.preco = preco;
    }
}
