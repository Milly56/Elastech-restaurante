package org.restaurante.financeiro;

import java.util.Objects;

public final class Transacao {
    private final long numeroPedido;
    private final double valor;

    public Transacao(long numeroPedido, double valor) {
        if (numeroPedido <= 0) {
            throw new IllegalArgumentException("Número do pedido deve ser positivo.");
        }
        if (!Double.isFinite(valor) || valor < 0) {
            throw new IllegalArgumentException("Valor deve ser finito e não pode ser negativo.");
        }
        this.numeroPedido = numeroPedido;
        this.valor = valor == 0.0 ? 0.0 : valor;
    }

    public long getNumeroPedido() { return numeroPedido; }
    public double getValor() { return valor; }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) { return true; }
        if (!(objeto instanceof Transacao outra)) { return false; }
        return numeroPedido == outra.numeroPedido && Double.compare(valor, outra.valor) == 0;
    }

    @Override
    public int hashCode() { return Objects.hash(numeroPedido, valor); }
}
