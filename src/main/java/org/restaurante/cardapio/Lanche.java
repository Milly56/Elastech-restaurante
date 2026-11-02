package org.restaurante.cardapio;

import java.math.BigDecimal;
import java.util.List;

public class Lanche implements ItemCardapio {
    private String nome;
    private BigDecimal preco;
    private final List<String> ingredientes;
    private boolean comQueijo = true;
    private boolean comAlface = true;
    private boolean comTomate = true;
    private boolean comCebola = true;

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
    public String getDescricao() {
        String descricao = nome;
        if (!comQueijo) { descricao += " sem queijo"; }
        if (!comAlface) { descricao += " sem alface"; }
        if (!comTomate) { descricao += " sem tomate"; }
        if (!comCebola) { descricao += " sem cebola"; }
        return descricao;
    }
    @Override
    public BigDecimal getPreco() { return preco; }
    public List<String> getIngredientes() { return ingredientes; }
    public boolean isComQueijo() { return comQueijo; }
    public void setComQueijo(boolean comQueijo) { this.comQueijo = comQueijo; }
    public boolean isComAlface() { return comAlface; }
    public void setComAlface(boolean comAlface) { this.comAlface = comAlface; }
    public boolean isComTomate() { return comTomate; }
    public void setComTomate(boolean comTomate) { this.comTomate = comTomate; }
    public boolean isComCebola() { return comCebola; }
    public void setComCebola(boolean comCebola) { this.comCebola = comCebola; }

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
