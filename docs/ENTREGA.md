# Entrega do restaurante ElasTech

Etapa 0 e níveis 1 a 3 concluídos antes do bônus. Java 27, Maven e JUnit 5.10.2.

## 1. Estrutura de pacotes e classes

```text
.
├── .gitignore
├── README.md
├── pom.xml
├── docs/
│   └── ENTREGA.md
└── src/
    ├── main/java/org/restaurante/
    │   ├── app/
    │   │   ├── Main.java
    │   │   ├── MenuConsole.java
    │   │   └── RestauranteFacade.java
    │   ├── cardapio/
    │   │   ├── ItemCardapio.java
    │   │   ├── Lanche.java
    │   │   ├── LancheFactory.java
    │   │   ├── ComCheddar.java
    │   │   └── ComBaconExtra.java
    │   ├── pedido/
    │   │   ├── Pedido.java
    │   │   └── GerenciadorPedidos.java
    │   ├── util/
    │   │   ├── FormatadorMoeda.java
    │   │   └── Validacoes.java
    │   └── financeiro/
    │       ├── Caixa.java
    │       └── Transacao.java
    └── test/java/org/restaurante/
        ├── app/MenuConsoleTest.java
        ├── cardapio/CardapioTest.java
        ├── pedido/GerenciadorPedidosTest.java
        ├── pedido/NivelTresTest.java
        ├── util/UtilTest.java
        └── financeiro/FinanceiroTest.java
```

## 2. Código completo

Cada classe aparece em um bloco separado. As classes financeiras estão na seção 7.

### src/main/java/org/restaurante/app/Main.java

```java
package org.restaurante.app;

import org.restaurante.pedido.GerenciadorPedidos;

public class Main {
    public static void main(String[] args) {
        RestauranteFacade fachada = new RestauranteFacade(
                new MenuConsole(), new GerenciadorPedidos());
        while (fachada.executarProximaOperacao()) {
            // Mantém o fluxo principal até a saída.
        }
    }
}
```

### src/main/java/org/restaurante/app/MenuConsole.java

```java
package org.restaurante.app;

import org.restaurante.pedido.Pedido;
import org.restaurante.cardapio.ItemCardapio;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
        exibirMensagem("\n1. X-Burger: " + FormatadorMoeda.formatar(new BigDecimal("16.00"))
                + "\n2. X-Salada: " + FormatadorMoeda.formatar(new BigDecimal("18.00"))
                + "\n3. X-Bacon: " + FormatadorMoeda.formatar(new BigDecimal("22.00")));
        return lerOpcao("Escolha o lanche: ", 3);
    }

    public boolean desejaAdicionarOutroItem() {
        return lerConfirmacao("Adicionar outro item? (s/n): ");
    }

    public boolean desejaRetirarIngrediente(String ingrediente) {
        return lerConfirmacao("Retirar " + ingrediente + "? (s/n): ");
    }

    public BigDecimal lerGorjeta() {
        while (true) {
            String texto = lerLinha("Gorjeta (0 para nenhuma): ").trim();
            if (!texto.matches("[0-9]+([.,][0-9]{1,2})?")) {
                exibirMensagem("Informe um valor não negativo, com até duas casas decimais.");
                continue;
            }
            return new BigDecimal(texto.replace(',', '.'))
                    .setScale(2, RoundingMode.UNNECESSARY);
        }
    }

    public int lerOpcaoAdicional() {
        exibirMensagem("\n1. Cheddar: " + FormatadorMoeda.formatar(new BigDecimal("4.00"))
                + "\n2. Bacon extra: " + FormatadorMoeda.formatar(new BigDecimal("6.00"))
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

    public void exibirTotalDoDia(BigDecimal total) {
        exibirMensagem("Total do dia: " + FormatadorMoeda.formatar(total));
    }

    public void exibirFechamento(BigDecimal total, Set<String> combinacoes) {
        exibirTotalDoDia(total);
        exibirMensagem("Combinações diferentes: " + combinacoes.size());
        for (String descricao : combinacoes) {
            exibirMensagem("  " + descricao);
        }
    }

    public void exibirRelatorioGorjetas(
            int quantidade, BigDecimal gorjetas, BigDecimal totalComGorjetas) {
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
```

### src/main/java/org/restaurante/app/RestauranteFacade.java

