import java.util.*;

public class TreeIsomorphism {
    private final Graph graph;
    private Boolean treeResult = null;
    private String validationMsg = null;
    private int[] centers = null;
    private String canonicalCode = null;

    public TreeIsomorphism(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("graph nao pode ser nulo");
        }
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public boolean isTree() {
        if (treeResult == null)
            computeValidation();
        return treeResult;
    }

    public String getValidationMessage() {
        if (validationMsg == null)
            computeValidation();
        return validationMsg;
    }

    private void computeValidation() {
        int V = graph.V();
        int E = graph.E();

        if (V == 0) {
            treeResult = true;
            validationMsg = "Entrada valida: arvore vazia (0 vertices).";
            return;
        }

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

        treeResult = true;
        validationMsg = String.format(
                "Entrada valida: arvore com %d vertices e %d arestas.", V, E);
    }

    public int[] getCenters() {
        if (centers != null)
            return centers;
        if (!isTree())
            throw new IllegalStateException("O grafo nao representa uma arvore valida.");

        int V = graph.V();

        if (V == 1) {
            centers = new int[] { 0 };
            return centers;
        }

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

        centers = new int[leaves.size()];
        for (int i = 0; i < leaves.size(); i++) {
            centers[i] = leaves.get(i);
        }
        Arrays.sort(centers);
        return centers;
    }

    public String getCanonicalEncoding() {
        if (canonicalCode != null)
            return canonicalCode;
        if (!isTree())
            throw new IllegalStateException("O grafo nao representa uma arvore valida.");

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

    // Codificacao canonica recursiva da subarvore enraizada em 'root'
    // com 'parent' sendo o vertice pai (para evitar voltar na arvore)
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
}
