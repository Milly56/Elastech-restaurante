package org.restaurante.pedido;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.restaurante.cardapio.ItemCardapio;

public class GerenciadorPedidos {
    private final Map<Long, Pedido> pedidos = new HashMap<>();
    private long proximoNumero = 1;

    public Pedido criarPedido(String cliente) {
        long seguinte = Math.incrementExact(proximoNumero);
        Pedido pedido = new Pedido(proximoNumero, cliente);
        if (pedidos.putIfAbsent(proximoNumero, pedido) != null) {
            throw new IllegalStateException("Número de pedido já utilizado.");
        }
        proximoNumero = seguinte;
        return pedido;
    }

    public Optional<Pedido> buscarPorNumero(long numero) {
        return Optional.ofNullable(pedidos.get(numero));
    }

    public List<Pedido> listarTodos() { return List.copyOf(pedidos.values()); }

    public void cancelarPedido(long numero) {
        Pedido pedido = buscarPorNumero(numero).orElseThrow(
                () -> new IllegalArgumentException("Pedido não encontrado."));
        pedido.cancelar();
    }

    public double calcularTotalVendido() {
        double total = 0.00;
        for (Pedido pedido : pedidos.values()) {
            if (!pedido.isCancelado()) {
                total += pedido.calcularTotal();
            }
        }
        return total;
    }

    public Set<String> consultarCombinacoes() {
        Set<String> combinacoes = new HashSet<>();
        for (Pedido pedido : pedidos.values()) {
            if (!pedido.isCancelado()) {
                for (ItemCardapio item : pedido.consultarItens()) {
                    combinacoes.add(item.getDescricao());
                }
            }
        }
        return Set.copyOf(combinacoes);
    }
}
