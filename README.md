# T6 - Isomorfismo em Árvores

Trabalho Prático 6 da disciplina **Resolução de Problemas com Grafos**  
Orientador: Prof. Me Ricardo Carubbi — Universidade de Fortaleza (UNIFOR)

## Vídeo Explicativo

Link do vídeo: [Link do video](https://youtu.be/-nECmwNCeMc)


## Descrição

O programa lê duas árvores não direcionadas no formato `algs4` e determina se elas são **isomorfas** usando o algoritmo de **codificação canônica**. O processo segue as etapas:

1. Validação da entrada (verifica se cada grafo é realmente uma árvore)
2. Localização do(s) centro(s) por remoção iterativa de folhas
3. Enraizamento da árvore no centro
4. Codificação canônica recursiva com ordenação lexicográfica dos filhos
5. Comparação das codificações para o veredito final

## Estrutura do Projeto

```text
T6/
├── README.md
├── T6.md
├── bin/                         # .class gerados pela compilação
├── dados/
│   ├── invalid-ciclo3.txt      # grafo inválido (ciclo)
│   ├── iso-path4-a.txt         # caminho de 4 vértices (isomorfo)
│   ├── iso-path4-b.txt         # caminho de 4 vértices (isomorfo)
│   ├── nao-iso-estrela5.txt    # estrela com 5 vértices
│   ├── nao-iso-path5.txt       # caminho com 5 vértices
│   ├── unico-centro-a.txt      # árvore com centro único
│   └── unico-centro-b.txt      # árvore com centro único (isomorfa à anterior)
├── imgs/
└── src/
    ├── Bag.java                 # estrutura auxiliar (algs4)
    ├── Graph.java               # representação principal do grafo (algs4)
    ├── In.java                  # leitura de arquivos (algs4)
    ├── Main.java                # ponto de entrada
    ├── Stack.java               # estrutura auxiliar (algs4)
    ├── StdIn.java               # entrada padrão (algs4)
    ├── StdOut.java              # saída padrão (algs4)
    └── TreeIsomorphism.java     # lógica de validação, centros e codificação canônica
```

## Compilação

Na raiz do projeto (`T6/`), execute:

```bash
javac -d bin src/*.java
```

Os arquivos `.class` serão gerados em `bin/`, separados dos fontes em `src/`.

## Execução

A partir da raiz do projeto (`T6/`):

```bash
java -cp bin Main <arquivo1.txt> <arquivo2.txt>
```

### Casos de teste

```bash
# Árvores isomorfas (dois caminhos de 4 vértices com rotulações diferentes)
java -cp bin Main dados/iso-path4-a.txt dados/iso-path4-b.txt

# Árvores não isomorfas (estrela vs caminho, 5 vértices)
java -cp bin Main dados/nao-iso-estrela5.txt dados/nao-iso-path5.txt

# Árvores isomorfas com centro único
java -cp bin Main dados/unico-centro-a.txt dados/unico-centro-b.txt

# Entrada inválida (ciclo) — comparação interrompida
java -cp bin Main dados/invalid-ciclo3.txt dados/iso-path4-a.txt
```

### Exemplo de saída

```
========================================
ARVORE 1: ../dados/iso-path4-a.txt
========================================
4 vertices, 3 edges
0: 1
1: 2 0
2: 3 1
3: 2

========================================
ARVORE 2: ../dados/iso-path4-b.txt
========================================
4 vertices, 3 edges
0: 2
1: 2
2: 3 1 0
3: 2

--- Validacao ---
Arvore 1: Entrada valida: arvore com 4 vertices e 3 arestas.
Arvore 2: Entrada valida: arvore com 4 vertices e 3 arestas.

--- Centros ---
Arvore 1: [1, 2]
Arvore 2: [2, 3]

--- Codificacao Canonica ---
Arvore 1: ((())())
Arvore 2: ((())())

--- Veredito ---
As arvores SAO isomorfas.
```

## Formato de Entrada

Padrão `algs4` para grafos não direcionados:

```
V
E
v1 w1
v2 w2
...
```

onde `V` é o número de vértices, `E` o número de arestas, e cada linha `v w` representa uma aresta. Vértices indexados de `0` a `V-1`.

