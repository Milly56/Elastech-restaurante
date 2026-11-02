package org.restaurante.pedido;

import org.junit.jupiter.api.Test;
import org.restaurante.cardapio.*;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    private final LancheFactory fabrica = new LancheFactory();

    @Test
    void removerIngredientesPreservaPrecoEOrdenaDescricao() {
        Lanche lanche = fabrica.criarLanche(3);
        assertTrue(lanche.isComQueijo());
        assertTrue(lanche.isComAlface());
        assertTrue(lanche.isComTomate());
        assertTrue(lanche.isComCebola());
        assertEquals("X-Bacon", lanche.getDescricao());
        lanche.setComCebola(false);
        lanche.setComTomate(false);
        lanche.setComAlface(false);
        lanche.setComQueijo(false);
        assertEquals("X-Bacon sem queijo sem alface sem tomate sem cebola", lanche.getDescricao());
        assertEquals(22.00, lanche.getPreco(), 0.000001);
        ItemCardapio completo = new ComBaconExtra(new ComCheddar(lanche));
        assertEquals("X-Bacon sem queijo sem alface sem tomate sem cebola + cheddar + bacon extra",
                completo.getDescricao());
        assertEquals(32.00, completo.getPreco(), 0.000001);
        lanche.setComQueijo(true);
        assertFalse(lanche.getDescricao().contains("sem queijo"));
    }

    @Test
    void personalizacaoNaoVazaEntreItensOuPedidos() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido primeiro = gerenciador.criarPedido("Ana");
        Pedido segundo = gerenciador.criarPedido("Bia");
        Lanche personalizado = fabrica.criarLanche(3);
        personalizado.setComQueijo(false);
        personalizado.setComCebola(false);
        primeiro.adicionarLanche(personalizado);
        primeiro.adicionarLanche(fabrica.criarLanche(3));
        segundo.adicionarLanche(fabrica.criarLanche(3));
        assertEquals("X-Bacon sem queijo sem cebola", primeiro.consultarItens().get(0).getDescricao());
        assertEquals("X-Bacon", primeiro.consultarItens().get(1).getDescricao());
        assertEquals("X-Bacon", segundo.consultarItens().get(0).getDescricao());
        assertNotSame(primeiro.consultarItens().get(0), primeiro.consultarItens().get(1));
        assertNotSame(primeiro.consultarItens().get(1), segundo.consultarItens().get(0));
    }

    @Test
    void cancelamentoExcluiTotalECombinacoesSemReutilizarNumero() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido cancelado = gerenciador.criarPedido("Ana");
        cancelado.adicionarLanche(fabrica.criarLanche(3));
        Pedido ativo = gerenciador.criarPedido("Bia");
        ativo.adicionarLanche(fabrica.criarLanche(1));
        gerenciador.cancelarPedido(cancelado.getIdentificador());
        assertTrue(cancelado.isCancelado());
        assertEquals(16.00, gerenciador.calcularTotalVendido(), 0.000001);
        assertEquals(Set.of("X-Burger"), gerenciador.consultarCombinacoes());
        assertSame(cancelado, gerenciador.buscarPorNumero(1).orElseThrow());
        assertEquals(3, gerenciador.criarPedido("Caio").getIdentificador());
        assertThrows(IllegalStateException.class, () -> cancelado.removerItem(0));
        assertThrows(IllegalStateException.class,
                () -> cancelado.adicionarLanche(fabrica.criarLanche(2)));
        assertThrows(IllegalArgumentException.class, () -> gerenciador.cancelarPedido(99));
    }

    @Test
    void remocaoRecalculaPedidoDiaECombinacoesAteFicarVazio() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido pedido = gerenciador.criarPedido("Ana");
        pedido.adicionarLanche(fabrica.criarLanche(1));
        pedido.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        assertEquals(48.00, pedido.calcularTotal(), 0.000001);
        pedido.removerItem(1);
        assertEquals(16.00, pedido.calcularTotal(), 0.000001);
        assertEquals(16.00, gerenciador.calcularTotalVendido(), 0.000001);
        assertEquals(Set.of("X-Burger"), gerenciador.consultarCombinacoes());
        pedido.removerItem(0);
        assertEquals(0.00, pedido.calcularTotal(), 0.000001);
        assertEquals(0.00, gerenciador.calcularTotalVendido(), 0.000001);
        assertTrue(gerenciador.consultarCombinacoes().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> pedido.removerItem(0));
        assertThrows(IllegalArgumentException.class, () -> pedido.removerItem(-1));
    }

    @Test
    void trocaRemoveAntigoEAcrescentaNovoSemAlterarOriginal() {
        Pedido pedido = new Pedido(1, "Ana");
        Lanche antigo = fabrica.criarLanche(1);
        pedido.adicionarLanche(antigo);
        pedido.removerItem(0);
        pedido.adicionarLanche(fabrica.criarLanche(2));
        assertEquals(1, pedido.consultarItens().size());
        assertNotSame(antigo, pedido.consultarItens().get(0));
        assertEquals("X-Burger", antigo.getDescricao());
        assertEquals(18.00, pedido.calcularTotal(), 0.000001);
    }

    @Test
    void combinacoesRepetidasContamUmaVezEOrdensDiferentesContamSeparadas() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido pedido = gerenciador.criarPedido("Ana");
        pedido.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        pedido.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        pedido.adicionarLanche(new ComCheddar(new ComBaconExtra(fabrica.criarLanche(3))));
        assertEquals(Set.of("X-Bacon + cheddar + bacon extra",
                "X-Bacon + bacon extra + cheddar"), gerenciador.consultarCombinacoes());
    }
}
