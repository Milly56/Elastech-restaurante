package org.restaurante.pedido;

import org.restaurante.cardapio.ItemCardapio;

import java.util.ArrayList;
import java.util.List;
import org.restaurante.util.Validacoes;

public class Pedido {
    private final long identificador;
    private final String cliente;
    private final List<ItemCardapio> itens = new ArrayList<>();
    private boolean cancelado;

    public Pedido(long identificador, String cliente) {
        this.identificador = identificador;
        this.cliente = Validacoes.textoNaoVazio(cliente, "Nome do cliente");
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

    public double calcularTotal() {
        double total = 0.00;
        for (ItemCardapio item : itens) {
            total += item.getPreco();
        }
        return total;
    }
}
