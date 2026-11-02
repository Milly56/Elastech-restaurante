# Conferência da entrega

O projeto reúne o cadastro de pedidos, a personalização dos lanches, a alteração e o cancelamento de pedidos. O controle de gorjetas foi incluído como bônus do exercício.

As instruções de execução e a descrição do projeto estão no [README](../README.md). O código está em `src/main/java` e os testes em `src/test/java`.

## Exemplo para conferir no terminal

1. Registre um pedido para Ana com um X-Bacon.
2. Retire o queijo e a cebola e adicione cheddar e bacon extra.
3. Finalize o pedido com uma gorjeta de R$ 5,00.
4. Use a opção de modificar pedido para acrescentar um X-Burger sem alterações.
5. Registre outro pedido para Bia com um X-Salada e gorjeta de R$ 2,00.
6. Cancele o pedido de Bia.
7. Liste os pedidos e consulte o fechamento.

O pedido de Ana deve ter estes itens:

```text
X-Bacon sem queijo sem cebola + cheddar + bacon extra - R$ 32,00
X-Burger - R$ 16,00
```

O total vendido deve ser R$ 48,00, com duas combinações diferentes e uma gorjeta de R$ 5,00. O total com gorjetas deve ser R$ 53,00. O pedido de Bia deve continuar na listagem como cancelado, sem entrar nesses totais.

## Outros pontos para verificar

- Entradas inválidas devem permitir uma nova tentativa.
- Personalizar um lanche não deve alterar outros itens ou pedidos.
- Remover ou trocar itens deve atualizar o valor do pedido.
- Pedidos cancelados não devem aceitar alterações, e seus números não devem ser reutilizados.
- Uma gorjeta repetida para o mesmo pedido e valor deve contar apenas uma vez.
- A opção de fechamento deve apresentar o relatório e retornar ao menu.

Para rodar os testes automatizados, execute `mvn test` na pasta do projeto.
