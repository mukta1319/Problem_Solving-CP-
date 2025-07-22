package Mukta;

import java.util.*;

public class Graph {
    class E implements Comparable<E> {
        int u, v, w;
        public int compareTo(E e) {
            return this.w - e.w;
        }
    }

    class S {
        int p, r;
    }

    int n, m;
    E[] es;

    Graph(int n, int m) {
        this.n = n;
        this.m = m;
        es = new E[m];
        for (int i = 0; i < m; i++) es[i] = new E();
    }

    int find(S[] sets, int i) {
        if (sets[i].p != i)
            sets[i].p = find(sets, sets[i].p);
        return sets[i].p;
    }

    void union(S[] sets, int x, int y) {
        int rx = find(sets, x);
        int ry = find(sets, y);
        if (sets[rx].r < sets[ry].r)
            sets[rx].p = ry;
        else if (sets[rx].r > sets[ry].r)
            sets[ry].p = rx;
        else {
            sets[ry].p = rx;
            sets[rx].r++;
        }
    }

    // Fixed determinant calculation using integer arithmetic with proper handling
    long det(long[][] mat, int sz) {
        if (sz == 0) return 1;
        if (sz == 1) return mat[0][0];

        // Create a copy to work with
        long[][] A = new long[sz][sz];
        for (int i = 0; i < sz; i++)
            System.arraycopy(mat[i], 0, A[i], 0, sz);

        long res = 1;
        for (int i = 0; i < sz; i++) {
            // Find non-zero pivot
            int pivot = i;
            for (int j = i + 1; j < sz; j++) {
                if (Math.abs(A[j][i]) > Math.abs(A[pivot][i]))
                    pivot = j;
            }

            if (A[pivot][i] == 0)
                return 0;

            // Swap rows if needed
            if (pivot != i) {
                long[] tmp = A[i];
                A[i] = A[pivot];
                A[pivot] = tmp;
                res = -res;
            }

            res *= A[i][i];

            // Eliminate using integer operations (only when divisible)
            for (int j = i + 1; j < sz; j++) {
                if (A[j][i] != 0) {
                    // Use cross multiplication to avoid division
                    long factor = A[j][i];
                    long pivot_val = A[i][i];

                    for (int k = i; k < sz; k++) {
                        A[j][k] = A[j][k] * pivot_val - factor * A[i][k];
                    }
                    res *= pivot_val;
                }
            }
        }

        // Calculate final determinant
        long finalDet = res;
        for (int i = 0; i < sz - 1; i++) {
            if (A[i][i] != 0) {
                finalDet /= A[i][i];
            }
        }

        return Math.abs(finalDet);
    }

    long countST(int cCnt, List<int[]> cEdges) {
        long[][] lap = new long[cCnt][cCnt];
        for (int[] e : cEdges) {
            int a = e[0], b = e[1];
            lap[a][a]++;
            lap[b][b]++;
            lap[a][b]--;
            lap[b][a]--;
        }

        int sz = cCnt - 1;
        long[][] minor = new long[sz][sz];
        for (int i = 0; i < sz; i++)
            for (int j = 0; j < sz; j++)
                minor[i][j] = lap[i][j];

        return det(minor, sz);
    }

    void countMSTs() {
        Arrays.sort(es);

        S[] sets = new S[n];
        for (int i = 0; i < n; i++) {
            sets[i] = new S();
            sets[i].p = i;
            sets[i].r = 0;
        }

        long ways = 1;
        int idx = 0;

        while (idx < m) {
            int w = es[idx].w;
            List<E> sameW = new ArrayList<>();
            while (idx < m && es[idx].w == w) sameW.add(es[idx++]);

            Map<Integer, Integer> comp = new HashMap<>();
            int cCnt = 0;

            for (int i = 0; i < n; i++) {
                int r = find(sets, i);
                if (!comp.containsKey(r)) comp.put(r, cCnt++);
            }

            List<int[]> cEdges = new ArrayList<>();
            for (E e : sameW) {
                int a = comp.get(find(sets, e.u));
                int b = comp.get(find(sets, e.v));
                if (a != b) cEdges.add(new int[]{a, b});
            }

            if (!cEdges.isEmpty()) {
                long cnt = countST(cCnt, cEdges);
                System.out.print("Edges with weight " + w + ": ");
                for (int i = 0; i < sameW.size(); i++) {
                    E e = sameW.get(i);
                    System.out.print("(" + e.u + "," + e.v + ")");
                    if (i < sameW.size() - 1) System.out.print(", ");
                }
                System.out.println(" - " + cnt + " spanning trees");
                ways *= cnt;
            }

            for (E e : sameW) {
                int x = find(sets, e.u);
                int y = find(sets, e.v);
                if (x != y) union(sets, x, y);
            }
        }

        System.out.println("Number of distinct MSTs: " + ways);
    }

    public static void main(String[] args) {
        int n = 4, m = 5;
        Graph g = new Graph(n, m);

        g.es[0].u = 0; g.es[0].v = 1; g.es[0].w = 10;
        g.es[1].u = 0; g.es[1].v = 2; g.es[1].w = 6;
        g.es[2].u = 0; g.es[2].v = 3; g.es[2].w = 5;
        g.es[3].u = 1; g.es[3].v = 3; g.es[3].w = 15;
        g.es[4].u = 2; g.es[4].v = 3; g.es[4].w = 4;

        g.countMSTs();
    }
}