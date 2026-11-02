package org.restaurante.pedido;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public BigDecimal calcularTotalVendido() {
        BigDecimal total = new BigDecimal("0.00");
        for (Pedido pedido : pedidos.values()) {
            if (!pedido.isCancelado()) {
                total = total.add(pedido.calcularTotal());
            }
        }
        return total;
    }
}
