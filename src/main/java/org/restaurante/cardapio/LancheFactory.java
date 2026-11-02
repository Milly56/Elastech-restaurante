package org.restaurante.cardapio;

import java.util.List;

public class LancheFactory {
    public Lanche criarLanche(int opcao) {
        return switch (opcao) {
            case 1 -> new Lanche("X-Burger", 16.00,
                    List.of("pão", "hambúrguer", "queijo", "alface", "tomate"));
            case 2 -> new Lanche("X-Salada", 18.00,
                    List.of("pão", "hambúrguer", "queijo", "alface", "tomate", "cebola"));
            case 3 -> new Lanche("X-Bacon", 22.00,
                    List.of("pão", "hambúrguer", "queijo", "bacon", "alface", "tomate", "cebola"));
            default -> throw new IllegalArgumentException("Lanche inexistente.");
        };
    }
}
