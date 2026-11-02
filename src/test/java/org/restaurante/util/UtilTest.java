package org.restaurante.util;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    void formataMoedaComEspacoComumEDuasCasas() {
        assertEquals("R$ 16,00", FormatadorMoeda.formatar(new BigDecimal("16.0")));
        assertEquals("R$ 1.234,56", FormatadorMoeda.formatar(new BigDecimal("1234.56")));
        assertEquals("R$ 0,00", FormatadorMoeda.formatar(BigDecimal.ZERO));
    }

    @Test
    void validaTextoReutilizavel() {
        assertEquals("Ana", Validacoes.textoNaoVazio("  Ana  ", "Cliente"));
        for (String texto : new String[]{null, "", "   "}) {
            assertThrows(IllegalArgumentException.class,
                    () -> Validacoes.textoNaoVazio(texto, "Cliente"));
        }
    }
}
