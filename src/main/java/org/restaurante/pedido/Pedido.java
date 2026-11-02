package org.restaurante.pedido;

import org.restaurante.cardapio.Lanche;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final long identificador;
    private final String cliente;
    private final List<Lanche> lanches = new ArrayList<>();

    public Pedido(long identificador, String cliente) {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }
        this.identificador = identificador;
        this.cliente = cliente.trim();
    }

    public void adicionarLanche(Lanche lanche) {
        if (lanche == null) {
            throw new IllegalArgumentException("Lanche não pode ser nulo.");
        }
        lanches.add(lanche);
    }

    public List<Lanche> consultarItens() { return List.copyOf(lanches); }
    public long getIdentificador() { return identificador; }
    public String getCliente() { return cliente; }

    public BigDecimal calcularTotal() {
        BigDecimal total = new BigDecimal("0.00");
        for (Lanche lanche : lanches) {
            total = total.add(lanche.getPreco());
        }
        return total;
    }
}
