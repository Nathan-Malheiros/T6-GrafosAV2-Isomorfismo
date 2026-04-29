public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            StdOut.println("Uso: java Main <arquivo1.txt> <arquivo2.txt>");
            throw new IllegalArgumentException(
                    "Informe dois arquivos de entrada. Ex.: java Main ../dados/arvore1.txt ../dados/arvore2.txt");
        }

        Graph tree1 = new Graph(new In(args[0]));
        Graph tree2 = new Graph(new In(args[1]));

        StdOut.println("========================================");
        StdOut.println("ARVORE 1: " + args[0]);
        StdOut.println("========================================");
        StdOut.println(tree1);

        StdOut.println("========================================");
        StdOut.println("ARVORE 2: " + args[1]);
        StdOut.println("========================================");
        StdOut.println(tree2);

        TreeIsomorphism analysis1 = new TreeIsomorphism(tree1);
        TreeIsomorphism analysis2 = new TreeIsomorphism(tree2);

        StdOut.println("--- Validacao ---");
        StdOut.println("Arvore 1: " + analysis1.getValidationMessage());
        StdOut.println("Arvore 2: " + analysis2.getValidationMessage());
        StdOut.println();

        if (!analysis1.isTree()) {
            StdOut.println("Comparacao interrompida: Arvore 1 nao e uma arvore valida.");
            return;
        }
        if (!analysis2.isTree()) {
            StdOut.println("Comparacao interrompida: Arvore 2 nao e uma arvore valida.");
            return;
        }

        StdOut.println("--- Centros ---");
        int[] centers1 = analysis1.getCenters();
        int[] centers2 = analysis2.getCenters();
        StdOut.println("Arvore 1: " + formatArray(centers1));
        StdOut.println("Arvore 2: " + formatArray(centers2));
        StdOut.println();

        StdOut.println("--- Codificacao Canonica ---");
        String code1 = analysis1.getCanonicalEncoding();
        String code2 = analysis2.getCanonicalEncoding();
        StdOut.println("Arvore 1: " + code1);
        StdOut.println("Arvore 2: " + code2);
        StdOut.println();

        StdOut.println("--- Veredito ---");
        if (code1.equals(code2)) {
            StdOut.println("As arvores SAO isomorfas.");
        } else {
            StdOut.println("As arvores NAO sao isomorfas.");
        }
    }

    private static String formatArray(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
