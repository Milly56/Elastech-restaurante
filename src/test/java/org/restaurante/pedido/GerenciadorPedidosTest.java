package org.restaurante.pedido;

import org.junit.jupiter.api.Test;
import org.restaurante.cardapio.*;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class GerenciadorPedidosTest {
    @Test
    void preservaPedidosNumeracaoEBuscaAusente() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido primeiro = gerenciador.criarPedido("Ana");
        assertThrows(IllegalArgumentException.class, () -> gerenciador.criarPedido("   "));
        Pedido segundo = gerenciador.criarPedido("Bia");
        assertEquals(1, primeiro.getIdentificador());
        assertEquals(2, segundo.getIdentificador());
        assertSame(primeiro, gerenciador.buscarPorNumero(1).orElseThrow());
        assertSame(segundo, gerenciador.buscarPorNumero(2).orElseThrow());
        assertTrue(gerenciador.buscarPorNumero(99).isEmpty());
        assertEquals(2, gerenciador.listarTodos().size());
    }

    @Test
    void somaItensDecoradosEPedidosDoDia() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        LancheFactory fabrica = new LancheFactory();
        assertEquals(new BigDecimal("0.00"), gerenciador.calcularTotalVendido());
        Pedido primeiro = gerenciador.criarPedido("Ana");
        primeiro.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        primeiro.adicionarLanche(fabrica.criarLanche(1));
        Pedido segundo = gerenciador.criarPedido("Bia");
        segundo.adicionarLanche(fabrica.criarLanche(2));
        assertEquals(new BigDecimal("48.00"), primeiro.calcularTotal());
        assertEquals(new BigDecimal("66.00"), gerenciador.calcularTotalVendido());
    }
}
