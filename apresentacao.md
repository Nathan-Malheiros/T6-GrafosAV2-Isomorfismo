# Apresentação — T6: Isomorfismo em Árvores

> Este arquivo contém todos os visuais, exemplos e trechos de código a serem
> mostrados na tela durante o vídeo, na ordem em que aparecem.

---

## BLOCO 1 — Nathan: O que é isomorfismo

### Slide 1.1 — Definição

**Duas árvores são isomorfas quando têm a mesma estrutura, independente dos rótulos dos vértices.**

---

### Slide 1.2 — Exemplo de árvores isomorfas

```
Árvore A                   Árvore B

      0                         5
     / \                       / \
    1   2                     7   6
       /                           \
      3                             8
```

| Propriedade | Árvore A | Árvore B |
|---|---|---|
| Vértices | 4 | 4 |
| Arestas | 3 | 3 |
| Forma | raiz → 2 filhos, um deles com 1 filho | raiz → 2 filhos, um deles com 1 filho |

**São isomorfas.** Se renomearmos: 5→0, 7→1, 6→2, 8→3, as conexões ficam idênticas.

---

### Slide 1.3 — Contra-exemplo: mesmo número de vértices, estruturas diferentes

```
Estrela (5 vértices)       Path (5 vértices)

       0                    0 — 1 — 2 — 3 — 4
      /|\ \
     1 2 3 4
```

| | Estrela | Path |
|---|---|---|
| Vértices | 5 | 5 |
| Arestas | 4 | 4 |
| Sequência de graus | [4, 1, 1, 1, 1] | [1, 2, 2, 2, 1] |

Aqui os graus já diferem. Mas existem casos onde a sequência de graus é igual e a estrutura ainda é diferente — por isso graus **não bastam**.

---

### Slide 1.4 — Validação da entrada

**Regras para ser uma árvore:**

```
1. E == V - 1       (número de arestas exato)
2. Conectado        (BFS a partir do vértice 0 alcança todos)
```

**Arquivo válido — iso-path4-a.txt:**
```
4       ← V = 4 vértices
3       ← E = 3 arestas  (4 - 1 = 3 ✓)
0 1
1 2
2 3
```

**Arquivo inválido — invalid-ciclo3.txt:**
```
3       ← V = 3 vértices
3       ← E = 3 arestas  (esperado 2, há ciclo ✗)
0 1
1 2
2 0
```

**Trecho do código — `computeValidation()` em `TreeIsomorphism.java`:**

```java
// Uma arvore tem exatamente V-1 arestas
if (E != V - 1) {
    treeResult = false;
    String reason = (E > V - 1)
            ? "numero de arestas maior que V-1, possivel ciclo"
            : "numero de arestas menor que V-1, grafo desconexo";
    validationMsg = String.format(
            "Entrada invalida: %d vertices e %d arestas (esperado %d). Motivo: %s.",
            V, E, V - 1, reason);
    return;
}

// Verificar conectividade por BFS
boolean[] visited = new boolean[V];
Queue<Integer> queue = new LinkedList<>();
queue.add(0);
visited[0] = true;
int count = 1;
while (!queue.isEmpty()) {
    int u = queue.poll();
    for (int w : graph.adj(u)) {
        if (!visited[w]) {
            visited[w] = true;
            count++;
            queue.add(w);
        }
    }
}

if (count != V) {
    treeResult = false;
    validationMsg = String.format(
            "Entrada invalida: grafo desconexo (%d de %d vertices alcancados a partir do vertice 0).",
            count, V);
    return;
}
```

---

---

## BLOCO 2 — Marcos: Centro e Codificação Canônica

### Slide 2.1 — Por que precisamos do centro

**Problema:** se enraizarmos em vértices diferentes, o mesmo grafo pode gerar códigos diferentes.

```
Path:  0 — 1 — 2 — 3

Enraizando em 0:          Enraizando em 1:
    0                         1
    |                        / \
    1                       0   2
    |                           |
    2                           3
    |
    3
```

O centro é o único ponto de enraizamento que é **invariante à rotulação**.
Toda árvore tem exatamente **1 ou 2 centros**.

---

### Slide 2.2 — Algoritmo: remoção iterativa de folhas

**Ideia:** descascar a árvore como uma cebola, retirando folhas camada por camada.

```
Exemplo — Path de 4 vértices:  0 — 1 — 2 — 3

Graus iniciais:  grau[0]=1  grau[1]=2  grau[2]=2  grau[3]=1
Folhas iniciais: {0, 3}

Rodada 1:
  Processar 0 → vizinho 1: grau[1] = 2-1 = 1 → nova folha
  Processar 3 → vizinho 2: grau[2] = 2-1 = 1 → nova folha
  Novas folhas: {1, 2}
  Processados: 2 + 2 = 4 == V → PARAR

Centros = {1, 2}   ← dois centros
```

