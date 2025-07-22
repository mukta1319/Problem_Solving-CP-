import java.util.*;

class Djk {
    static final int V = 5;

    int min(int[] d, boolean[] done) {
        int m = Integer.MAX_VALUE, idx = -1;
        for (int i = 0; i < V; i++)
            if (!done[i] && d[i] <= m) {
                m = d[i];
                idx = i;
            }
        return idx;
    }

    void show(int[] d) {
        System.out.printf("%-6s %s\n", "Node", "Cost");
        for (int i = 0; i < V; i++) {
            System.out.printf("%-6d %d\n", i, d[i]);
        }
    }

    void run(int[][] g, int[] c, int src) {
        int[] d = new int[V];
        boolean[] done = new boolean[V];

        Arrays.fill(d, Integer.MAX_VALUE);
        d[src] = c[src];

        for (int i = 0; i < V - 1; i++) {
            int u = min(d, done);
            done[u] = true;

            for (int v = 0; v < V; v++)
                if (!done[v] && g[u][v] != 0 && d[u] != Integer.MAX_VALUE &&
                        d[u] + g[u][v] + c[v] < d[v])
                    d[v] = d[u] + g[u][v] + c[v];
        }

        show(d);
    }

    public static void main(String[] args) {
        int[][] g = {
                { 0, 10, 5, 0, 0 },
                { 0, 0, 2, 1, 0 },
                { 0, 3, 0, 9, 2 },
                { 0, 0, 0, 0, 4 },
                { 7, 0, 0, 6, 0 }
        };

        int[] c = { 2, 4, 1, 6, 3 };

        new Djk().run(g, c, 0);
    }
}
