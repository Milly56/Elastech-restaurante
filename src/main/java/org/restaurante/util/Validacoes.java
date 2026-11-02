package org.restaurante.util;

public final class Validacoes {
    private Validacoes() { }

    public static String textoNaoVazio(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
        return texto.trim();
    }
}