```java
package org.restaurante.app;

import org.restaurante.cardapio.LancheFactory;
import org.restaurante.cardapio.Lanche;
import org.restaurante.cardapio.ItemCardapio;
import org.restaurante.cardapio.ComCheddar;
import org.restaurante.cardapio.ComBaconExtra;
import org.restaurante.pedido.GerenciadorPedidos;
import org.restaurante.pedido.Pedido;
import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;
import org.restaurante.financeiro.Caixa;

public class RestauranteFacade {
    private final MenuConsole menu;
    private final GerenciadorPedidos gerenciador;
    private final LancheFactory fabrica = new LancheFactory();
    private final Caixa caixa = Caixa.getInstancia();

    public RestauranteFacade(MenuConsole menu, GerenciadorPedidos gerenciador) {
        this.menu = menu;
        this.gerenciador = gerenciador;
    }

    public boolean executarProximaOperacao() {
        try {
            switch (menu.lerOpcaoPrincipal()) {
                case 1 -> registrarPedido();
                case 2 -> menu.exibirPedidos(gerenciador.listarTodos());
                case 3 -> modificarPedido();
                case 4 -> cancelarPedido();
                case 5 -> fecharRestaurante();
                case 6 -> {
                    menu.exibirMensagem("Até logo!");
                    return false;
                }
                default -> throw new IllegalStateException("Opção não validada.");
            }
            return true;
        } catch (MenuConsole.EntradaEncerradaException e) {
            menu.exibirMensagem("Entrada encerrada. Até logo!");
            return false;
        }
    }

    private void registrarPedido() {
        Pedido pedido = gerenciador.criarPedido(menu.lerNomeCliente());
        do {
            pedido.adicionarLanche(escolherItem());
        } while (menu.desejaAdicionarOutroItem());
        caixa.registrarGorjeta(pedido, menu.lerGorjeta());
        menu.exibirMensagem("Pedido registrado com sucesso.");
    }

    private void fecharRestaurante() {
        List<Pedido> pedidos = gerenciador.listarTodos();
        BigDecimal totalVendido = gerenciador.calcularTotalVendido();
        menu.exibirFechamento(totalVendido, gerenciador.consultarCombinacoes());
        menu.exibirRelatorioGorjetas(
                caixa.contarGorjetas(pedidos),
                caixa.calcularTotalGorjetas(pedidos),
                caixa.calcularTotalComGorjetas(totalVendido, pedidos));
    }

    private ItemCardapio escolherItem() {
        Lanche lanche = fabrica.criarLanche(menu.lerOpcaoLanche());
        lanche.setComQueijo(!menu.desejaRetirarIngrediente("queijo"));
        lanche.setComAlface(!menu.desejaRetirarIngrediente("alface"));
        lanche.setComTomate(!menu.desejaRetirarIngrediente("tomate"));
        lanche.setComCebola(!menu.desejaRetirarIngrediente("cebola"));
        return escolherAdicionais(lanche);
    }

    private Optional<Pedido> localizarPedidoAtivo() {
        Optional<Pedido> resultado = gerenciador.buscarPorNumero(menu.lerNumeroPedido());
        if (resultado.isEmpty()) {
            menu.exibirMensagem("Pedido não encontrado.");
            return Optional.empty();
        }
        if (resultado.get().isCancelado()) {
            menu.exibirMensagem("Pedido já cancelado.");
            return Optional.empty();
        }
        return resultado;
    }

    private void modificarPedido() {
        Optional<Pedido> resultado = localizarPedidoAtivo();
        if (resultado.isEmpty()) { return; }
        Pedido pedido = resultado.get();
        switch (menu.lerOpcaoModificacao()) {
            case 1 -> pedido.adicionarLanche(escolherItem());
            case 2 -> {
                if (!removerItem(pedido)) { return; }
            }
            case 3 -> {
                if (!removerItem(pedido)) { return; }
                pedido.adicionarLanche(escolherItem());
            }
            case 4 -> { return; }
            default -> throw new IllegalStateException("Modificação não validada.");
        }
        menu.exibirMensagem("Pedido atualizado com sucesso.");
    }

    private boolean removerItem(Pedido pedido) {
        int indice = menu.lerIndiceItem(pedido);
        if (indice < 0) { return false; }
        pedido.removerItem(indice);
        return true;
    }

    private void cancelarPedido() {
        Optional<Pedido> resultado = localizarPedidoAtivo();
        if (resultado.isEmpty()) { return; }
        gerenciador.cancelarPedido(resultado.get().getIdentificador());
        menu.exibirMensagem("Pedido cancelado com sucesso.");
    }

    private ItemCardapio escolherAdicionais(ItemCardapio item) {
        while (true) {
            switch (menu.lerOpcaoAdicional()) {
                case 1 -> item = new ComCheddar(item);
                case 2 -> item = new ComBaconExtra(item);
                case 3 -> { return item; }
                default -> throw new IllegalStateException("Adicional não validado.");
            }
        }
    }
}
```

### src/main/java/org/restaurante/cardapio/ComBaconExtra.java

```java
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
```

### src/main/java/org/restaurante/cardapio/ComCheddar.java

```java
package org.restaurante.cardapio;

import java.math.BigDecimal;
import java.util.Objects;

public class ComCheddar implements ItemCardapio {
    private final ItemCardapio item;

    public ComCheddar(ItemCardapio item) {
        this.item = Objects.requireNonNull(item, "Item não pode ser nulo.");
    }

    @Override
    public String getDescricao() { return item.getDescricao() + " + cheddar"; }

    @Override
    public BigDecimal getPreco() {
        return item.getPreco().add(new BigDecimal("4.00"));
    }
}
```

### src/main/java/org/restaurante/cardapio/ItemCardapio.java

```java
package org.restaurante.cardapio;

import java.math.BigDecimal;

public interface ItemCardapio {
    String getDescricao();
    BigDecimal getPreco();
}
```

### src/main/java/org/restaurante/cardapio/Lanche.java

```java
package org.restaurante.cardapio;

import java.math.BigDecimal;
import java.util.List;
import org.restaurante.util.Validacoes;

public class Lanche implements ItemCardapio {
    private String nome;
    private BigDecimal preco;
    private final List<String> ingredientes;
    private boolean comQueijo = true;
    private boolean comAlface = true;
    private boolean comTomate = true;
    private boolean comCebola = true;

    public Lanche(String nome, BigDecimal preco) {
        this(nome, preco, List.of());
    }

    public Lanche(String nome, BigDecimal preco, List<String> ingredientes) {
        setNome(nome);
        setPreco(preco);
        this.ingredientes = List.copyOf(ingredientes);
    }

    public String getNome() { return nome; }
    @Override
    public String getDescricao() {
        String descricao = nome;
        if (!comQueijo) { descricao += " sem queijo"; }
        if (!comAlface) { descricao += " sem alface"; }
        if (!comTomate) { descricao += " sem tomate"; }
        if (!comCebola) { descricao += " sem cebola"; }
        return descricao;
    }
    @Override
    public BigDecimal getPreco() { return preco; }
    public List<String> getIngredientes() { return ingredientes; }
    public boolean isComQueijo() { return comQueijo; }
    public void setComQueijo(boolean comQueijo) { this.comQueijo = comQueijo; }
    public boolean isComAlface() { return comAlface; }
    public void setComAlface(boolean comAlface) { this.comAlface = comAlface; }
    public boolean isComTomate() { return comTomate; }
    public void setComTomate(boolean comTomate) { this.comTomate = comTomate; }
    public boolean isComCebola() { return comCebola; }
    public void setComCebola(boolean comCebola) { this.comCebola = comCebola; }

    public void setNome(String nome) {
        this.nome = Validacoes.textoNaoVazio(nome, "Nome do lanche");
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.signum() < 0) {
            throw new IllegalArgumentException("Preço deve ser informado e não pode ser negativo.");
        }
        this.preco = preco;
    }
}
```

### src/main/java/org/restaurante/cardapio/LancheFactory.java

```java
package org.restaurante.cardapio;

import java.math.BigDecimal;
import java.util.List;

public class LancheFactory {
    public Lanche criarLanche(int opcao) {
        return switch (opcao) {
            case 1 -> new Lanche("X-Burger", new BigDecimal("16.00"),
                    List.of("pão", "hambúrguer", "queijo", "alface", "tomate"));
            case 2 -> new Lanche("X-Salada", new BigDecimal("18.00"),
                    List.of("pão", "hambúrguer", "queijo", "alface", "tomate", "cebola"));
            case 3 -> new Lanche("X-Bacon", new BigDecimal("22.00"),
                    List.of("pão", "hambúrguer", "queijo", "bacon", "alface", "tomate", "cebola"));
            default -> throw new IllegalArgumentException("Lanche inexistente.");
        };
    }
}
```

### src/main/java/org/restaurante/pedido/GerenciadorPedidos.java

