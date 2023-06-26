import java.util.*;

public class Main {

    public static void main(String[] args) {
        Map<Character, List<String>> cfg = new HashMap<>();
        // cfg.put('E', Arrays.asList("E+T", "T"));
        // cfg.put('T', Arrays.asList("T*F", "F"));
        // cfg.put('F', Arrays.asList("(E)", "id"));
        cfg.put('A', Arrays.asList("Bm", "n"));
        cfg.put('B', Arrays.asList("Ag", "h"));

        System.out.println("Original CFG:");
        printCFG(cfg);

        removeIndirectLeftRecursion(cfg);
        removeDirectLeftRecursion(cfg);

        System.out.println("\nCFG without left recursion:");
        printCFG(cfg);
    }

    public static void removeDirectLeftRecursion(Map<Character, List<String>> cfg) {
        ArrayList<Character> keys = new ArrayList<>();

        for (char A : cfg.keySet())
            keys.add(A);

        for (char A : keys) {
            List<String> alpha = new ArrayList<>();
            List<String> beta = new ArrayList<>();

            for (String s : cfg.get(A)) {
                if (s.charAt(0) == A)
                    alpha.add(s.substring(1));
                else
                    beta.add(s);
            }

            if (alpha.size() == 0)
                continue;

            char B = getNewNonterminal(cfg);
            List<String> newBeta = new ArrayList<>();
            for (String s : beta)
                newBeta.add(s + B);

            cfg.put(A, new ArrayList<>(newBeta));
            List<String> newProductions = new ArrayList<>();
            for (String ty : alpha)
                newProductions.add(ty + B);

            cfg.put(B, new ArrayList<>(newProductions));
            cfg.get(B).add("epsilon");
        }

    }

    public static void removeIndirectLeftRecursion(Map<Character, List<String>> cfg) {
        List<Character> nonterminals = new ArrayList<Character>(cfg.keySet());

        for (int i = 0; i < nonterminals.size(); i++) {
            for (int j = 0; j < i; j++) {
                Character A = nonterminals.get(i);
                Character B = nonterminals.get(j);
                if (A == B)
                    continue;

                List<String> alpha = cfg.get(A);
                List<String> beta = cfg.get(B);
                List<String> alpha2 = new ArrayList<>();
                List<String> alpha1 = new ArrayList<>();
                List<String> beta1 = new ArrayList<>();
                List<String> beta2 = new ArrayList<>();

                for (String s : alpha) {
                    if (s.charAt(0) == B)
                        alpha2.add(s.substring(1));
                    else
                        beta1.add(s);
                }

                for (String s : beta) {
                    if (s.charAt(0) == A)
                        alpha1.add(s);
                }

                for (String s : beta)
                    for (String t : alpha2)
                        beta2.add(s + t);

                List<String> newProductions = new ArrayList<>();
                for (String s : beta1)
                    newProductions.add(s);

                for (String s : beta2)
                    newProductions.add(s);

                if (alpha1.size() > 0)
                    cfg.put(A, newProductions);
            }
        }
    }

    public static char getNewNonterminal(Map<Character, List<String>> cfg) {
        char c = 'A';
        while (cfg.containsKey(c))
            c++;
        return c;
    }

    public static void printCFG(Map<Character, List<String>> cfg) {
        for (char A : cfg.keySet()) {
            System.out.print(A + " -> ");
            List<String> productions = cfg.get(A);
            for (int i = 0; i < productions.size(); i++) {
                System.out.print(productions.get(i));
                if (i < productions.size() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }
}