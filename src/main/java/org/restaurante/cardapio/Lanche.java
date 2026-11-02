package org.restaurante.cardapio;

import java.math.BigDecimal;

public class Lanche {
    private String nome;
    private BigDecimal preco;

    public Lanche(String nome, BigDecimal preco) {
        setNome(nome);
        setPreco(preco);
    }

    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }

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
