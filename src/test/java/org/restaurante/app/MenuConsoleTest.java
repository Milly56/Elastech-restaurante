package org.restaurante.app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class MenuConsoleTest {
    @Test
    void gorjetaRepeteEntradasInvalidasEAceitaVirgulaPontoEZero() {
        InputStream entradaOriginal = System.in;
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(
                    "abc\n-1\n1,234\n1.2.3\n1e2\n\n5,50\n0\n2.75\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(saida, true, StandardCharsets.UTF_8));
            MenuConsole menu = new MenuConsole();
            assertEquals(5.50, menu.lerGorjeta(), 0.000001);
            assertEquals(0.00, menu.lerGorjeta(), 0.000001);
            assertEquals(2.75, menu.lerGorjeta(), 0.000001);
            assertTrue(saida.toString(StandardCharsets.UTF_8).contains("Informe um valor não negativo"));
        } finally {
            System.setIn(entradaOriginal);
            System.setOut(saidaOriginal);
        }
    }
}
