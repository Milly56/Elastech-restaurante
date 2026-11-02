# Restaurante ElasTech

Sistema de pedidos de restaurante feito em Java para o bootcamp ElasTech, com Soulcode e PagBank. A aplicação funciona pelo terminal e permite montar lanches, gerenciar pedidos e consultar o caixa do dia.

## Funcionalidades

- Cadastrar e listar pedidos por cliente.
- Personalizar lanches, retirando ingredientes e incluindo adicionais.
- Adicionar, remover e trocar itens de um pedido.
- Cancelar pedidos.
- Consultar o total vendido e as combinações de lanches pedidas.
- Registrar gorjetas e consultar o total do caixa.

## Cardápio

| Lanche | Preço | Ingredientes |
|---|---:|---|
| X-Burger | R$ 16,00 | Pão, hambúrguer, queijo, alface e tomate |
| X-Salada | R$ 18,00 | Pão, hambúrguer, queijo, alface, tomate e cebola |
| X-Bacon | R$ 22,00 | Pão, hambúrguer, queijo, bacon, alface, tomate e cebola |

Os adicionais são cheddar (R$ 4,00) e bacon extra (R$ 6,00). Retirar ingredientes não muda o preço do lanche.

Por exemplo, um X-Bacon com cheddar e bacon extra custa R$ 32,00.

## Como executar

O projeto usa Java 27 e Maven. Com os dois instalados, execute na pasta do projeto:

```bash
mvn compile
java -cp target/classes org.restaurante.app.Main
```

O menu permite registrar, listar, modificar e cancelar pedidos. A opção **Fechar restaurante** mostra o relatório do dia e volta ao menu. Para encerrar o programa, escolha **Sair**.

Ao informar uma gorjeta, use vírgula ou ponto decimal, com até duas casas. Digite `0` se não houver gorjeta.

## Organização do código

As classes estão no pacote `org.restaurante`, divididas em:

- `app`: menu do terminal e coordenação das operações.
- `cardapio`: lanches, ingredientes e adicionais.
- `pedido`: cadastro de pedidos, cancelamento e cálculo das vendas.
- `financeiro`: registro de gorjetas e relatório do caixa.
- `util`: validação de texto e formatação de valores em reais.

O exercício usa Factory para criar os lanches, Decorator para os adicionais, Facade para coordenar as operações e Singleton para o caixa. Os valores monetários usam `double`.

## Testes

Os testes usam JUnit 5 e ficam em `src/test/java`. Eles verificam preços, personalização, alterações e cancelamentos de pedidos, gorjetas e validação de entradas.

Para executá-los:

```bash
mvn test
```

Há um [roteiro de conferência da entrega](docs/ENTREGA.md) com um exemplo de pedido e os principais comportamentos a verificar.

## Limitações

Os pedidos e as gorjetas ficam apenas em memória e são perdidos ao encerrar o programa. Pedidos cancelados continuam na listagem, mas não entram nos totais do fechamento.

As combinações de lanches são comparadas pela descrição. Por isso, adicionar cheddar e depois bacon extra conta como uma combinação diferente de adicionar bacon extra e depois cheddar.

A ordem de exibição dos pedidos e das combinações pode variar. Remover todos os itens deixa o pedido ativo com valor zero; para cancelá-lo, é preciso usar a opção de cancelamento.

## Autora

[Jamily](https://github.com/Milly56)
