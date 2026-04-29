# Roteiro do Vídeo — T6: Isomorfismo em Árvores
**Duração alvo: 4 minutos e 40 segundos**
**Apresentadores: Nathan, Marcos e Julia**

---

## Visão geral do roteiro

| Apresentador | Tempo estimado | Tópicos |
|---|---|---|
| **Nathan** | 0:00 – 1:35 | Abertura, o que é isomorfismo, por que graus não bastam, validação da entrada |
| **Marcos** | 1:35 – 3:05 | Centro de uma árvore, codificação canônica, por que ordenar os filhos |
| **Julia** | 3:05 – 4:40 | Como o código lê as entradas, estrutura da solução, execução ao vivo, interpretação da saída, encerramento |

---

---

# PARTE 1 — NATHAN (0:00 até ~1:35)

## O que Nathan precisa entender antes de falar

### Conceito: isomorfismo em árvores

Duas árvores são **isomorfas** quando existe uma forma de renomear os vértices de uma delas de modo que a estrutura de conexões fique idêntica à outra. Em outras palavras: a *forma* das duas árvores é a mesma, só os *nomes* dos vértices são diferentes.

**Exemplo para fixar:**
```
Árvore A:          Árvore B:

    0                  5
   / \                / \
  1   2              7   6
     /                    \
    3                      8
```
Ambas têm a mesma forma: raiz com dois filhos, sendo um folha e o outro com um filho folha. São isomorfas, mesmo com vértices renomeados.

**O que NÃO é isomorfismo:**
- Não é comparar se os dois grafos têm o mesmo número de vértices (ambos podem ter 4 vértices e ser completamente diferentes).
- Não é comparar a sequência de graus (ex.: `[1, 1, 2, 2]` não garante mesma estrutura).
- Não é comparar "visualmente" se parecem iguais no papel.

---

### Conceito: por que comparar graus não basta

Considere estas duas árvores, ambas com 5 vértices:

```
Estrela:           Path (caminho):

    0                  0
   /|\\ \               |
  1 2 3 4              1
                       |
                       2
                       |
                       3
                       |
                       4
```

**Sequência de graus da estrela:** `[4, 1, 1, 1, 1]`
**Sequência de graus do path:** `[1, 2, 2, 2, 1]`

Nesse caso os graus já diferem, ótimo. Mas existem casos onde a sequência de graus é **igual** e as árvores ainda assim são diferentes estruturalmente. A sequência de graus é necessária mas não suficiente.

---

### Conceito: validação da entrada

Antes de qualquer análise, o programa verifica se o grafo lido realmente é uma árvore. Um grafo é uma árvore se e somente se:
1. Tem exatamente **V − 1 arestas** (onde V é o número de vértices).
2. É **conectado** (todos os vértices alcançáveis a partir de qualquer um).

Se E > V − 1: há ciclo.
Se E < V − 1: o grafo é desconexo.
A conectividade é verificada por **BFS** a partir do vértice 0.

**Exemplo de entrada inválida (ciclo com 3 vértices e 3 arestas):**
```
3    ← V = 3
3    ← E = 3 (mas uma árvore teria E = V-1 = 2)
0 1
1 2
2 0
```
O programa detecta isso e interrompe a comparação com uma mensagem clara.

---

## Fala do Nathan (leia com naturalidade, não decorado)

> *[0:00]* — Olá, somos Nathan, Marcos e Julia, e nesse vídeo vamos explicar o Trabalho Prático 6 de Grafos: **isomorfismo em árvores** usando codificação canônica.
>
> *[0:08]* — Começando pela pergunta central: **o que significa duas árvores serem isomorfas?**
> Duas árvores são isomorfas quando têm a mesma estrutura de conexões, independentemente de como os vértices estão rotulados. Olha esse exemplo: temos uma árvore com raiz 0, dois filhos, sendo que o filho da direita tem mais um descendente. Na segunda árvore, os vértices têm outros números, mas a forma é exatamente a mesma — raiz com dois galhos, um curto e um com mais um nível. Essas duas são isomorfas.
>
> *[0:35]* — A questão importante é: **por que não basta comparar os graus dos vértices ou a aparência do desenho?**
> Comparar graus é uma heurística fraca. Dois grafos podem ter a mesma sequência de graus e ainda assim serem estruturalmente diferentes. E comparar "aparência" visual não é um algoritmo — não é reproduzível, não é correto para casos gerais. Precisamos de um método formal.
>
> *[1:00]* — Antes de qualquer análise estrutural, o programa **valida se cada entrada é de fato uma árvore**. Um grafo é árvore se tem exatamente V menos 1 arestas e é conectado. Se o número de arestas for maior que V menos 1, há um ciclo. Se for menor, o grafo está desconexo. A conectividade é verificada com uma BFS a partir do vértice zero. Se qualquer uma das entradas falhar nessa validação, o programa avisa o motivo e encerra — sem tentar comparar algo inválido.
>
> *[1:30]* — Com isso, passo para o Marcos, que vai explicar como encontramos o centro da árvore e como funciona a codificação canônica.

