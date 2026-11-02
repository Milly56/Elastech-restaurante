package org.restaurante.cardapio;

import java.math.BigDecimal;

public class LancheFactory {
    public Lanche criarLanche(int opcao) {
        return switch (opcao) {
            case 1 -> new Lanche("X-Burger", new BigDecimal("16.00"));
            case 2 -> new Lanche("X-Salada", new BigDecimal("18.00"));
            case 3 -> new Lanche("X-Bacon", new BigDecimal("22.00"));
            default -> throw new IllegalArgumentException("Lanche inexistente.");
        };
    }
}
