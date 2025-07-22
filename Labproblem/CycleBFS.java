import java.util.*;

public class CycleBFS {

    static char[] c = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'S'};
    static int[] e = {2, 2, 2, 2, 2, 2, 3, 3};
    static int[][] list = {
            {3, 7}, {4, 7}, {5, 7}, {0, 6},
            {1, 6}, {2, 6}, {3, 4, 5}, {0, 1, 2}
    };
    static int[] checked = new int[20];
    static int[] parent = new int[20];
    static int[] que = new int[20];
    static int first = 0, last = 0;
    static boolean cycleFound = false;

    public static void main(String[] args) {
        Arrays.fill(parent, -1);
        bfsCycle(7);

        if (cycleFound) {
            System.out.println("Cycle detected in the graph.");
        } else {
            System.out.println("No cycle in the graph.");
        }
    }

    static void bfsCycle(int start) {
        enq(start);

        while (first < last) {
            int n = dq();

            for (int i = 0; i < e[n]; i++) {
                int neighbor = list[n][i];

                if (notChecked(neighbor) == 1) {
                    enq(neighbor);
                    parent[neighbor] = n;
                } else if (parent[n] != neighbor) {
                    cycleFound = true;
                    return;
                }
            }
        }
    }

    static int notChecked(int n) {
        if (checked[n] == 1)
            return 0;
        return 1;
    }

    static void enq(int n) {
        checked[n] = 1;
        que[last] = n;
        last++;
    }

    static int dq() {
        System.out.print(c[que[first]] + " ");
        first++;
        return que[first - 1];
    }
}
