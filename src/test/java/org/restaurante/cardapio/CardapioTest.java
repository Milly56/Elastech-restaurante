package org.restaurante.cardapio;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CardapioTest {
    private final LancheFactory fabrica = new LancheFactory();

    @Test
    void rejeitaPrecoNegativoNoConstrutorENoSetter() {
        assertThrows(IllegalArgumentException.class,
                () -> new Lanche("X-Burger", new BigDecimal("-1.00")));
        Lanche lanche = fabrica.criarLanche(1);
        assertThrows(IllegalArgumentException.class,
                () -> lanche.setPreco(new BigDecimal("-1.00")));
        assertEquals(new BigDecimal("16.00"), lanche.getPreco());
    }

    @Test
    void rejeitaNomeInvalido() {
        for (String nome : new String[]{null, "", "   "}) {
            assertThrows(IllegalArgumentException.class,
                    () -> new Lanche(nome, new BigDecimal("16.00")));
        }
    }

    @Test
    void encadeiaCheddarEBaconExtra() {
        ItemCardapio item = new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3)));
        assertEquals(new BigDecimal("32.00"), item.getPreco());
        assertEquals("X-Bacon + cheddar + bacon extra", item.getDescricao());
    }

    @Test
    void criaInstanciasIndependentes() {
        Lanche primeiro = fabrica.criarLanche(1);
        Lanche segundo = fabrica.criarLanche(1);
        assertNotSame(primeiro, segundo);
        primeiro.setNome("Personalizado");
        primeiro.setPreco(new BigDecimal("20.00"));
        assertEquals("X-Burger", segundo.getNome());
        assertEquals(new BigDecimal("16.00"), segundo.getPreco());
    }

    @Test
    void preservaComposicoesEDescricao() {
        assertEquals(List.of("pão", "hambúrguer", "queijo", "alface", "tomate"),
                fabrica.criarLanche(1).getIngredientes());
        assertEquals(List.of("pão", "hambúrguer", "queijo", "alface", "tomate", "cebola"),
                fabrica.criarLanche(2).getIngredientes());
        Lanche bacon = fabrica.criarLanche(3);
        assertEquals(List.of("pão", "hambúrguer", "queijo", "bacon", "alface", "tomate", "cebola"),
                bacon.getIngredientes());
        assertEquals("X-Bacon", bacon.getDescricao());
        assertThrows(UnsupportedOperationException.class,
                () -> bacon.getIngredientes().add("outro"));
    }
}
