package org.restaurante.financeiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.restaurante.pedido.Pedido;

public final class Caixa {
    private static final Caixa INSTANCIA = new Caixa();
    private final Set<Transacao> transacoes = new HashSet<>();

    private Caixa() { }

    public static Caixa getInstancia() { return INSTANCIA; }

    public void registrarGorjeta(Pedido pedido, double valor) {
        if (pedido == null || pedido.isCancelado()) {
            throw new IllegalArgumentException("Informe um pedido ativo.");
        }
        Transacao transacao = new Transacao(pedido.getIdentificador(), valor);
        if (transacao.getValor() > 0) {
            transacoes.add(transacao);
        }
    }

    private Set<Transacao> consultarGorjetasAtivas(Collection<Pedido> pedidos) {
        Set<Long> numerosAtivos = new HashSet<>();
        for (Pedido pedido : pedidos) {
            if (!pedido.isCancelado()) {
                numerosAtivos.add(pedido.getIdentificador());
            }
        }
        Set<Transacao> ativas = new HashSet<>();
        for (Transacao transacao : transacoes) {
            if (numerosAtivos.contains(transacao.getNumeroPedido())) {
                ativas.add(transacao);
            }
        }
        return ativas;
    }

    public int contarGorjetas(Collection<Pedido> pedidos) {
        return consultarGorjetasAtivas(pedidos).size();
    }

    public double calcularTotalGorjetas(Collection<Pedido> pedidos) {
        double total = 0.00;
        for (Transacao transacao : consultarGorjetasAtivas(pedidos)) {
            total += transacao.getValor();
        }
        return total;
    }

    public double calcularTotalComGorjetas(
            double totalVendido, Collection<Pedido> pedidos) {
        return totalVendido + calcularTotalGorjetas(pedidos);
    }
}
