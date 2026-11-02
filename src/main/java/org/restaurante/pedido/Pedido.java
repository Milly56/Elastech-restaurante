package org.restaurante.pedido;

import org.restaurante.cardapio.ItemCardapio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final long identificador;
    private final String cliente;
    private final List<ItemCardapio> itens = new ArrayList<>();
    private boolean cancelado;

    public Pedido(long identificador, String cliente) {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }
        this.identificador = identificador;
        this.cliente = cliente.trim();
    }

    public void adicionarLanche(ItemCardapio item) {
        validarAtivo();
        if (item == null) {
            throw new IllegalArgumentException("Lanche não pode ser nulo.");
        }
        itens.add(item);
    }

    public void removerItem(int indice) {
        validarAtivo();
        if (indice < 0 || indice >= itens.size()) {
            throw new IllegalArgumentException("Item inexistente.");
        }
        itens.remove(indice);
    }

    public void cancelar() { cancelado = true; }
    public boolean isCancelado() { return cancelado; }

    private void validarAtivo() {
        if (cancelado) {
            throw new IllegalStateException("Pedido cancelado não pode ser modificado.");
        }
    }

    public List<ItemCardapio> consultarItens() { return List.copyOf(itens); }
    public long getIdentificador() { return identificador; }
    public String getCliente() { return cliente; }

    public BigDecimal calcularTotal() {
        BigDecimal total = new BigDecimal("0.00");
        for (ItemCardapio item : itens) {
            total = total.add(item.getPreco());
        }
        return total;
    }
}