```java
package org.restaurante.pedido;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.restaurante.cardapio.ItemCardapio;

public class GerenciadorPedidos {
    private final Map<Long, Pedido> pedidos = new HashMap<>();
    private long proximoNumero = 1;

    public Pedido criarPedido(String cliente) {
        long seguinte = Math.incrementExact(proximoNumero);
        Pedido pedido = new Pedido(proximoNumero, cliente);
        if (pedidos.putIfAbsent(proximoNumero, pedido) != null) {
            throw new IllegalStateException("Número de pedido já utilizado.");
        }
        proximoNumero = seguinte;
        return pedido;
    }   

    public Optional<Pedido> buscarPorNumero(long numero) {
        return Optional.ofNullable(pedidos.get(numero));
    }

    public List<Pedido> listarTodos() { return List.copyOf(pedidos.values()); }

    public void cancelarPedido(long numero) {
        Pedido pedido = buscarPorNumero(numero).orElseThrow(
                () -> new IllegalArgumentException("Pedido não encontrado."));
        pedido.cancelar();
    }

    public BigDecimal calcularTotalVendido() {
        BigDecimal total = new BigDecimal("0.00");
        for (Pedido pedido : pedidos.values()) {
            if (!pedido.isCancelado()) {
                total = total.add(pedido.calcularTotal());
            }
        }
        return total;
    }

    public Set<String> consultarCombinacoes() {
        Set<String> combinacoes = new HashSet<>();
        for (Pedido pedido : pedidos.values()) {
            if (!pedido.isCancelado()) {
                for (ItemCardapio item : pedido.consultarItens()) {
                    combinacoes.add(item.getDescricao());
                }
            }
        }
        return Set.copyOf(combinacoes);
    }
}
```

### src/main/java/org/restaurante/pedido/Pedido.java

```java
package org.restaurante.pedido;

import org.restaurante.cardapio.ItemCardapio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.restaurante.util.Validacoes;

public class Pedido {
    private final long identificador;
    private final String cliente;
    private final List<ItemCardapio> itens = new ArrayList<>();
    private boolean cancelado;

    public Pedido(long identificador, String cliente) {
        this.identificador = identificador;
        this.cliente = Validacoes.textoNaoVazio(cliente, "Nome do cliente");
    }

    public void adicionarLanche(ItemCardapio item) {
        validarAtivo();
        if (item == null) {
            throw new IllegalArgumentException("Lanche não pode ser nulo.");
        }
        itens.add(item);
    }

    public void removerItem(int indice) {
        validarAtivo();
        if (indice < 0 || indice >= itens.size()) {
            throw new IllegalArgumentException("Item inexistente.");
        }
        itens.remove(indice);
    }

    public void cancelar() { cancelado = true; }
    public boolean isCancelado() { return cancelado; }

    private void validarAtivo() {
        if (cancelado) {
            throw new IllegalStateException("Pedido cancelado não pode ser modificado.");
        }
    }

    public List<ItemCardapio> consultarItens() { return List.copyOf(itens); }
    public long getIdentificador() { return identificador; }
    public String getCliente() { return cliente; }

    public BigDecimal calcularTotal() {
        BigDecimal total = new BigDecimal("0.00");
        for (ItemCardapio item : itens) {
            total = total.add(item.getPreco());
        }
        return total;
    }
}
```

### src/main/java/org/restaurante/util/FormatadorMoeda.java

```java
package org.restaurante.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class FormatadorMoeda {
    private FormatadorMoeda() { }

    public static String formatar(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
                .format(valor).replace('\u00a0', ' ').replace('\u202f', ' ');
    }
}
```

### src/main/java/org/restaurante/util/Validacoes.java

```java
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
```

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>elastech</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>27</maven.compiler.source>
        <maven.compiler.target>27</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <junit.version>5.10.2</junit.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
        </plugins>
    </build>

</project>
```

## 3. Um objeto novo por escolha

A fábrica executa `new Lanche(...)` em todas as chamadas. Registro, acréscimo e troca passam por essa fábrica. A personalização atua no lanche recém-criado; os decorators também são novos. Os testes verificam a independência entre itens e entre pedidos.

## 4. Testes JUnit completos

Resultado real: **22 testes, zero falhas, zero erros e zero ignorados**.

| Teste | Resultado esperado, confirmado |
|---|---|
| `rejeitaPrecoNegativoNoConstrutorENoSetter` | Lança exceção e preserva o preço anterior. |
| `rejeitaNomeInvalido` | Rejeita nome nulo, vazio e composto só de espaços. |
| `encadeiaCheddarEBaconExtra` | Retorna R$ 32,00 e os sufixos exatos. |
| `criaInstanciasIndependentes` | Alterar um lanche não altera outro. |
| `preservaComposicoesEDescricao` | Composições padrão corretas e lista protegida. |
| `preservaPedidosNumeracaoEBuscaAusente` | Números 1 e 2 preservados, consulta ausente via Optional. |
| `somaItensDecoradosEPedidosDoDia` | Pedido de R$ 48,00 e vendas de R$ 66,00. |
| `removerIngredientesPreservaPrecoEOrdenaDescricao` | Remoções na ordem definida, R$ 22,00 na base e R$ 32,00 com adicionais. |
| `personalizacaoNaoVazaEntreItensOuPedidos` | Outros itens e pedidos conservam sua descrição. |
| `cancelamentoExcluiTotalECombinacoesSemReutilizarNumero` | Somente o pedido ativo entra no total e nas combinações. |
| `remocaoRecalculaPedidoDiaECombinacoesAteFicarVazio` | Totais atualizados até zero; índices inválidos rejeitados. |
| `trocaRemoveAntigoEAcrescentaNovoSemAlterarOriginal` | Nova instância, total de R$ 18,00, antigo preservado. |
| `combinacoesRepetidasContamUmaVezEOrdensDiferentesContamSeparadas` | Duas descrições distintas, repetição eliminada. |
| `formataMoedaComEspacoComumEDuasCasas` | 16.0 formatado exatamente como R$ 16,00. |
| `validaTextoReutilizavel` | Remove espaços nas pontas e rejeita texto inválido. |
| `transacoesIguaisPossuemMesmoHashIndependentementeDaEscala` | Mesmo pedido e valor produzem igualdade e hash iguais. |
| `hashSetImpedeTransacoesDuplicadas` | Duas transações iguais deixam um elemento no conjunto. |
| `caixaSempreRetornaMesmaInstancia` | As duas referências de Caixa são idênticas. |
| `cancelamentoExcluiGorjetaSemAlterarValorDoPedido` | Exclui gorjeta cancelada e preserva vendas separadas. |
| `zeroNaoContaComoGorjetaEDuplicataNaoAumentaTotal` | Zero não conta; transação repetida conta uma vez. |
| `rejeitaGorjetaNegativaNulaOuDePedidoCancelado` | Lança exceção para os três casos. |
| `gorjetaRepeteEntradasInvalidasEAceitaVirgulaPontoEZero` | Rejeita entradas inválidas e lê 5,50, 0 e 2.75 corretamente. |

### src/test/java/org/restaurante/app/MenuConsoleTest.java

```java
package org.restaurante.app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
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
            assertEquals(new BigDecimal("5.50"), menu.lerGorjeta());
            assertEquals(new BigDecimal("0.00"), menu.lerGorjeta());
            assertEquals(new BigDecimal("2.75"), menu.lerGorjeta());
            assertTrue(saida.toString(StandardCharsets.UTF_8).contains("Informe um valor não negativo"));
        } finally {
            System.setIn(entradaOriginal);
            System.setOut(saidaOriginal);
        }
    }
}
```

### src/test/java/org/restaurante/cardapio/CardapioTest.java

```java
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
```

### src/test/java/org/restaurante/financeiro/FinanceiroTest.java

```java
package org.restaurante.financeiro;

