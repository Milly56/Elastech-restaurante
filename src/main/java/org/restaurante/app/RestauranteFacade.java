package org.restaurante.app;

import org.restaurante.cardapio.LancheFactory;
import org.restaurante.pedido.GerenciadorPedidos;
import org.restaurante.pedido.Pedido;

public class RestauranteFacade {
    private final MenuConsole menu;
    private final GerenciadorPedidos gerenciador;
    private final LancheFactory fabrica = new LancheFactory();

    public RestauranteFacade(MenuConsole menu, GerenciadorPedidos gerenciador) {
        this.menu = menu;
        this.gerenciador = gerenciador;
    }

    public boolean executarProximaOperacao() {
        try {
            switch (menu.lerOpcaoPrincipal()) {
                case 1 -> registrarPedido();
                case 2 -> menu.exibirPedidos(gerenciador.listarTodos());
                case 3 -> menu.exibirTotalDoDia(gerenciador.calcularTotalVendido());
                case 4 -> {
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
            pedido.adicionarLanche(fabrica.criarLanche(menu.lerOpcaoLanche()));
        } while (menu.desejaAdicionarOutroItem());
        menu.exibirMensagem("Pedido registrado com sucesso.");
    }
}
