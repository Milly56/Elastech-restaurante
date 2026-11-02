package org.restaurante.cardapio;

import java.util.Objects;

public class ComCheddar implements ItemCardapio {
    private final ItemCardapio item;

    public ComCheddar(ItemCardapio item) {
        this.item = Objects.requireNonNull(item, "Item não pode ser nulo.");
    }

    @Override
    public String getDescricao() { return item.getDescricao() + " + cheddar"; }

    @Override
    public double getPreco() {
        return item.getPreco() + 4.00;
    }
}
