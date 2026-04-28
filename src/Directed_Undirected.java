import java.util.*;

public class Directed_Undirected {

    private static int[] parent;

    private static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a != b) parent[a] = b;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        if (n == 0) {
            System.out.println("The graph is weakly connected (trivially, no nodes).");
            return;
        }

        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                adj[i][j] = sc.nextInt();

        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (adj[i][j] != 0 || adj[j][i] != 0)   // edge in either direction
                    union(i, j);

        int root = find(0);
        boolean weaklyConnected = true;
        for (int i = 1; i < n; i++) {
            if (find(i) != root) {
                weaklyConnected = false;
                break;
            }
        }

        if (weaklyConnected)
            System.out.println("The graph IS weakly connected.");
        else
            System.out.println("The graph is NOT weakly connected.");
    }
}
