package org.restaurante.financeiro;

import org.junit.jupiter.api.Test;
import org.restaurante.cardapio.LancheFactory;
import org.restaurante.pedido.Pedido;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FinanceiroTest {
    @Test
    void transacoesIguaisPossuemMesmoHashIndependentementeDaEscala() {
        Transacao primeira = new Transacao(1, new BigDecimal("4.0"));
        Transacao segunda = new Transacao(1, new BigDecimal("4.00"));
        assertEquals(primeira, segunda);
        assertEquals(primeira.hashCode(), segunda.hashCode());
        assertNotEquals(primeira, new Transacao(2, new BigDecimal("4.00")));
        assertNotEquals(primeira, new Transacao(1, new BigDecimal("5.00")));
    }

    @Test
    void hashSetImpedeTransacoesDuplicadas() {
        HashSet<Transacao> transacoes = new HashSet<>();
        transacoes.add(new Transacao(1, new BigDecimal("4.0")));
        transacoes.add(new Transacao(1, new BigDecimal("4.00")));
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
        caixa.registrarGorjeta(primeiro, new BigDecimal("4.00"));
        caixa.registrarGorjeta(segundo, new BigDecimal("6.00"));
        List<Pedido> pedidos = List.of(primeiro, segundo);
        assertEquals(new BigDecimal("10.00"), caixa.calcularTotalGorjetas(pedidos));
        assertEquals(2, caixa.contarGorjetas(pedidos));
        segundo.cancelar();
        assertEquals(new BigDecimal("16.00"), primeiro.calcularTotal());
        assertEquals(new BigDecimal("4.00"), caixa.calcularTotalGorjetas(pedidos));
        assertEquals(1, caixa.contarGorjetas(pedidos));
        assertEquals(new BigDecimal("20.00"),
                caixa.calcularTotalComGorjetas(primeiro.calcularTotal(), pedidos));
    }

    @Test
    void zeroNaoContaComoGorjetaEDuplicataNaoAumentaTotal() {
        Caixa caixa = Caixa.getInstancia();
        Pedido pedido = new Pedido(10003, "Caio");
        List<Pedido> pedidos = List.of(pedido);
        caixa.registrarGorjeta(pedido, BigDecimal.ZERO);
        assertEquals(0, caixa.contarGorjetas(pedidos));
        caixa.registrarGorjeta(pedido, new BigDecimal("3.0"));
        caixa.registrarGorjeta(pedido, new BigDecimal("3.00"));
        assertEquals(1, caixa.contarGorjetas(pedidos));
        assertEquals(new BigDecimal("3.00"), caixa.calcularTotalGorjetas(pedidos));
    }

    @Test
    void rejeitaGorjetaNegativaNulaOuDePedidoCancelado() {
        Pedido pedido = new Pedido(10004, "Dani");
        Caixa caixa = Caixa.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> caixa.registrarGorjeta(pedido, new BigDecimal("-1.00")));
        assertThrows(IllegalArgumentException.class, () -> caixa.registrarGorjeta(pedido, null));
        pedido.cancelar();
        assertThrows(IllegalArgumentException.class,
                () -> caixa.registrarGorjeta(pedido, new BigDecimal("1.00")));
    }
}