---

---

# PARTE 2 — MARCOS (1:35 até ~3:05)

## O que Marcos precisa entender antes de falar

### Conceito: centro de uma árvore

O **centro** de uma árvore é o vértice (ou os dois vértices) que minimiza a maior distância até qualquer folha. Em termos práticos, é o "meio" da árvore.

**Por que precisamos do centro?**
Se enraizarmos a árvore em um vértice qualquer, a codificação que gerarmos vai depender de qual vértice escolhemos como raiz. Isso é ruim: duas árvores isomorfas poderiam gerar códigos diferentes se enraizadas em pontos diferentes. O centro é a única escolha de raiz que é **invariante à rotulação** — toda árvore tem 1 ou 2 centros, e isso não depende dos nomes dos vértices.

**Como encontrar o centro — remoção iterativa de folhas:**

Imagine descascar a árvore como uma cebola, removendo as folhas camada por camada:

1. Calcule o grau de cada vértice.
2. Coloque em uma lista `L` todos os vértices com grau 0 ou 1 (as folhas).
3. Enquanto ainda houver vértices não processados:
   - Para cada folha em `L`, reduza o grau de seus vizinhos.
   - Se um vizinho ficar com grau 1, ele é a nova folha.
   - Substitua `L` pelas novas folhas.
4. Os vértices que restaram **na última rodada** são os centros (1 ou 2).

**Exemplo com um path de 4 vértices: 0 — 1 — 2 — 3**

- Rodada 1: folhas = {0, 3}. Seus vizinhos (1 e 2) perdem um grau cada.
- Agora grau(1) = 1 e grau(2) = 1 → nova lista = {1, 2}.
- Processados = 4 = V → paramos. Centros = **{1, 2}** (dois centros).

**Exemplo com uma estrela de 5 vértices: 0 ligado a 1, 2, 3, 4**

- Rodada 1: folhas = {1, 2, 3, 4}. Todos diminuem o grau de 0.
- grau(0) vai de 4 para 0. Mas grau(0) nunca chega a 1 nessa rodada.
- Processados = 4. Faltam 1 (o vértice 0). Nova lista = {} ... wait.
  - Na verdade: cada folha {1,2,3,4} reduz grau(0) por 1 de cada vez.
  - Após processar {1}: grau(0) = 3. Após {2}: grau(0) = 2. Após {3}: grau(0) = 1 → 0 entra na nova lista.
  - Nova lista = {0}. Processados = 5 = V. Centros = **{0}** (único centro).

---

### Conceito: codificação canônica

Com o centro escolhido como raiz, percorremos a árvore recursivamente e geramos um código de texto:

- **Folha** → código é `"()"`
- **Nó interno** → calcula o código de cada filho, **ordena esses códigos lexicograficamente**, concatena e envolve com parênteses: `"(" + códigos_ordenados + ")"`

**Por que ordenar?** Porque os filhos na lista de adjacência podem aparecer em ordens diferentes dependendo de como o arquivo foi lido. Dois arquivos com a mesma árvore mas arestas em ordens diferentes gerariam listas de filhos em ordens diferentes. Ao ordenar os códigos dos filhos antes de concatenar, garantimos que a codificação final é sempre a mesma independentemente da ordem de leitura.

**Exemplo:**
```
    r
   / \
  a   b
     /
    c
```
- `encode(a)` = `()` (folha)
- `encode(c)` = `()` (folha)
- `encode(b)` = `(())` (b tem filho c que é folha)
- Filhos de r: códigos são `["()", "(())"]` → ordenados: `["()", "(())"]`
  - Concatenados: `()(())`
  - Código de r: `(()(()))` ← note: `"(" + "()" + "(())" + ")"` = `"(()(()))"`

Ops, para clareza: `"(" + "()" + "(())" + ")"` = `"(()(()))"`

Mas o enunciado mostra `((())())` — isso porque a ordenação lexicográfica coloca `"(())"` antes de `"()"`:
- `"(())" < "()"` lexicograficamente? Compare char a char: `(` == `(`, depois `(` vs `)`. `(` tem ASCII 40, `)` tem ASCII 41. Portanto `(())"` < `"()"`. Então a ordem é `["(())", "()"]`, concatenando: `(())()`, e o código de r fica `((())())`. Isso bate com o enunciado. ✓

**Caso de dois centros:**
Calcula-se a codificação enraizando em c[0] e em c[1]. Toma-se a **menor lexicograficamente** das duas. Isso garante que duas árvores isomorfas com dois centros sempre gerem o mesmo código.

---

## Fala do Marcos (leia com naturalidade, não decorado)

