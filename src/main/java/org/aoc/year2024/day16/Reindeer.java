package org.aoc.year2024.day16;

public class Reindeer {
    Position pos;
    Direction dir;
    int score;

    public Reindeer(char[][] board, Position pos) {
        this.pos = pos;
        this.dir = Direction.RIGHT;
        this.score = 0;
    }

    public Reindeer(Reindeer reindeer) {
        this.pos = new Position(reindeer.pos.x, reindeer.pos.y);
        this.dir = reindeer.dir;
        this.score = reindeer.score;
    }

    public boolean isInside(Position pos, char[][] board) {
        return pos.x >= 0 && pos.x < board[0].length && pos.y >= 0 && pos.y < board.length;
    }

    public boolean tryMove(Direction newDir, char[][] board, boolean[][] visited) {
        switch (newDir) {
            case UP:
                if ((pos.y > 0) && (!visited[pos.y - 1][pos.x]) && (board[pos.y-1][pos.x] != '#')) {
                   pos.y--;
                   if (newDir != dir) {
                       score += 1000;
                   }
                   score++;
                   dir = newDir;
                   return true;

                }
                break;
            case RIGHT:
                if ((pos.x < board[0].length) && (!visited[pos.y][pos.x + 1]) && (board[pos.y][pos.x + 1] != '#')) {
                    pos.x++;
                    if (newDir != dir) {
                        score += 1000;
                    }
                    score++;
                    dir = newDir;
                    return true;
                }
                break;
            case DOWN:
                if ((pos.y < board.length) && (!visited[pos.y + 1][pos.x]) && (board[pos.y + 1][pos.x] != '#')) {
                    pos.y++;
                    if (newDir != dir) {
                        score += 1000;
                    }
                    score++;
                    dir = newDir;
                    return true;
                }
                break;
            case LEFT:
                if ((pos.x > 0) && (!visited[pos.y][pos.x - 1]) && (board[pos.y][pos.x - 1] != '#')) {
                    pos.x--;
                    if (newDir != dir) {
                        score += 1000;
                    }
                    score++;
                    dir = newDir;
                    return true;
                }
                break;
        }
        return false;
    }
}
