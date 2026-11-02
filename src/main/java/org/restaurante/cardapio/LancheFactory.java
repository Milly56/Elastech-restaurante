package org.restaurante.cardapio;

import java.math.BigDecimal;
import java.util.List;

public class LancheFactory {
    public Lanche criarLanche(int opcao) {
        return switch (opcao) {
            case 1 -> new Lanche("X-Burger", new BigDecimal("16.00"),
                    List.of("pão", "hambúrguer", "queijo", "alface", "tomate"));
            case 2 -> new Lanche("X-Salada", new BigDecimal("18.00"),
                    List.of("pão", "hambúrguer", "queijo", "alface", "tomate", "cebola"));
            case 3 -> new Lanche("X-Bacon", new BigDecimal("22.00"),
                    List.of("pão", "hambúrguer", "queijo", "bacon", "alface", "tomate", "cebola"));
            default -> throw new IllegalArgumentException("Lanche inexistente.");
        };
    }
}