> *[1:35]* — Obrigado, Nathan. Agora vou explicar o núcleo do algoritmo: primeiro como encontramos o **centro** da árvore, e depois como geramos a **codificação canônica**.
>
> *[1:42]* — O centro existe porque se enraizarmos a árvore em um vértice qualquer, o código que gerarmos vai depender dessa escolha. Para que duas árvores isomorfas sempre produzam o mesmo código, precisamos de uma raiz que não dependa dos rótulos. O centro é exatamente isso.
>
> *[1:55]* — Encontramos o centro removendo folhas iterativamente, como descascar uma cebola. A gente começa identificando todos os vértices com grau 1 — as folhas externas — e os "remove" da árvore. Os vizinhos dessas folhas perdem um grau. Quando algum vizinho fica com grau 1, ele vira a nova folha. Repetimos esse processo até restar 1 ou 2 vértices: esses são os centros. Uma árvore sempre tem exatamente 1 ou 2 centros.
>
> *[2:15]* — Com o centro como raiz, calculamos a **codificação canônica** recursivamente. A regra é simples: uma folha recebe o código `()`. Um nó interno calcula o código de cada filho, **ordena esses códigos em ordem lexicográfica**, concatena tudo e envolve com parênteses. Isso gera uma string que representa unicamente a estrutura da subárvore.
>
> *[2:38]* — E por que a ordenação é essencial? Porque a ordem em que os filhos aparecem na lista de adjacência depende da ordem em que as arestas foram escritas no arquivo. Dois arquivos representando a mesma árvore podem ter os filhos em ordens diferentes. Se não ordenarmos, duas árvores isomorfas podem gerar códigos diferentes. A ordenação lexicográfica dos códigos dos filhos elimina essa ambiguidade.
>
> *[2:58]* — Quando a árvore tem dois centros, calculamos a codificação enraizando em cada um dos dois e ficamos com a menor lexicograficamente. Isso garante um resultado consistente. Passo para a Julia, que vai mostrar o código e a execução.

---

---

# PARTE 3 — JULIA (3:05 até ~4:40)

## O que Julia precisa entender antes de falar

### Como o programa lê as entradas

O ponto de entrada é `Main.java`. O programa recebe dois caminhos de arquivo pela linha de comando:

```bash
java Main dados/iso-path4-a.txt dados/iso-path4-b.txt
```

Para cada arquivo, instancia um objeto `Graph` da classe `Graph.java` do algs4, passando um objeto `In` (também do algs4):

```java
Graph tree1 = new Graph(new In(args[0]));
Graph tree2 = new Graph(new In(args[1]));
```

O formato algs4 é:
```
4       ← número de vértices V
3       ← número de arestas E
0 1     ← aresta entre 0 e 1
1 2     ← aresta entre 1 e 2
2 3     ← aresta entre 2 e 3
```

`Graph.java` e `In.java` são os arquivos **originais do algs4** — não foram modificados. O trabalho cria apenas dois arquivos novos: `TreeIsomorphism.java` (a lógica) e `Main.java` (o ponto de entrada).

---

### Estrutura principal da solução

```
Main.java
  └── cria Graph (algs4) para cada arquivo
  └── cria TreeIsomorphism para cada Graph
        ├── isTree()             → valida se é árvore (E == V-1 e conectado)
        ├── getCenters()         → remoção iterativa de folhas
        ├── getCanonicalEncoding() → encode() recursivo com ordenação
  └── compara os dois códigos canônicos
  └── imprime veredito
```

`TreeIsomorphism` **nunca modifica** o `Graph`. Ele apenas lê o grafo (via `graph.V()`, `graph.E()`, `graph.adj(v)`, `graph.degree(v)`) e faz todos os cálculos internamente. A interferência no algs4 é zero.

---

### A execução e a saída

**Execução com dois arquivos isomorfos (path de 4 vértices):**

```
========================================
ARVORE 1: dados\iso-path4-a.txt
========================================
4 vertices, 3 edges
0: 1
1: 2 0
2: 3 1
3: 2

========================================
ARVORE 2: dados\iso-path4-b.txt
========================================
4 vertices, 3 edges
0: 2
1: 3 2
2: 1 0
3: 1

--- Validacao ---
Arvore 1: Entrada valida: arvore com 4 vertices e 3 arestas.
Arvore 2: Entrada valida: arvore com 4 vertices e 3 arestas.

--- Centros ---
Arvore 1: [1, 2]
Arvore 2: [1, 2]

--- Codificacao Canonica ---
Arvore 1: ((())())
Arvore 2: ((())())

--- Veredito ---
As arvores SAO isomorfas.
```

