package org.restaurante.cardapio;

import java.math.BigDecimal;
import java.util.Objects;

public class ComBaconExtra implements ItemCardapio {
    private final ItemCardapio item;

    public ComBaconExtra(ItemCardapio item) {
        this.item = Objects.requireNonNull(item, "Item não pode ser nulo.");
    }

    @Override
    public String getDescricao() { return item.getDescricao() + " + bacon extra"; }

    @Override
    public BigDecimal getPreco() {
        return item.getPreco().add(new BigDecimal("6.00"));
    }
}
