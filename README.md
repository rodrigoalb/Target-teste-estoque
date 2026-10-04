# inventory-movement

Aplicação para lançar entrada e saída de mercadoria no depósito.

O estoque inicial é carregado do arquivo `src/main/resources/estoque.json`. Cada movimentação recebe um **número identificador único**, uma **descrição** do que foi feito e devolve a **quantidade final** do produto no estoque.

Os dados ficam só em memória: ao reiniciar, o estoque volta ao conteúdo do arquivo e o histórico de movimentações é zerado.

## Regras

- `ENTRADA` soma a quantidade ao estoque do produto.
- `SAIDA` subtrai a quantidade do estoque. Se não houver estoque suficiente, a movimentação é recusada.
- O produto precisa existir no estoque (código cadastrado no arquivo).
- A quantidade deve ser no mínimo 1 e a descrição é obrigatória.
- Cada movimentação aceita recebe um número sequencial e a data/hora do lançamento.

Produtos do estoque inicial:

| Código | Produto | Estoque |
| --- | --- | --- |
| 101 | Caneta Azul | 150 |
| 102 | Caderno Universitário | 75 |
| 103 | Borracha Branca | 200 |
| 104 | Lápis Preto HB | 320 |
| 105 | Marcador de Texto Amarelo | 90 |

## Pré-requisitos

- Java 21 instalado.
- Não precisa instalar Maven: o projeto já vem com tudo que precisa para rodar.

## Como executar

Abra o terminal na pasta do projeto e rode:

```bash
.\mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8082`.

Caso já tenha o java 21 instalado no seu computador, execute esse comando para trocar a versão e executar a aplicação

```bash
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
java -version
.\mvnw.cmd clean spring-boot:run
```

## Como rodar os testes

```bash
.\mvnw.cmd test
```

## Como testar as chamadas da API

Nas chamadas `POST`, selecione o corpo como **JSON** (raw).

### Ver o estoque atual

**GET** `http://localhost:8082/api/estoque`

Resposta:

```json
[
  { "codigoProduto": 101, "descricaoProduto": "Caneta Azul", "estoque": 150 },
  { "codigoProduto": 102, "descricaoProduto": "Caderno Universitário", "estoque": 75 },
  { "codigoProduto": 103, "descricaoProduto": "Borracha Branca", "estoque": 200 },
  { "codigoProduto": 104, "descricaoProduto": "Lápis Preto HB", "estoque": 320 },
  { "codigoProduto": 105, "descricaoProduto": "Marcador de Texto Amarelo", "estoque": 90 }
]
```

### Lançar uma entrada

**POST** `http://localhost:8082/api/movimentacoes`

Corpo:

```json
{
  "codigoProduto": 101,
  "tipo": "ENTRADA",
  "descricao": "Compra de reposição",
  "quantidade": 20
}
```

Resposta (201 Created):

```json
{
  "id": 1,
  "codigoProduto": 101,
  "tipo": "ENTRADA",
  "descricao": "Compra de reposição",
  "quantidade": 20,
  "estoqueFinal": 170,
  "dataHora": "2026-10-03T22:40:12.345"
}
```

### Lançar uma saída

**POST** `http://localhost:8082/api/movimentacoes`

Corpo:

```json
{
  "codigoProduto": 101,
  "tipo": "SAIDA",
  "descricao": "Venda balcão",
  "quantidade": 5
}
```

Resposta (201 Created):

```json
{
  "id": 2,
  "codigoProduto": 101,
  "tipo": "SAIDA",
  "descricao": "Venda balcão",
  "quantidade": 5,
  "estoqueFinal": 165,
  "dataHora": "2026-10-03T22:41:03.812"
}
```

Quando a movimentação não pode ser feita, a resposta é 400 Bad Request:

```json
{ "erro": "Estoque insuficiente. Disponível: 165" }
```

```json
{ "erro": "Produto não encontrado: 999" }
```

Campos faltando ou errados (quantidade 0, descrição vazia, tipo diferente de `ENTRADA` ou `SAIDA`) também retornam 400 Bad Request.

### Ver o histórico de movimentações

**GET** `http://localhost:8082/api/movimentacoes`

Resposta (na ordem em que foram lançadas):

```json
[
  {
    "id": 1,
    "codigoProduto": 101,
    "tipo": "ENTRADA",
    "descricao": "Compra de reposição",
    "quantidade": 20,
    "estoqueFinal": 170,
    "dataHora": "2026-10-03T22:40:12.345"
  },
  {
    "id": 2,
    "codigoProduto": 101,
    "tipo": "SAIDA",
    "descricao": "Venda balcão",
    "quantidade": 5,
    "estoqueFinal": 165,
    "dataHora": "2026-10-03T22:41:03.812"
  }
]
```