```
Exemplo — Estrela de 5 vértices:  0 ligado a 1, 2, 3, 4

Graus iniciais:  grau[0]=4  grau[1]=1  grau[2]=1  grau[3]=1  grau[4]=1
Folhas iniciais: {1, 2, 3, 4}

Rodada 1:
  Processar 1 → vizinho 0: grau[0] = 4-1 = 3
  Processar 2 → vizinho 0: grau[0] = 3-1 = 2
  Processar 3 → vizinho 0: grau[0] = 2-1 = 1 → nova folha
  Processar 4 → vizinho 0: grau[0] = 1-1 = 0  (já foi adicionado)
  Novas folhas: {0}
  Processados: 4 + 1 = 5 == V → PARAR

Centros = {0}   ← único centro
```

**Trecho do código — `getCenters()` em `TreeIsomorphism.java`:**

```java
// Remoção iterativa de folhas para encontrar o(s) centro(s)
int[] degree = new int[V];
for (int v = 0; v < V; v++) {
    degree[v] = graph.degree(v);
}

List<Integer> leaves = new ArrayList<>();
for (int v = 0; v < V; v++) {
    if (degree[v] <= 1)
        leaves.add(v);
}

int processed = leaves.size();
while (processed < V) {
    List<Integer> newLeaves = new ArrayList<>();
    for (int u : leaves) {
        for (int w : graph.adj(u)) {
            degree[w]--;
            if (degree[w] == 1) {
                newLeaves.add(w);
            }
        }
    }
    processed += newLeaves.size();
    leaves = newLeaves;
}
```

---

### Slide 2.3 — Codificação canônica: a regra

```
encode(nó):
  se nó é folha  →  retorna "()"
  senão:
    1. calcular encode(filho) para cada filho
    2. ORDENAR os códigos dos filhos lexicograficamente
    3. concatenar os códigos ordenados
    4. retornar "(" + concatenação + ")"
```

---

### Slide 2.4 — Exemplo passo a passo

```
Árvore:
      r
     / \
    a   b
       /
      c
```

```
encode(a):   a é folha  →  "()"
encode(c):   c é folha  →  "()"
encode(b):   filhos = [c]  →  códigos = ["()"]
             ordenados = ["()"]
             resultado = "(" + "()" + ")" = "(())"

encode(r):   filhos = [a, b]  →  códigos = ["()", "(())"]
             ordenar:  "(())" < "()"  ?
               comparar char a char: '(' == '(', depois '(' vs ')'
               ASCII '(' = 40, ASCII ')' = 41  →  '(' < ')'
               portanto "(())" vem antes de "()"
             ordenados = ["(())", "()"]
             resultado = "(" + "(())" + "()" + ")" = "((())())"
```

**Código canônico final: `((())())`**

---

### Slide 2.5 — Por que a ordenação é necessária

```
Mesmo grafo, arestas em ordens diferentes no arquivo:

Arquivo A:              Arquivo B:
  r — a  (lida 1ª)       r — b  (lida 1ª)
  r — b  (lida 2ª)       r — a  (lida 2ª)

Sem ordenação:           Com ordenação:
  filhos de r = [a, b]     filhos de r = [a, b]
  encode(a) = "()"         encode(a) = "()"
  encode(b) = "(())"       encode(b) = "(())"
  concat = "()(())"        sort → ["(())", "()"]
  código = "(()(()))"      concat = "(())()"
                           código = "((())())"  ← sempre igual ✓
```

A ordenação garante que o código é **independente da ordem de inserção das arestas**.

**Trecho do código — `encode()` em `TreeIsomorphism.java`:**

```java
private String encode(int root, int parent) {
    List<String> childCodes = new ArrayList<>();
    for (int child : graph.adj(root)) {
        if (child != parent) {
            childCodes.add(encode(child, root));
        }
    }
    Collections.sort(childCodes); // ordenacao lexicografica dos filhos
    StringBuilder sb = new StringBuilder("(");
    for (String code : childCodes)
        sb.append(code);
    sb.append(")");
    return sb.toString();
}
```

---

### Slide 2.6 — Caso de dois centros

```java
public String getCanonicalEncoding() {
    int[] c = getCenters();
    if (c.length == 1) {
        // Unico centro: enraizar nele
        canonicalCode = encode(c[0], -1);
    } else {
        // Dois centros: calcular ambas as codificacoes e tomar a menor
        // lexicograficamente, garantindo independencia da rotulacao
        String code1 = encode(c[0], -1);
        String code2 = encode(c[1], -1);
        canonicalCode = (code1.compareTo(code2) <= 0) ? code1 : code2;
    }
    return canonicalCode;
}
```

Se há dois centros, calculamos as duas codificações e usamos a **menor lexicograficamente**. Duas árvores isomorfas com dois centros vão produzir os mesmos dois códigos, então a escolha do mínimo é consistente.

---

---

## BLOCO 3 — Julia: Código, execução e saída