**Interpretação:**
- Os dois grafos têm 4 vértices e 3 arestas → estrutura de árvore confirmada.
- Ambos têm dois centros (vértices 1 e 2) → são paths, o meio de um path de 4 são sempre os dois vértices centrais.
- Os dois geram o mesmo código `((())())` → estruturalmente idênticos → **isomorfas**.

**Execução com entrada inválida (ciclo):**
```
--- Validacao ---
Arvore 1: Entrada invalida: 3 vertices e 3 arestas (esperado 2). Motivo: numero de arestas maior que V-1, possivel ciclo.

Comparacao interrompida: Arvore 1 nao e uma arvore valida.
```

**Execução com árvores não isomorfas:**
```
--- Codificacao Canonica ---
Arvore 1: (()()()())    ← estrela: centro único com 4 folhas
Arvore 2: ((())(()))    ← path de 5: dois centros, estrutura diferente

--- Veredito ---
As arvores NAO sao isomorfas.
```

---

## Fala do Julia (leia com naturalidade, não decorado)

> *[3:05]* — Obrigado, Marcos. Agora vou mostrar como o programa é estruturado e o que acontece quando a gente executa.
>
> *[3:10]* — O programa tem dois arquivos criados por nós: `Main.java` e `TreeIsomorphism.java`. Os demais arquivos — `Graph.java`, `In.java`, `StdOut.java` e os outros — são os originais do algs4, sem nenhuma modificação. Isso atende à exigência do trabalho de usar o `Graph.java` como estrutura principal sem alterar a base.
>
> *[3:28]* — No `Main`, primeiro lemos os dois arquivos de entrada pela linha de comando e construímos dois objetos `Graph` com `new Graph(new In(args[0]))`. Depois criamos um `TreeIsomorphism` para cada um, que é a nossa classe com toda a lógica. Ela só lê o grafo — nunca modifica nada.
>
> *[3:45]* — Vou mostrar a execução. [**compartilhe a tela aqui**] Rodando `java Main dados/iso-path4-a.txt dados/iso-path4-b.txt`. O programa imprime a lista de adjacência das duas árvores, depois a validação — ambas válidas —, depois os centros — vértices 1 e 2 nas duas —, e por fim as codificações canônicas: as duas geram `((())())`. Veredito: **isomorfas**.
>
> *[4:08]* — Agora com árvores diferentes: estrela e path de 5 vértices. [**execute o segundo caso**] A estrela gera `(()()()())` e o path gera `((())(()))`. Os códigos são diferentes, então o veredito é: **não isomorfas**. O código canônico deixou visível exatamente onde a estrutura difere.
>
> *[4:22]* — E com uma entrada inválida: um ciclo de 3 vértices. [**execute o terceiro caso**] O programa detecta que E é maior que V menos 1, informa que há um possível ciclo e interrompe a comparação sem tentar codificar nada.
>
> *[4:33]* — Para resumir: o programa valida, encontra os centros, gera a codificação canônica e compara. A comparação é estruturalmente correta, independente da ordem das arestas ou dos rótulos dos vértices. Obrigada.

---

---

# Resumo da divisão e cronômetro

```
[0:00 - 0:08]  Nathan  → Abertura / apresentação do tema
[0:08 - 0:35]  Nathan  → O que é isomorfismo (com exemplo)
[0:35 - 1:00]  Nathan  → Por que graus e aparência não bastam
[1:00 - 1:30]  Nathan  → Validação da entrada (E == V-1, BFS)
[1:30 - 1:35]  Nathan  → Passagem para Marcos

[1:35 - 1:55]  Marcos  → Por que precisamos do centro
[1:55 - 2:15]  Marcos  → Algoritmo de remoção iterativa de folhas
[2:15 - 2:38]  Marcos  → Codificação canônica recursiva
[2:38 - 2:58]  Marcos  → Por que a ordenação dos filhos é necessária
[2:58 - 3:05]  Marcos  → Caso de dois centros + passagem para Julia

[3:05 - 3:28]  Julia   → Estrutura dos arquivos / sem modificar algs4
[3:28 - 3:45]  Julia   → Como Main.java lê as entradas
[3:45 - 4:08]  Julia   → Demo: caso isomorfo (path4)
[4:08 - 4:22]  Julia   → Demo: caso não isomorfo (estrela vs path5)
[4:22 - 4:33]  Julia   → Demo: entrada inválida (ciclo)
[4:33 - 4:40]  Julia   → Encerramento
```

---

# Dicas de gravação

- Cada um grave sua parte separadamente e depois editem juntos, ou gravem em sequência sem cortes.
- Julia deve deixar o terminal **já aberto** com os comandos prontos antes de começar a falar, para não perder tempo digitando ao vivo.
- Os três devem falar em tom conversacional, não robotizado — leram o conteúdo, entenderam, estão explicando.
- Não precisam decorar: podem ter um resumo em papel ou na segunda tela como cola.
- Testem o tempo com um cronômetro antes da gravação final.