import org.junit.jupiter.api.Test;
import org.restaurante.cardapio.LancheFactory;
import org.restaurante.pedido.Pedido;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FinanceiroTest {
    @Test
    void transacoesIguaisPossuemMesmoHashIndependentementeDaEscala() {
        Transacao primeira = new Transacao(1, new BigDecimal("4.0"));
        Transacao segunda = new Transacao(1, new BigDecimal("4.00"));
        assertEquals(primeira, segunda);
        assertEquals(primeira.hashCode(), segunda.hashCode());
        assertNotEquals(primeira, new Transacao(2, new BigDecimal("4.00")));
        assertNotEquals(primeira, new Transacao(1, new BigDecimal("5.00")));
    }

    @Test
    void hashSetImpedeTransacoesDuplicadas() {
        HashSet<Transacao> transacoes = new HashSet<>();
        transacoes.add(new Transacao(1, new BigDecimal("4.0")));
        transacoes.add(new Transacao(1, new BigDecimal("4.00")));
        assertEquals(1, transacoes.size());
    }

    @Test
    void caixaSempreRetornaMesmaInstancia() {
        assertSame(Caixa.getInstancia(), Caixa.getInstancia());
    }

    @Test
    void cancelamentoExcluiGorjetaSemAlterarValorDoPedido() {
        Caixa caixa = Caixa.getInstancia();
        Pedido primeiro = new Pedido(10001, "Ana");
        Pedido segundo = new Pedido(10002, "Bia");
        primeiro.adicionarLanche(new LancheFactory().criarLanche(1));
        segundo.adicionarLanche(new LancheFactory().criarLanche(2));
        caixa.registrarGorjeta(primeiro, new BigDecimal("4.00"));
        caixa.registrarGorjeta(segundo, new BigDecimal("6.00"));
        List<Pedido> pedidos = List.of(primeiro, segundo);
        assertEquals(new BigDecimal("10.00"), caixa.calcularTotalGorjetas(pedidos));
        assertEquals(2, caixa.contarGorjetas(pedidos));
        segundo.cancelar();
        assertEquals(new BigDecimal("16.00"), primeiro.calcularTotal());
        assertEquals(new BigDecimal("4.00"), caixa.calcularTotalGorjetas(pedidos));
        assertEquals(1, caixa.contarGorjetas(pedidos));
        assertEquals(new BigDecimal("20.00"),
                caixa.calcularTotalComGorjetas(primeiro.calcularTotal(), pedidos));
    }

    @Test
    void zeroNaoContaComoGorjetaEDuplicataNaoAumentaTotal() {
        Caixa caixa = Caixa.getInstancia();
        Pedido pedido = new Pedido(10003, "Caio");
        List<Pedido> pedidos = List.of(pedido);
        caixa.registrarGorjeta(pedido, BigDecimal.ZERO);
        assertEquals(0, caixa.contarGorjetas(pedidos));
        caixa.registrarGorjeta(pedido, new BigDecimal("3.0"));
        caixa.registrarGorjeta(pedido, new BigDecimal("3.00"));
        assertEquals(1, caixa.contarGorjetas(pedidos));
        assertEquals(new BigDecimal("3.00"), caixa.calcularTotalGorjetas(pedidos));
    }

    @Test
    void rejeitaGorjetaNegativaNulaOuDePedidoCancelado() {
        Pedido pedido = new Pedido(10004, "Dani");
        Caixa caixa = Caixa.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> caixa.registrarGorjeta(pedido, new BigDecimal("-1.00")));
        assertThrows(IllegalArgumentException.class, () -> caixa.registrarGorjeta(pedido, null));
        pedido.cancelar();
        assertThrows(IllegalArgumentException.class,
                () -> caixa.registrarGorjeta(pedido, new BigDecimal("1.00")));
    }
}
```

### src/test/java/org/restaurante/pedido/GerenciadorPedidosTest.java

```java
package org.restaurante.pedido;

import org.junit.jupiter.api.Test;
import org.restaurante.cardapio.*;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class GerenciadorPedidosTest {
    @Test
    void preservaPedidosNumeracaoEBuscaAusente() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido primeiro = gerenciador.criarPedido("Ana");
        assertThrows(IllegalArgumentException.class, () -> gerenciador.criarPedido("   "));
        Pedido segundo = gerenciador.criarPedido("Bia");
        assertEquals(1, primeiro.getIdentificador());
        assertEquals(2, segundo.getIdentificador());
        assertSame(primeiro, gerenciador.buscarPorNumero(1).orElseThrow());
        assertSame(segundo, gerenciador.buscarPorNumero(2).orElseThrow());
        assertTrue(gerenciador.buscarPorNumero(99).isEmpty());
        assertEquals(2, gerenciador.listarTodos().size());
    }

    @Test
    void somaItensDecoradosEPedidosDoDia() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        LancheFactory fabrica = new LancheFactory();
        assertEquals(new BigDecimal("0.00"), gerenciador.calcularTotalVendido());
        Pedido primeiro = gerenciador.criarPedido("Ana");
        primeiro.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        primeiro.adicionarLanche(fabrica.criarLanche(1));
        Pedido segundo = gerenciador.criarPedido("Bia");
        segundo.adicionarLanche(fabrica.criarLanche(2));
        assertEquals(new BigDecimal("48.00"), primeiro.calcularTotal());
        assertEquals(new BigDecimal("66.00"), gerenciador.calcularTotalVendido());
    }
}
```

### src/test/java/org/restaurante/pedido/NivelTresTest.java

```java
package org.restaurante.pedido;