### Slide 3.1 — Estrutura dos arquivos

```
T6/
├── src/
│   ├── Main.java              ← criado por nós (ponto de entrada)
│   ├── TreeIsomorphism.java   ← criado por nós (toda a lógica)
│   │
│   ├── Graph.java             ← algs4 original, NÃO modificado
│   ├── In.java                ← algs4 original, NÃO modificado
│   ├── StdOut.java            ← algs4 original, NÃO modificado
│   ├── Bag.java               ← algs4 original, NÃO modificado
│   └── Stack.java             ← algs4 original, NÃO modificado
│
└── dados/
    ├── iso-path4-a.txt
    ├── iso-path4-b.txt
    ├── unico-centro-a.txt
    ├── unico-centro-b.txt
    ├── nao-iso-estrela5.txt
    ├── nao-iso-path5.txt
    └── invalid-ciclo3.txt
```

---

### Slide 3.2 — Como Main.java lê as entradas

```java
// Lê dois caminhos de arquivo pela linha de comando
Graph tree1 = new Graph(new In(args[0]));
Graph tree2 = new Graph(new In(args[1]));
```

- `In` é a classe do algs4 que lê o arquivo no formato padrão.
- `Graph` é a classe do algs4 que constrói a lista de adjacência.
- `TreeIsomorphism` recebe o `Graph` e **só lê** — nunca modifica.

```java
TreeIsomorphism analysis1 = new TreeIsomorphism(tree1);
TreeIsomorphism analysis2 = new TreeIsomorphism(tree2);
```

---

### Slide 3.3 — Fluxo completo do programa

```
args[0]  ──►  In  ──►  Graph  ──►  TreeIsomorphism
                                    ├── isTree()
                                    ├── getCenters()
                                    └── getCanonicalEncoding()

args[1]  ──►  In  ──►  Graph  ──►  TreeIsomorphism
                                    ├── isTree()
                                    ├── getCenters()
                                    └── getCanonicalEncoding()

                          code1.equals(code2) ?
                          ├── sim  →  "SAO isomorfas"
                          └── não  →  "NAO sao isomorfas"
```

---

### Slide 3.4 — Demo 1: árvores isomorfas (path de 4 vértices)

**Comando a executar:**
```bash
java -cp bin Main dados\iso-path4-a.txt dados\iso-path4-b.txt
```

**Saída esperada:**
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

**O que apontar na saída:**
- As listas de adjacência são diferentes (arestas em ordens distintas).
- Mesmo assim, os códigos canônicos são **idênticos**: `((())())`.
- Isso prova que a ordenação dos filhos funcionou corretamente.

---

### Slide 3.5 — Demo 2: árvores não isomorfas (estrela vs path)

**Comando a executar:**
```bash
java -cp bin Main dados\nao-iso-estrela5.txt dados\nao-iso-path5.txt
```

**Saída esperada:**
```
--- Centros ---
Arvore 1: [0]
Arvore 2: [2]

--- Codificacao Canonica ---
Arvore 1: (()()()())
Arvore 2: ((())(()))

--- Veredito ---
As arvores NAO sao isomorfas.
```

**O que apontar na saída:**
- A estrela tem único centro (o hub central) e código com 4 filhos folha: `(()()()())`.
- O path tem dois centros (os dois vértices do meio) e estrutura aninhada: `((())(()))`.
- Os códigos diferem → **não isomorfas**.

---

### Slide 3.6 — Demo 3: entrada inválida (ciclo)

**Comando a executar:**
```bash
java -cp bin Main dados\invalid-ciclo3.txt dados\iso-path4-a.txt
```

**Saída esperada:**
```
--- Validacao ---
Arvore 1: Entrada invalida: 3 vertices e 3 arestas (esperado 2). Motivo: numero de arestas maior que V-1, possivel ciclo.
Arvore 2: Entrada valida: arvore com 4 vertices e 3 arestas.

Comparacao interrompida: Arvore 1 nao e uma arvore valida.
```

**O que apontar na saída:**
- O programa detectou E = 3 > V−1 = 2 e concluiu que há ciclo.
- Não tentou codificar nada.
- A mensagem diz exatamente o motivo.

---

### Slide 3.7 — Demo bonus: duas árvores isomorfas com estrutura diferente (unico-centro)

**Comando a executar:**
```bash
java -cp bin Main dados\unico-centro-a.txt dados\unico-centro-b.txt
```

**Saída esperada:**
```
--- Centros ---
Arvore 1: [2]
Arvore 2: [2]

--- Codificacao Canonica ---
Arvore 1: ((()())(())())
Arvore 2: ((()())(())())

--- Veredito ---
As arvores SAO isomorfas.
```

**O que apontar:**
- As listas de adjacência são completamente diferentes.
- Mesmo assim, ambas geram `((()())(())())`.
- Esse é o caso mais forte: duas árvores que parecem diferentes, mas são isomorfas.
