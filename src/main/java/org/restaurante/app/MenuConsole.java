package org.restaurante.app;

import org.restaurante.pedido.Pedido;
import org.restaurante.cardapio.ItemCardapio;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.restaurante.util.FormatadorMoeda;

public class MenuConsole {
    private final Scanner entrada = new Scanner(System.in);

    public int lerOpcaoPrincipal() {
        exibirMensagem("\n1. Registrar novo pedido\n2. Listar pedidos do dia"
                + "\n3. Modificar pedido\n4. Cancelar pedido"
                + "\n5. Fechar restaurante\n6. Sair");
        return lerOpcao("Opção: ", 6);
    }

    public long lerNumeroPedido() {
        return lerNumeroPositivo("Número do pedido: ", Long.MAX_VALUE);
    }

    public int lerOpcaoModificacao() {
        exibirMensagem("\n1. Acrescentar item\n2. Remover item"
                + "\n3. Trocar item\n4. Voltar");
        return lerOpcao("Modificação: ", 4);
    }

    public int lerIndiceItem(Pedido pedido) {
        List<ItemCardapio> itens = pedido.consultarItens();
        if (itens.isEmpty()) {
            exibirMensagem("O pedido não possui itens.");
            return -1;
        }
        for (int indice = 0; indice < itens.size(); indice++) {
            exibirMensagem((indice + 1) + ". " + itens.get(indice).getDescricao());
        }
        return lerOpcao("Item a retirar: ", itens.size()) - 1;
    }

    public String lerNomeCliente() {
        while (true) {
            String nome = lerLinha("Nome do cliente: ").trim();
            if (!nome.isBlank()) { return nome; }
            exibirMensagem("Informe um nome não vazio.");
        }
    }

    public int lerOpcaoLanche() {
        exibirMensagem("\n1. X-Burger: " + FormatadorMoeda.formatar(16.00)
                + "\n2. X-Salada: " + FormatadorMoeda.formatar(18.00)
                + "\n3. X-Bacon: " + FormatadorMoeda.formatar(22.00));
        return lerOpcao("Escolha o lanche: ", 3);
    }

    public boolean desejaAdicionarOutroItem() {
        return lerConfirmacao("Adicionar outro item? (s/n): ");
    }

    public boolean desejaRetirarIngrediente(String ingrediente) {
        return lerConfirmacao("Retirar " + ingrediente + "? (s/n): ");
    }

    public double lerGorjeta() {
        while (true) {
            String texto = lerLinha("Gorjeta (0 para nenhuma): ").trim();
            if (!texto.matches("[0-9]+([.,][0-9]{1,2})?")) {
                exibirMensagem("Informe um valor não negativo, com até duas casas decimais.");
                continue;
            }
            double valor = Double.parseDouble(texto.replace(',', '.'));
            if (Double.isFinite(valor)) {
                return valor;
            }
            exibirMensagem("Valor muito alto. Informe outro valor.");
        }
    }

    public int lerOpcaoAdicional() {
        exibirMensagem("\n1. Cheddar: " + FormatadorMoeda.formatar(4.00)
                + "\n2. Bacon extra: " + FormatadorMoeda.formatar(6.00)
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
                    + " | Cliente: " + pedido.getCliente()
                    + (pedido.isCancelado() ? " | CANCELADO" : " | ATIVO"));
            for (ItemCardapio item : pedido.consultarItens()) {
                exibirMensagem("  " + item.getDescricao() + " - " + FormatadorMoeda.formatar(item.getPreco()));
            }
            exibirMensagem("Valor do pedido: " + FormatadorMoeda.formatar(pedido.calcularTotal()));
        }
    }

    public void exibirTotalDoDia(double total) {
        exibirMensagem("Total do dia: " + FormatadorMoeda.formatar(total));
    }

    public void exibirFechamento(double total, Set<String> combinacoes) {
        exibirTotalDoDia(total);
        exibirMensagem("Combinações diferentes: " + combinacoes.size());
        for (String descricao : combinacoes) {
            exibirMensagem("  " + descricao);
        }
    }

    public void exibirRelatorioGorjetas(
            int quantidade, double gorjetas, double totalComGorjetas) {
        exibirMensagem("Quantidade de gorjetas: " + quantidade);
        exibirMensagem("Total de gorjetas: " + FormatadorMoeda.formatar(gorjetas));
        exibirMensagem("Total do dia com gorjetas: "
                + FormatadorMoeda.formatar(totalComGorjetas));
    }

    public void exibirMensagem(String mensagem) { System.out.println(mensagem); }

    private int lerOpcao(String mensagem, int limite) {
        return (int) lerNumeroPositivo(mensagem, limite);
    }

    private long lerNumeroPositivo(String mensagem, long limite) {
        while (true) {
            try {
                long numero = Long.parseLong(lerLinha(mensagem).trim());
                if (numero >= 1 && numero <= limite) { return numero; }
                exibirMensagem("Opção inexistente. Informe um número de 1 a " + limite + ".");
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
