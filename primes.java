import java.util.*;

class Primes {
    static class Edge implements Comparable<Edge> {
        int u, v, w;
        Edge(int u, int v, int w) {
            this.u = u; this.v = v; this.w = w;
        }
        public int compareTo(Edge other) {
            return this.w - other.w;
        }
    }

    int V;
    Edge[] edges;
    List<Edge> mstEdges = new ArrayList<>();
    int totalWeight = 0;

    Primes(int V, Edge[] edges) {
        this.V = V;
        this.edges = edges;
    }

    int[] parent, rank;

    void makeSet() {
        parent = new int[V];
        rank = new int[V];
        for (int i = 0; i < V; i++) parent[i] = i;
    }

    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) return false;

        if (rank[rootA] < rank[rootB]) {
            parent[rootA] = rootB;
        } else if (rank[rootB] < rank[rootA]) {
            parent[rootB] = rootA;
        } else {
            parent[rootB] = rootA;
            rank[rootA]++;
        }
        return true;
    }

    long countDistinctMSTs() {
        Arrays.sort(edges);
        makeSet();

        long count = 1;
        int edgesUsed = 0;
        int i = 0;

        while (edgesUsed < V - 1) {
            int w = edges[i].w;
            List<Edge> sameWeightEdges = new ArrayList<>();
            while (i < edges.length && edges[i].w == w) {
                sameWeightEdges.add(edges[i]);
                i++;
            }

            List<Edge> candidates = new ArrayList<>();
            for (Edge e : sameWeightEdges) {
                if (find(e.u) != find(e.v)) {
                    candidates.add(e);
                }
            }

            Set<Integer> compRoots = new HashSet<>();
            for (Edge e : candidates) {
                compRoots.add(find(e.u));
                compRoots.add(find(e.v));
            }

            int componentsBefore = compRoots.size();
            int edgesNeeded = componentsBefore - 1;

            int added = 0;
            for (Edge e : candidates) {
                if (union(e.u, e.v)) {
                    edgesUsed++;
                    added++;
                    mstEdges.add(e);
                    totalWeight += e.w;
                    if (added == edgesNeeded) break;
                }
            }
        }

        return count;
    }

    void printMST() {
        System.out.println(" MST Edges:");
        for (Edge e : mstEdges) {
            System.out.println(e.u + " -- " + e.v + " (weight: " + e.w + ")");
        }
        System.out.println("Total MST Weight: " + totalWeight);
    }

    public static void main(String[] args) {
        int V = 5;
        Edge[] edges = {
                new Edge(0, 1, 2),
                new Edge(1, 2, 3),
                new Edge(0, 3, 6),
                new Edge(1, 3, 8),
                new Edge(1, 4, 5),
                new Edge(2, 4, 7),
                new Edge(3, 4, 9)
        };

        Primes primes = new Primes(V, edges);
        long numberOfMSTs = primes.countDistinctMSTs();
        primes.printMST();
        System.out.println("d be the output" +
                "Number of distinct MSTs: " + numberOfMSTs);
    }
}