import org.junit.jupiter.api.Test;
import org.restaurante.cardapio.*;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class NivelTresTest {
    private final LancheFactory fabrica = new LancheFactory();

    @Test
    void removerIngredientesPreservaPrecoEOrdenaDescricao() {
        Lanche lanche = fabrica.criarLanche(3);
        assertTrue(lanche.isComQueijo());
        assertTrue(lanche.isComAlface());
        assertTrue(lanche.isComTomate());
        assertTrue(lanche.isComCebola());
        assertEquals("X-Bacon", lanche.getDescricao());
        lanche.setComCebola(false);
        lanche.setComTomate(false);
        lanche.setComAlface(false);
        lanche.setComQueijo(false);
        assertEquals("X-Bacon sem queijo sem alface sem tomate sem cebola", lanche.getDescricao());
        assertEquals(new BigDecimal("22.00"), lanche.getPreco());
        ItemCardapio completo = new ComBaconExtra(new ComCheddar(lanche));
        assertEquals("X-Bacon sem queijo sem alface sem tomate sem cebola + cheddar + bacon extra",
                completo.getDescricao());
        assertEquals(new BigDecimal("32.00"), completo.getPreco());
        lanche.setComQueijo(true);
        assertFalse(lanche.getDescricao().contains("sem queijo"));
    }

    @Test
    void personalizacaoNaoVazaEntreItensOuPedidos() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido primeiro = gerenciador.criarPedido("Ana");
        Pedido segundo = gerenciador.criarPedido("Bia");
        Lanche personalizado = fabrica.criarLanche(3);
        personalizado.setComQueijo(false);
        personalizado.setComCebola(false);
        primeiro.adicionarLanche(personalizado);
        primeiro.adicionarLanche(fabrica.criarLanche(3));
        segundo.adicionarLanche(fabrica.criarLanche(3));
        assertEquals("X-Bacon sem queijo sem cebola", primeiro.consultarItens().get(0).getDescricao());
        assertEquals("X-Bacon", primeiro.consultarItens().get(1).getDescricao());
        assertEquals("X-Bacon", segundo.consultarItens().get(0).getDescricao());
        assertNotSame(primeiro.consultarItens().get(0), primeiro.consultarItens().get(1));
        assertNotSame(primeiro.consultarItens().get(1), segundo.consultarItens().get(0));
    }

    @Test
    void cancelamentoExcluiTotalECombinacoesSemReutilizarNumero() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido cancelado = gerenciador.criarPedido("Ana");
        cancelado.adicionarLanche(fabrica.criarLanche(3));
        Pedido ativo = gerenciador.criarPedido("Bia");
        ativo.adicionarLanche(fabrica.criarLanche(1));
        gerenciador.cancelarPedido(cancelado.getIdentificador());
        assertTrue(cancelado.isCancelado());
        assertEquals(new BigDecimal("16.00"), gerenciador.calcularTotalVendido());
        assertEquals(Set.of("X-Burger"), gerenciador.consultarCombinacoes());
        assertSame(cancelado, gerenciador.buscarPorNumero(1).orElseThrow());
        assertEquals(3, gerenciador.criarPedido("Caio").getIdentificador());
        assertThrows(IllegalStateException.class, () -> cancelado.removerItem(0));
        assertThrows(IllegalStateException.class,
                () -> cancelado.adicionarLanche(fabrica.criarLanche(2)));
        assertThrows(IllegalArgumentException.class, () -> gerenciador.cancelarPedido(99));
    }

    @Test
    void remocaoRecalculaPedidoDiaECombinacoesAteFicarVazio() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido pedido = gerenciador.criarPedido("Ana");
        pedido.adicionarLanche(fabrica.criarLanche(1));
        pedido.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        assertEquals(new BigDecimal("48.00"), pedido.calcularTotal());
        pedido.removerItem(1);
        assertEquals(new BigDecimal("16.00"), pedido.calcularTotal());
        assertEquals(new BigDecimal("16.00"), gerenciador.calcularTotalVendido());
        assertEquals(Set.of("X-Burger"), gerenciador.consultarCombinacoes());
        pedido.removerItem(0);
        assertEquals(new BigDecimal("0.00"), pedido.calcularTotal());
        assertEquals(new BigDecimal("0.00"), gerenciador.calcularTotalVendido());
        assertTrue(gerenciador.consultarCombinacoes().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> pedido.removerItem(0));
        assertThrows(IllegalArgumentException.class, () -> pedido.removerItem(-1));
    }

    @Test
    void trocaRemoveAntigoEAcrescentaNovoSemAlterarOriginal() {
        Pedido pedido = new Pedido(1, "Ana");
        Lanche antigo = fabrica.criarLanche(1);
        pedido.adicionarLanche(antigo);
        pedido.removerItem(0);
        pedido.adicionarLanche(fabrica.criarLanche(2));
        assertEquals(1, pedido.consultarItens().size());
        assertNotSame(antigo, pedido.consultarItens().get(0));
        assertEquals("X-Burger", antigo.getDescricao());
        assertEquals(new BigDecimal("18.00"), pedido.calcularTotal());
    }

    @Test
    void combinacoesRepetidasContamUmaVezEOrdensDiferentesContamSeparadas() {
        GerenciadorPedidos gerenciador = new GerenciadorPedidos();
        Pedido pedido = gerenciador.criarPedido("Ana");
        pedido.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        pedido.adicionarLanche(new ComBaconExtra(new ComCheddar(fabrica.criarLanche(3))));
        pedido.adicionarLanche(new ComCheddar(new ComBaconExtra(fabrica.criarLanche(3))));
        assertEquals(Set.of("X-Bacon + cheddar + bacon extra",
                "X-Bacon + bacon extra + cheddar"), gerenciador.consultarCombinacoes());
    }
}
```

### src/test/java/org/restaurante/util/UtilTest.java

```java
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
```

## 5. Execução real no console

```text
1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 1
Nome do cliente: Ana

1. X-Burger: R$ 16,00
2. X-Salada: R$ 18,00
3. X-Bacon: R$ 22,00
Escolha o lanche: 3
Retirar queijo? (s/n): s
Retirar alface? (s/n): n
Retirar tomate? (s/n): n
Retirar cebola? (s/n): s

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 1

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 2

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 3
Adicionar outro item? (s/n): n
Gorjeta (0 para nenhuma): 5,00
Pedido registrado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 3
Número do pedido: 1

