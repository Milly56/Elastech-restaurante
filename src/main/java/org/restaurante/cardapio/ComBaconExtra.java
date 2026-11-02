package org.restaurante.cardapio;

import java.util.Objects;

public class ComBaconExtra implements ItemCardapio {
    private final ItemCardapio item;

    public ComBaconExtra(ItemCardapio item) {
        this.item = Objects.requireNonNull(item, "Item não pode ser nulo.");
    }

    @Override
    public String getDescricao() { return item.getDescricao() + " + bacon extra"; }

    @Override
    public double getPreco() {
        return item.getPreco() + 6.00;
    }
}
