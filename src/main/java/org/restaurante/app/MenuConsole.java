package org.restaurante.app;

import org.restaurante.pedido.Pedido;
import org.restaurante.cardapio.ItemCardapio;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MenuConsole {
    private final Scanner entrada = new Scanner(System.in);
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(
            Locale.forLanguageTag("pt-BR"));

    public int lerOpcaoPrincipal() {
        exibirMensagem("\n1. Registrar novo pedido\n2. Listar pedidos do dia"
                + "\n3. Fechar restaurante\n4. Sair");
        return lerOpcao("Opção: ", 4);
    }

    public String lerNomeCliente() {
        while (true) {
            String nome = lerLinha("Nome do cliente: ").trim();
            if (!nome.isBlank()) { return nome; }
            exibirMensagem("Informe um nome não vazio.");
        }
    }

    public int lerOpcaoLanche() {
        exibirMensagem("\n1. X-Burger: R$ 16,00\n2. X-Salada: R$ 18,00"
                + "\n3. X-Bacon: R$ 22,00");
        return lerOpcao("Escolha o lanche: ", 3);
    }

    public boolean desejaAdicionarOutroItem() {
        return lerConfirmacao("Adicionar outro item? (s/n): ");
    }

    public int lerOpcaoAdicional() {
        exibirMensagem("\n1. Cheddar: R$ 4,00\n2. Bacon extra: R$ 6,00"
                + "\n3. Finalizar lanche sem mais adicionais");
        return lerOpcao("Escolha o adicional: ", 3);
    }

    private boolean lerConfirmacao(String mensagem) {
        while (true) {
            String resposta = lerLinha(mensagem).trim();
            if (resposta.equalsIgnoreCase("s")) { return true; }
            if (resposta.equalsIgnoreCase("n")) { return false; }
            exibirMensagem("Digite s ou n.");
        }
    }

    public void exibirPedidos(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) { exibirMensagem("Nenhum pedido registrado."); }
        for (Pedido pedido : pedidos) {
            exibirMensagem("Pedido " + pedido.getIdentificador()
                    + " | Cliente: " + pedido.getCliente());
            for (ItemCardapio item : pedido.consultarItens()) {
                exibirMensagem("  " + item.getDescricao() + " - " + moeda.format(item.getPreco()));
            }
            exibirMensagem("Valor do pedido: " + moeda.format(pedido.calcularTotal()));
        }
    }

    public void exibirTotalDoDia(BigDecimal total) {
        exibirMensagem("Total do dia: " + moeda.format(total));
    }

    public void exibirMensagem(String mensagem) { System.out.println(mensagem); }

    private int lerOpcao(String mensagem, int limite) {
        while (true) {
            try {
                int opcao = Integer.parseInt(lerLinha(mensagem).trim());
                if (opcao >= 1 && opcao <= limite) { return opcao; }
                exibirMensagem("Opção inexistente.");
            } catch (NumberFormatException e) {
                exibirMensagem("Digite um número inteiro válido.");
            }
        }
    }

    private String lerLinha(String mensagem) {
        System.out.print(mensagem);
        if (!entrada.hasNextLine()) { throw new EntradaEncerradaException(); }
        return entrada.nextLine();
    }

    public static class EntradaEncerradaException extends RuntimeException { }
}
