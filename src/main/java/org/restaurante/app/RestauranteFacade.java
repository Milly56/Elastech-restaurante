package org.restaurante.app;

import org.restaurante.cardapio.LancheFactory;
import org.restaurante.cardapio.Lanche;
import org.restaurante.cardapio.ItemCardapio;
import org.restaurante.cardapio.ComCheddar;
import org.restaurante.cardapio.ComBaconExtra;
import org.restaurante.pedido.GerenciadorPedidos;
import org.restaurante.pedido.Pedido;
import java.util.Optional;
import java.util.List;
import org.restaurante.financeiro.Caixa;

public class RestauranteFacade {
    private final MenuConsole menu;
    private final GerenciadorPedidos gerenciador;
    private final LancheFactory fabrica = new LancheFactory();
    private final Caixa caixa = Caixa.getInstancia();

    public RestauranteFacade(MenuConsole menu, GerenciadorPedidos gerenciador) {
        this.menu = menu;
        this.gerenciador = gerenciador;
    }

    public boolean executarProximaOperacao() {
        try {
            switch (menu.lerOpcaoPrincipal()) {
                case 1 -> registrarPedido();
                case 2 -> menu.exibirPedidos(gerenciador.listarTodos());
                case 3 -> modificarPedido();
                case 4 -> cancelarPedido();
                case 5 -> fecharRestaurante();
                case 6 -> {
                    menu.exibirMensagem("Até logo!");
                    return false;
                }
                default -> throw new IllegalStateException("Opção não validada.");
            }
            return true;
        } catch (MenuConsole.EntradaEncerradaException e) {
            menu.exibirMensagem("Entrada encerrada. Até logo!");
            return false;
        }
    }

    private void registrarPedido() {
        Pedido pedido = gerenciador.criarPedido(menu.lerNomeCliente());
        do {
            pedido.adicionarLanche(escolherItem());
        } while (menu.desejaAdicionarOutroItem());
        caixa.registrarGorjeta(pedido, menu.lerGorjeta());
        menu.exibirMensagem("Pedido registrado com sucesso.");
    }

    private void fecharRestaurante() {
        List<Pedido> pedidos = gerenciador.listarTodos();
        double totalVendido = gerenciador.calcularTotalVendido();
        menu.exibirFechamento(totalVendido, gerenciador.consultarCombinacoes());
        menu.exibirRelatorioGorjetas(
                caixa.contarGorjetas(pedidos),
                caixa.calcularTotalGorjetas(pedidos),
                caixa.calcularTotalComGorjetas(totalVendido, pedidos));
    }

    private ItemCardapio escolherItem() {
        Lanche lanche = fabrica.criarLanche(menu.lerOpcaoLanche());
        lanche.setComQueijo(!menu.desejaRetirarIngrediente("queijo"));
        lanche.setComAlface(!menu.desejaRetirarIngrediente("alface"));
        lanche.setComTomate(!menu.desejaRetirarIngrediente("tomate"));
        lanche.setComCebola(!menu.desejaRetirarIngrediente("cebola"));
        return escolherAdicionais(lanche);
    }

    private Optional<Pedido> localizarPedidoAtivo() {
        Optional<Pedido> resultado = gerenciador.buscarPorNumero(menu.lerNumeroPedido());
        if (resultado.isEmpty()) {
            menu.exibirMensagem("Pedido não encontrado.");
            return Optional.empty();
        }
        if (resultado.get().isCancelado()) {
            menu.exibirMensagem("Pedido já cancelado.");
            return Optional.empty();
        }
        return resultado;
    }

    private void modificarPedido() {
        Optional<Pedido> resultado = localizarPedidoAtivo();
        if (resultado.isEmpty()) { return; }
        Pedido pedido = resultado.get();
        switch (menu.lerOpcaoModificacao()) {
            case 1 -> pedido.adicionarLanche(escolherItem());
            case 2 -> {
                if (!removerItem(pedido)) { return; }
            }
            case 3 -> {
                if (!removerItem(pedido)) { return; }
                pedido.adicionarLanche(escolherItem());
            }
            case 4 -> { return; }
            default -> throw new IllegalStateException("Modificação não validada.");
        }
        menu.exibirMensagem("Pedido atualizado com sucesso.");
    }

    private boolean removerItem(Pedido pedido) {
        int indice = menu.lerIndiceItem(pedido);
        if (indice < 0) { return false; }
        pedido.removerItem(indice);
        return true;
    }

    private void cancelarPedido() {
        Optional<Pedido> resultado = localizarPedidoAtivo();
        if (resultado.isEmpty()) { return; }
        gerenciador.cancelarPedido(resultado.get().getIdentificador());
        menu.exibirMensagem("Pedido cancelado com sucesso.");
    }

    private ItemCardapio escolherAdicionais(ItemCardapio item) {
        while (true) {
            switch (menu.lerOpcaoAdicional()) {
                case 1 -> item = new ComCheddar(item);
                case 2 -> item = new ComBaconExtra(item);
                case 3 -> { return item; }
                default -> throw new IllegalStateException("Adicional não validado.");
            }
        }
    }
}
