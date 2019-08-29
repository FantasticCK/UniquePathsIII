package com.CK;

import java.util.Stack;

public class Main {

    public static void main(String[] args) {
//        System.out.println(new Solution().uniquePathsIII(new int[][]{new int[]{1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 2}}));
        System.out.println(new Solution().uniquePathsIII(new int[][]{{-1, 0, 1, -1}, {2, 0, 0, 0}}));
    }
}

class Solution {
    int[][] dir = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
    int res = 0;

    public int uniquePathsIII(int[][] grid) {
        int r = grid.length, c = grid[0].length;
        if (r == 0 || c == 0)
            return 0;

        int stepSum = r * c - 1;
        int[] st = new int[2], ed = new int[2];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == -1)
                    stepSum--;

                if (grid[i][j] == 1) {
                    st[0] = i;
                    st[1] = j;
                }

                if (grid[i][j] == 2) {
                    ed[0] = i;
                    ed[1] = j;
                }
            }
        }
        Stack<int[]> stack = new Stack<>();
        boolean[][] visited = new boolean[r][c];
        stack.push(st);
        visited[st[0]][st[1]] = true;
        search(grid, stack, visited, 0, ed, stepSum);
        return res;
    }

    private void search(int[][] grid, Stack<int[]> stack, boolean[][] visited, int steps, int[] ed, int stepSum) {
        int size = stack.size();
        if (size == 0)
            return;

        for (int s = 0; s < size; s++) {
            int[] curr = stack.pop();

            if (curr[0] == ed[0] && curr[1] == ed[1] && steps == stepSum) {
                res++;
            }

            for (int i = 0; i < 4; i++) {
                int nextR = curr[0] + dir[i][0];
                int nextC = curr[1] + dir[i][1];
                if (nextR == ed[0] && nextC == ed[1] && steps != stepSum - 1)
                    continue;

                if (isValid(grid, visited, nextR, nextC)) {
                    visited[nextR][nextC] = true;
                    steps++;
                    stack.push(new int[]{nextR, nextC});
                    search(grid, stack, visited, steps, ed, stepSum);
                    visited[nextR][nextC] = false;
                    steps--;
                }
            }
        }
    }

    private boolean isValid(int[][] grid, boolean[][] visited, int r, int c) {
        return r >= 0 && c >= 0 && r < grid.length && c < grid[0].length && grid[r][c] != -1 && !visited[r][c];
    }
}

//DP
class Solution2 {
    int ans;
    int[][] grid;
    int R, C;
    int tr, tc, target;
    int[] dr = new int[]{0, -1, 0, 1};
    int[] dc = new int[]{1, 0, -1, 0};
    Integer[][][] memo;

    public int uniquePathsIII(int[][] grid) {
        this.grid = grid;
        R = grid.length;
        C = grid[0].length;
        target = 0;

        int sr = 0, sc = 0;
        for (int r = 0; r < R; ++r)
            for (int c = 0; c < C; ++c) {
                if (grid[r][c] % 2 == 0)
                    target |= code(r, c);

                if (grid[r][c] == 1) {
                    sr = r;
                    sc = c;
                } else if (grid[r][c] == 2) {
                    tr = r;
                    tc = c;
                }
            }

        memo = new Integer[R][C][1 << R*C];
        return dp(sr, sc, target);
    }

    public int code(int r, int c) {
        return 1 << (r * C + c);
    }

    public Integer dp(int r, int c, int todo) {
        if (memo[r][c][todo] != null)
            return memo[r][c][todo];

        if (r == tr && c == tc) {
            return todo == 0 ? 1 : 0;
        }

        int ans = 0;
        for (int k = 0; k < 4; ++k) {
            int nr = r + dr[k];
            int nc = c + dc[k];
            if (0 <= nr && nr < R && 0 <= nc && nc < C) {
                if ((todo & code(nr, nc)) != 0)
                    ans += dp(nr, nc, todo ^ code(nr, nc));
            }
        }
        memo[r][c][todo] = ans;
        return ans;
    }
}
