package org.restaurante.app;

import org.restaurante.pedido.GerenciadorPedidos;

public class Main {
    public static void main(String[] args) {
        RestauranteFacade fachada = new RestauranteFacade(
                new MenuConsole(), new GerenciadorPedidos());
        while (fachada.executarProximaOperacao()) {
            // Mantém o fluxo principal até a saída.
        }
    }
}
