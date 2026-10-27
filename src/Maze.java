/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        // TODO
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] temp = new int[w.cells() - 1];
        int count = 0;

        for (int i = 0; i < w.count(); i++) {
            if (dsu.union(w.a()[i], w.b()[i])) {
                temp[count++] = i;
                if (count == w.cells() - 1) {
                    break;
                }
            }
        }
        return java.util.Arrays.copyOf(temp, count);
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < w.count(); i++) {
            order[i] = i;
        }
        java.util.Arrays.sort(order,
                java.util.Comparator.comparingInt(i -> w.cost()[i]));

        DisjointSet dsu = new DisjointSet(w.cells());
        int[] result = new int[w.cells() - 1];
        int count = 0;

        for (int i : order) {
            if (dsu.union(w.a()[i], w.b()[i])) {
                result[count++] = i;
                if (count == w.cells() - 1) {
                    break;
                }
            }
        }
        return java.util.Arrays.copyOf(result, count);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();

        java.util.List<int[]>[] graph = new java.util.ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new java.util.ArrayList<>();
        }

        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];
            int cost = w.cost()[i];

            graph[a].add(new int[]{b, cost});
            graph[b].add(new int[]{a, cost});
        }

        boolean[] used = new boolean[n];

        java.util.PriorityQueue<int[]> pq =
                new java.util.PriorityQueue<>(
                        java.util.Comparator.comparingInt(x -> x[1]));

        pq.add(new int[]{0, 0});

        long total = 0;
        int visited = 0;

        while (!pq.isEmpty() && visited < n) {
            int[] current = pq.poll();
            int cell = current[0];
            int cost = current[1];

            if (used[cell]) {
                continue;
            }
            used[cell] = true;
            visited++;
            total += cost;

            for (int[] edge : graph[cell]) {
                if (!used[edge[0]]) {
                    pq.add(edge);
                }
            }
        }
        return total;
    }
}
