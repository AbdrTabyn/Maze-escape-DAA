/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int[][] open = Graphs.open(w, broken);

        // Count dead ends
        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (open[i].length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }

        // Iterative DFS: cell + next direction to try
        int[] stack = new int[n];
        int[] direction = new int[n];
        int top = 0;
        stack[0] = 0;
        visited[0] = true;

        int visitedCount = 1;
        int exit = n - 1;

        while (top >= 0 && !visited[exit]) {
            int cell = stack[top];
            if (direction[top] == 4) {
                top--;
                continue;
            }
            int d = direction[top]++;
            int next;

            // Required order: up, right, down, left
            if (d == 0) {
                next = cell - cols;
            } else if (d == 1) {
                next = cell + 1;
            } else if (d == 2) {
                next = cell + cols;
            } else {
                next = cell - 1;
            }

            if (next < 0 || next >= n || visited[next]) {
                continue;
            }

            // Check whether this neighbour is actually connected
            boolean isOpen = false;
            for (int neighbour : open[cell]) {
                if (neighbour == next) {
                    isOpen = true;
                    break;
                }
            }
            if (!isOpen) {
                continue;
            }

            visited[next] = true;
            visitedCount++;
            parent[next] = cell;

            top++;
            stack[top] = next;
            direction[top] = 0;
        }

        // Reconstruct path from exit back to start
        int pathLength = 1;
        for (int cell = exit; cell != 0; cell = parent[cell]) {
            pathLength++;
        }

        int[] path = new int[pathLength];
        int cell = exit;
        for (int i = pathLength - 1; i >= 0; i--) {
            path[i] = cell;

            if (cell != 0) {
                cell = parent[cell];
            }
        }
        return new Result(path, visitedCount, deadEnds);
    }
}