1. Acrescentar item
2. Remover item
3. Trocar item
4. Voltar
Modificação: 1

1. X-Burger: R$ 16,00
2. X-Salada: R$ 18,00
3. X-Bacon: R$ 22,00
Escolha o lanche: 1
Retirar queijo? (s/n): n
Retirar alface? (s/n): n
Retirar tomate? (s/n): n
Retirar cebola? (s/n): n

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 3
Pedido atualizado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 1
Nome do cliente: Bia

1. X-Burger: R$ 16,00
2. X-Salada: R$ 18,00
3. X-Bacon: R$ 22,00
Escolha o lanche: 2
Retirar queijo? (s/n): n
Retirar alface? (s/n): n
Retirar tomate? (s/n): n
Retirar cebola? (s/n): n

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 3
Adicionar outro item? (s/n): n
Gorjeta (0 para nenhuma): 2,00
Pedido registrado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 4
Número do pedido: 2
Pedido cancelado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 2
Pedido 1 | Cliente: Ana | ATIVO
  X-Bacon sem queijo sem cebola + cheddar + bacon extra - R$ 32,00
  X-Burger - R$ 16,00
Valor do pedido: R$ 48,00
Pedido 2 | Cliente: Bia | CANCELADO
  X-Salada - R$ 18,00
Valor do pedido: R$ 18,00

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 5
Total do dia: R$ 48,00
Combinações diferentes: 2
  X-Burger
  X-Bacon sem queijo sem cebola + cheddar + bacon extra
Quantidade de gorjetas: 1
Total de gorjetas: R$ 5,00
Total do dia com gorjetas: R$ 53,00

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 6
Até logo!
```

## 6. Limitação das combinações

O HashSet compara descrições completas. Os mesmos adicionais em ordens diferentes contam separadamente, conforme a limitação aceita.

## 7. Nível 4: BÔNUS

Implementado por inteiro após os níveis obrigatórios estarem testados e commitados. Caixa Singleton, transações imutáveis, deduplicação, gorjetas separadas das vendas e exclusão de gorjetas canceladas. Zero significa ausência de gorjeta.

### src/main/java/org/restaurante/financeiro/Caixa.java

```java
package org.restaurante.financeiro;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.restaurante.pedido.Pedido;

public final class Caixa {
    private static final Caixa INSTANCIA = new Caixa();
    private final Set<Transacao> transacoes = new HashSet<>();

    private Caixa() { }

    public static Caixa getInstancia() { return INSTANCIA; }

    public void registrarGorjeta(Pedido pedido, BigDecimal valor) {
        if (pedido == null || pedido.isCancelado()) {
            throw new IllegalArgumentException("Informe um pedido ativo.");
        }
        Transacao transacao = new Transacao(pedido.getIdentificador(), valor);
        if (transacao.getValor().signum() > 0) {
            transacoes.add(transacao);
        }
    }

    private Set<Transacao> consultarGorjetasAtivas(Collection<Pedido> pedidos) {
        Set<Long> numerosAtivos = new HashSet<>();
        for (Pedido pedido : pedidos) {
            if (!pedido.isCancelado()) {
                numerosAtivos.add(pedido.getIdentificador());
            }
        }
        Set<Transacao> ativas = new HashSet<>();
        for (Transacao transacao : transacoes) {
            if (numerosAtivos.contains(transacao.getNumeroPedido())) {
                ativas.add(transacao);
            }
        }
        return ativas;
    }

    public int contarGorjetas(Collection<Pedido> pedidos) {
        return consultarGorjetasAtivas(pedidos).size();
    }

    public BigDecimal calcularTotalGorjetas(Collection<Pedido> pedidos) {
        BigDecimal total = new BigDecimal("0.00");
        for (Transacao transacao : consultarGorjetasAtivas(pedidos)) {
            total = total.add(transacao.getValor());
        }
        return total;
    }

    public BigDecimal calcularTotalComGorjetas(
            BigDecimal totalVendido, Collection<Pedido> pedidos) {
        return totalVendido.add(calcularTotalGorjetas(pedidos));
    }
}
```

### src/main/java/org/restaurante/financeiro/Transacao.java

```java
package org.restaurante.financeiro;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Transacao {
    private final long numeroPedido;
    private final BigDecimal valor;

    public Transacao(long numeroPedido, BigDecimal valor) {
        if (numeroPedido <= 0) {
            throw new IllegalArgumentException("Número do pedido deve ser positivo.");
        }
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("Valor deve ser informado e não pode ser negativo.");
        }
        this.numeroPedido = numeroPedido;
        this.valor = valor.setScale(2, RoundingMode.UNNECESSARY);
    }

    public long getNumeroPedido() { return numeroPedido; }
    public BigDecimal getValor() { return valor; }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) { return true; }
        if (!(objeto instanceof Transacao outra)) { return false; }
        return numeroPedido == outra.numeroPedido && valor.equals(outra.valor);
    }

    @Override
    public int hashCode() { return Objects.hash(numeroPedido, valor); }
}
```

## 8. README.md completo

````markdown
# Restaurante ElasTech

![ElasTech](https://img.shields.io/badge/Bootcamp-ElasTech-8A2BE2)
![Soulcode](https://img.shields.io/badge/Parceira-Soulcode-0056D2)
![PagBank](https://img.shields.io/badge/Parceiro-PagBank-00A868)
![Java](https://img.shields.io/badge/Java-27-ED8B00)
![JUnit](https://img.shields.io/badge/JUnit-5.10.2-25A162)
![Git](https://img.shields.io/badge/Git-versionamento-F05032)
![GitHub](https://img.shields.io/badge/GitHub-repositório-181717)
![Status](https://img.shields.io/badge/Status-níveis_1_a_3_e_bônus_concluídos-success)

## Sobre o projeto

Sistema de pedidos de restaurante em console, desenvolvido como prática do bootcamp ElasTech, com Soulcode e PagBank como parceiros do bootcamp. O foco é separar as responsabilidades de entrada e saída, coordenação, domínio e gerenciamento de pedidos.

Os valores monetários usam `BigDecimal`. Os pedidos ficam em memória durante a execução.

## Funcionalidades

- Registrar pedidos com numeração automática a partir de 1.
- Listar pedidos ativos e cancelados, com cliente, itens e valor.
- Escolher lanches padrão, sempre com uma nova instância por escolha.
- Retirar queijo, alface, tomate e cebola sem alterar o preço.
- Encadear adicionais de cheddar e bacon extra.
- Acrescentar, remover e trocar itens de pedidos ativos.
- Cancelar pedidos sem reutilizar seus números.
- Fechar o restaurante para consultar o total e as combinações distintas, retornando ao menu.
- Repetir entradas inválidas de opções, números, índices e respostas s/n.
- **Bônus:** registrar gorjetas, impedir transações duplicadas e apresentar um relatório de caixa.

## Cardápio e adicionais

| Lanche | Preço | Composição padrão |
|---|---:|---|
| X-Burger | R$ 16,00 | pão, hambúrguer, queijo, alface e tomate |
| X-Salada | R$ 18,00 | pão, hambúrguer, queijo, alface, tomate e cebola |
| X-Bacon | R$ 22,00 | pão, hambúrguer, queijo, bacon, alface, tomate e cebola |

| Adicional | Acréscimo | Sufixo exato da descrição |
|---|---:|---|
| Cheddar | R$ 4,00 | ` + cheddar` |
| Bacon extra | R$ 6,00 | ` + bacon extra` |

**X-Bacon + cheddar + bacon extra = R$ 32,00** (22 + 4 + 6).

A lista de ingredientes guarda a composição padrão. Os quatro indicadores de personalização começam em `true`, conforme o exercício. As remoções aparecem na descrição na ordem: queijo, alface, tomate e cebola.

## Arquitetura

```text
.
├── .gitignore
├── README.md
├── pom.xml
├── docs/
│   └── ENTREGA.md
└── src/
    ├── main/java/org/restaurante/
    │   ├── app/
    │   │   ├── Main.java
    │   │   ├── MenuConsole.java
    │   │   └── RestauranteFacade.java
    │   ├── cardapio/
    │   │   ├── ItemCardapio.java
    │   │   ├── Lanche.java
    │   │   ├── LancheFactory.java
    │   │   ├── ComCheddar.java
    │   │   └── ComBaconExtra.java
    │   ├── pedido/
    │   │   ├── Pedido.java
    │   │   └── GerenciadorPedidos.java
    │   ├── util/
    │   │   ├── FormatadorMoeda.java
    │   │   └── Validacoes.java
    │   └── financeiro/
    │       ├── Caixa.java
    │       └── Transacao.java
    └── test/java/org/restaurante/
        ├── app/MenuConsoleTest.java
        ├── cardapio/CardapioTest.java
        ├── pedido/GerenciadorPedidosTest.java
        ├── pedido/NivelTresTest.java
        ├── util/UtilTest.java
        └── financeiro/FinanceiroTest.java
