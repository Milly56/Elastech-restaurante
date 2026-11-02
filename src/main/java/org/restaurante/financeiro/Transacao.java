package org.restaurante.financeiro;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Transacao {
    private final long numeroPedido;
    private final BigDecimal valor;

    public Transacao(long numeroPedido, BigDecimal valor) {
        if (numeroPedido <= 0) {
            throw new IllegalArgumentException("Número do pedido deve ser positivo.");
        }
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("Valor deve ser informado e não pode ser negativo.");
        }
        this.numeroPedido = numeroPedido;
        this.valor = valor.setScale(2, RoundingMode.UNNECESSARY);
    }

    public long getNumeroPedido() { return numeroPedido; }
    public BigDecimal getValor() { return valor; }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) { return true; }
        if (!(objeto instanceof Transacao outra)) { return false; }
        return numeroPedido == outra.numeroPedido && valor.equals(outra.valor);
    }

    @Override
    public int hashCode() { return Objects.hash(numeroPedido, valor); }
}
