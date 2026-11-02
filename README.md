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
