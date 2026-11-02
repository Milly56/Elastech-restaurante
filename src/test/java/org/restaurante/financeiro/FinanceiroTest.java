package org.restaurante.financeiro;

import org.junit.jupiter.api.Test;
import org.restaurante.cardapio.LancheFactory;
import org.restaurante.pedido.Pedido;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FinanceiroTest {
    @Test
    void transacoesComMesmoPedidoEValorSaoIguais() {
        Transacao primeira = new Transacao(1, 4.0);
        Transacao segunda = new Transacao(1, 4.00);
        assertEquals(primeira, segunda);
        assertEquals(primeira.hashCode(), segunda.hashCode());
        assertNotEquals(primeira, new Transacao(2, 4.00));
        assertNotEquals(primeira, new Transacao(1, 5.00));
    }

    @Test
    void hashSetImpedeTransacoesDuplicadas() {
        HashSet<Transacao> transacoes = new HashSet<>();
        transacoes.add(new Transacao(1, 4.0));
        transacoes.add(new Transacao(1, 4.00));
        assertEquals(1, transacoes.size());
    }

    @Test
    void caixaSempreRetornaMesmaInstancia() {
        assertSame(Caixa.getInstancia(), Caixa.getInstancia());
    }

    @Test
    void cancelamentoExcluiGorjetaSemAlterarValorDoPedido() {
        Caixa caixa = Caixa.getInstancia();
        Pedido primeiro = new Pedido(10001, "Ana");
        Pedido segundo = new Pedido(10002, "Bia");
        primeiro.adicionarLanche(new LancheFactory().criarLanche(1));
        segundo.adicionarLanche(new LancheFactory().criarLanche(2));
        caixa.registrarGorjeta(primeiro, 4.00);
        caixa.registrarGorjeta(segundo, 6.00);
        List<Pedido> pedidos = List.of(primeiro, segundo);
        assertEquals(10.00, caixa.calcularTotalGorjetas(pedidos), 0.000001);
        assertEquals(2, caixa.contarGorjetas(pedidos));
        segundo.cancelar();
        assertEquals(16.00, primeiro.calcularTotal(), 0.000001);
        assertEquals(4.00, caixa.calcularTotalGorjetas(pedidos), 0.000001);
        assertEquals(1, caixa.contarGorjetas(pedidos));
        assertEquals(20.00,
                caixa.calcularTotalComGorjetas(primeiro.calcularTotal(), pedidos), 0.000001);
    }

    @Test
    void zeroNaoContaComoGorjetaEDuplicataNaoAumentaTotal() {
        Caixa caixa = Caixa.getInstancia();
        Pedido pedido = new Pedido(10003, "Caio");
        List<Pedido> pedidos = List.of(pedido);
        caixa.registrarGorjeta(pedido, 0.0);
        assertEquals(0, caixa.contarGorjetas(pedidos));
        caixa.registrarGorjeta(pedido, 3.0);
        caixa.registrarGorjeta(pedido, 3.00);
        assertEquals(1, caixa.contarGorjetas(pedidos));
        assertEquals(3.00, caixa.calcularTotalGorjetas(pedidos), 0.000001);
    }

    @Test
    void rejeitaGorjetaNegativaNaoFinitaOuDePedidoCancelado() {
        Pedido pedido = new Pedido(10004, "Dani");
        Caixa caixa = Caixa.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> caixa.registrarGorjeta(pedido, -1.00));
        assertThrows(IllegalArgumentException.class, () -> caixa.registrarGorjeta(pedido, Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> caixa.registrarGorjeta(pedido, Double.POSITIVE_INFINITY));
        pedido.cancelar();
        assertThrows(IllegalArgumentException.class,
                () -> caixa.registrarGorjeta(pedido, 1.00));
    }
}
