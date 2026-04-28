import java.util.*;

public class DrawGraph {
    static int n;
    static boolean[][] adj;
    static int[][]   weight;
    static List<int[]> cycles = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();
        int m = sc.nextInt();

        adj    = new boolean[n][n];
        weight = new int[n][n];

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt(), v = sc.nextInt(), w = sc.nextInt();
            adj[u][v]    = true;
            weight[u][v] = w;
        }

        Set<String> seen = new LinkedHashSet<>();

        for (int a = 0; a < n; a++)
            for (int b = 0; b < n; b++) {
                if (b == a || !adj[a][b]) continue;
                for (int c = 0; c < n; c++) {
                    if (c == a || c == b || !adj[b][c]) continue;
                    for (int d = 0; d < n; d++) {
                        if (d == a || d == b || d == c) continue;
                        if (!adj[c][d] || !adj[d][a])  continue;

                        int[] cycle = {a, b, c, d};
                        int[] canonical = canonicalize(cycle);
                        String key = Arrays.toString(canonical);
                        seen.add(key);
                        cycles.add(new int[]{canonical[0], canonical[1],
                                canonical[2], canonical[3]});
                    }
                }
            }

        Map<String, int[]> unique = new LinkedHashMap<>();
        for (int[] c : cycles) {
            String key = Arrays.toString(c);
            unique.put(key, c);
        }

        if (unique.isEmpty()) {
            System.out.println("No cycles of length 4 found.");
            return;
        }

        System.out.println("Cycles of length 4 (vertex sequence and edge weights):");
        int idx = 1;
        for (int[] c : unique.values()) {
            int a = c[0], b = c[1], cc = c[2], d = c[3];
            int totalWeight = weight[a][b] + weight[b][cc] + weight[cc][d] + weight[d][a];
            System.out.printf("  Cycle %d: %d -(%d)-> %d -(%d)-> %d -(%d)-> %d -(%d)-> %d   [total weight = %d]%n",
                    idx++,
                    a, weight[a][b],
                    b, weight[b][cc],
                    cc, weight[cc][d],
                    d, weight[d][a],
                    a,
                    totalWeight);
        }
        System.out.println("Total cycles found: " + unique.size());
    }

    private static int[] canonicalize(int[] cycle) {
        int minIdx = 0;
        for (int i = 1; i < cycle.length; i++)
            if (cycle[i] < cycle[minIdx]) minIdx = i;
        int[] result = new int[cycle.length];
        for (int i = 0; i < cycle.length; i++)
            result[i] = cycle[(minIdx + i) % cycle.length];
        return result;
    }
}