```

| Pacote | Responsabilidade |
|---|---|
| `app` | Inicialização, fluxo principal, coordenação e interação no console |
| `cardapio` | Lanches, composição, personalização, fábrica e adicionais |
| `pedido` | Itens do pedido, cancelamento, armazenamento em HashMap, totais e combinações em HashSet |
| `util` | Formatação monetária usada em toda exibição de valores e validação de texto reutilizada por Lanche e Pedido |
| `financeiro` | Bônus: transações e caixa único para contabilizar gorjetas de pedidos ativos |

`FormatadorMoeda` e `Validacoes` têm construtor privado e métodos estáticos, sem estado. Nenhuma delas lê do teclado.

## Regras de ouro

| Classe | Faz | Não faz |
|---|---|---|
| `Main` | Inicia os componentes e mantém o loop principal | Não lê, não imprime e não calcula valores de negócio |
| `MenuConsole` | Lê todas as entradas, valida formatos e opções, repete tentativas e apresenta resultados | Não soma vendas nem gorjetas |
| `RestauranteFacade` | Coordena menu, fábrica, pedidos e caixa | Não lê diretamente, não imprime diretamente e não soma valores |
| `GerenciadorPedidos` | Gera números, mantém pedidos, busca com Optional, cancela e calcula vendas e combinações | Não interage com o console nem inclui gorjetas nas vendas |

`Pedido` soma os preços de seus itens usando um laço. `GerenciadorPedidos` soma os pedidos ativos também com um laço. Os resultados são recalculados a cada consulta.

Cada seleção chama a fábrica e recebe um novo `Lanche`. A personalização é feita antes dos adicionais. Trocar um item significa remover o antigo e acrescentar um novo.

## Padrões de projeto

- **Facade:** `RestauranteFacade` coordena os casos de uso.
- **Factory:** `LancheFactory` centraliza a construção dos lanches padrão.
- **Decorator:** `ComCheddar` e `ComBaconExtra` envolvem um `ItemCardapio` e acrescentam descrição e preço.
- **Singleton, bônus:** `Caixa` tem construtor privado e uma única instância, acessada por `getInstancia()`.

## Bônus: caixa e gorjetas

Ao finalizar o registro, informe a gorjeta. Zero significa sem gorjeta. A leitura aceita vírgula ou ponto decimal, com até duas casas, e rejeita valores negativos ou textos inválidos.

As gorjetas não alteram o total do pedido nem o total vendido. Cada gorjeta positiva vira uma `Transacao`, identificada pelo número do pedido e pelo valor. O valor é normalizado para duas casas, então `4.0` e `4.00` são equivalentes. O `HashSet` do caixa impede duplicatas.

O fechamento mostra vendas, quantidade de gorjetas, total de gorjetas e vendas somadas às gorjetas. Pedidos cancelados ficam fora desses totais e das combinações.

## Exemplo de execução real

Transcrição capturada no terminal, com as entradas digitadas. O pedido da Ana recebe adicionais e ingredientes removidos. Depois, um item é acrescentado. O pedido da Bia e sua gorjeta são cancelados.

```text
1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 1
Nome do cliente: Ana

1. X-Burger: R$ 16,00
2. X-Salada: R$ 18,00
3. X-Bacon: R$ 22,00
Escolha o lanche: 3
Retirar queijo? (s/n): s
Retirar alface? (s/n): n
Retirar tomate? (s/n): n
Retirar cebola? (s/n): s

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 1

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 2

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 3
Adicionar outro item? (s/n): n
Gorjeta (0 para nenhuma): 5,00
Pedido registrado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 3
Número do pedido: 1

1. Acrescentar item
2. Remover item
3. Trocar item
4. Voltar
Modificação: 1

1. X-Burger: R$ 16,00
2. X-Salada: R$ 18,00
3. X-Bacon: R$ 22,00
Escolha o lanche: 1
Retirar queijo? (s/n): n
Retirar alface? (s/n): n
Retirar tomate? (s/n): n
Retirar cebola? (s/n): n

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 3
Pedido atualizado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 1
Nome do cliente: Bia

1. X-Burger: R$ 16,00
2. X-Salada: R$ 18,00
3. X-Bacon: R$ 22,00
Escolha o lanche: 2
Retirar queijo? (s/n): n
Retirar alface? (s/n): n
Retirar tomate? (s/n): n
Retirar cebola? (s/n): n

1. Cheddar: R$ 4,00
2. Bacon extra: R$ 6,00
3. Finalizar lanche sem mais adicionais
Escolha o adicional: 3
Adicionar outro item? (s/n): n
Gorjeta (0 para nenhuma): 2,00
Pedido registrado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 4
Número do pedido: 2
Pedido cancelado com sucesso.

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 2
Pedido 1 | Cliente: Ana | ATIVO
  X-Bacon sem queijo sem cebola + cheddar + bacon extra - R$ 32,00
  X-Burger - R$ 16,00
Valor do pedido: R$ 48,00
Pedido 2 | Cliente: Bia | CANCELADO
  X-Salada - R$ 18,00
Valor do pedido: R$ 18,00

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 5
Total do dia: R$ 48,00
Combinações diferentes: 2
  X-Burger
  X-Bacon sem queijo sem cebola + cheddar + bacon extra
Quantidade de gorjetas: 1
Total de gorjetas: R$ 5,00
Total do dia com gorjetas: R$ 53,00

1. Registrar novo pedido
2. Listar pedidos do dia
3. Modificar pedido
4. Cancelar pedido
5. Fechar restaurante
6. Sair
Opção: 6
Até logo!
```

## Testes

JUnit **5.10.2**, configurado no `pom.xml`. O teste provisório da preparação passou e foi removido.

| Classe de teste | Casos | Cobertura |
|---|---:|---|
| `CardapioTest` | 5 | Validações, composição, adicionais e instâncias independentes |
| `GerenciadorPedidosTest` | 2 | Numeração, busca, armazenamento e soma dos pedidos |
| `NivelTresTest` | 6 | Personalização, isolamento, remoção, troca, cancelamento e combinações |
| `UtilTest` | 2 | Moeda brasileira e validação reutilizável de texto |
| `FinanceiroTest` | 6 | Igualdade e hash de transações, duplicatas, Singleton, zero, cancelamento e validação de gorjetas |
| `MenuConsoleTest` | 1 | Nova tentativa para gorjetas inválidas e aceitação de vírgula, ponto e zero |

Resultado verificado:

```text
Tests run: 22, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Também foi verificado o fluxo no console com acréscimo, remoção, troca, cancelamento, letras em campos numéricos, opções inexistentes e fim da entrada.

## Como executar

Requisitos: **JDK 27** e **Maven**, com `java` e `mvn` disponíveis no terminal. O Java 27 corresponde a `maven.compiler.source` e `maven.compiler.target` do projeto.

```bash
git clone git@github.com:Milly56/Elastech-restaurante.git
cd Elastech-restaurante
mvn clean test
java -cp target/classes org.restaurante.app.Main
```

Para executar apenas os testes:

```bash
mvn test
```

Para compilar sem executar os testes:

```bash
mvn -DskipTests compile
```

O programa permanece no menu até a opção **6. Sair**. A opção **5. Fechar restaurante** apenas apresenta o relatório e retorna ao menu.

## Evolução por níveis

- [x] Etapa 0: projeto Maven, JUnit 5.10.2 e verificação provisória.
- [x] Nível 1: cadastro, listagem e total de pedidos.
- [x] Nível 2: interface, fábrica, composição e adicionais.
- [x] Nível 3: personalização, modificação, cancelamento, combinações e utilitários em uso.
- [x] Nível 4, **bônus**: caixa Singleton, transações e gorjetas.

## Limitações conhecidas

As combinações são comparadas pela descrição completa. `X-Bacon + cheddar + bacon extra` e `X-Bacon + bacon extra + cheddar` contam separadamente. Essa limitação é aceita pelo exercício.

`HashMap` e `HashSet` não garantem ordem de apresentação. Os dados não são persistidos: encerrar o programa descarta pedidos e gorjetas. Remover o último item mantém um pedido ativo com valor zero; cancelar é uma operação separada.

## Autora

[![Jamily](https://img.shields.io/badge/Autora-Jamily-8A2BE2?logo=github)](https://github.com/Milly56)

[Jamily no GitHub](https://github.com/Milly56)
````

## 9. Versionamento

O repositório já havia sido inicializado e publicado nas etapas anteriores. O README inicial, git init, git add, primeiro commit, branch main e configuração do remoto foram concluídos. O primeiro push HTTPS falhou por falta de credenciais. O remoto foi alterado para `git@github.com:Milly56/Elastech-restaurante.git`, conforme a preferência por SSH, e os pushes seguintes funcionaram. A inicialização não foi repetida.

Histórico executado até a conclusão do código:

```text
f5c2468 first commit
2d803ab chore: preparar projeto Maven com JUnit 5.10.2
8610fd1 feat: implementar Lanche validado e criação por escolha
017fc04 feat: implementar Pedido e total dos itens
65a38fe feat: gerenciar pedidos por identificador em HashMap
13d256b feat: implementar entrada e apresentação no console
3f12889 feat: coordenar operações na fachada
087b936 feat: iniciar sistema e controlar fluxo principal
8dbf229 feat: introduzir ItemCardapio e composição dos lanches
8ed6262 feat: definir ingredientes dos lanches padrão na fábrica
da73514 feat: encadear adicionais com Decorator no fluxo de pedidos
45835e0 test: validar domínio adicionais e isolamento dos lanches
a6ee7ba feat: personalizar ingredientes antes de aplicar adicionais
20691eb feat: modificar itens e cancelar pedidos sem incluir cancelados no total
a1d2a55 feat: apresentar combinações distintas de pedidos ativos no fechamento
ef84f8f refactor: centralizar validação e repetição de todas as leituras numéricas
3d9b8fd test: validar personalização alterações cancelamentos e combinações
9fa5ceb refactor: centralizar formatação monetária e validações de texto
686cab7 test: verificar formatação brasileira e validações reutilizáveis
77665cd feat: representar transações imutáveis com igualdade por pedido e valor
c32eafa feat: criar caixa Singleton e contabilizar gorjetas de pedidos ativos
347d31c feat: ler gorjetas e apresentar fechamento separado das vendas
6dbaf85 test: verificar Singleton transações gorjetas e leitura monetária
```

Últimos commits planejados para esta entrega:

```text
docs: reunir código completo testes e execução da entrega
docs: documentar restaurante ElasTech com execução real e bônus
```

O README é registrado por último em commit próprio.
