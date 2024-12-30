package org.aoc.year2024.day15;

public class RobotState {

    char[][] board;
    int posX;
    int posY;

    public RobotState(char[][] board, int posX, int posY) {
        this.board = board;
        this.posX = posX;
        this.posY = posY;
    }

    public void tryMove(char direction) {
        int newX = posX;
        int newY = posY;
        switch (direction) {
            case '^':
                newY--;
                while (isInside(newX, newY)) {
                    if (board[newY][newX] == 'O') {
                        newY--;
                    } else if (board[newY][newX] == '#') {
                        break;
                    } else if (board[newY][newX] == '.') {
                        for (int i = newY; i < posY; i++) {
                            board[i][posX] = board[i + 1][posX];
                        }
                        board[posY][posX] = '.';
                        posY--;

                        break;
                    }
                }

                break;
            case '>':
                newX++;
                while (isInside(newX, newY)) {
                    if (board[newY][newX] == 'O') {
                        newX++;
                    } else if (board[newY][newX] == '#') {
                        break;
                    } else if (board[newY][newX] == '.') {
                        for (int i = newX; i > posX; i--) {
                            board[posY][i] = board[posY][i - 1];
                        }
                        board[posY][posX] = '.';
                        posX++;

                        break;
                    }
                }
                break;
            case 'v':
                newY++;
                while (isInside(newX, newY)) {
                    if (board[newY][newX] == 'O') {
                        newY++;
                    } else if (board[newY][newX] == '#') {
                        break;
                    } else if (board[newY][newX] == '.') {
                        for (int i = newY; i > posY; i--) {
                            board[i][posX] = board[i - 1][posX];
                        }
                        board[posY][posX] = '.';
                        posY++;

                        break;
                    }
                }
                break;
            case '<':
                newX--;
                while (isInside(newX, newY)) {
                    if (board[newY][newX] == 'O') {
                        newX--;
                    } else if (board[newY][newX] == '#') {
                        break;
                    } else if (board[newY][newX] == '.') {
                        for (int i = newX; i < posX; i++) {
                            board[posY][i] = board[posY][i + 1];
                        }
                        board[posY][posX] = '.';
                        posX--;

                        break;
                    }
                }
                break;
        }

    }

    public void move2(char direction) {
        switch (direction) {
            case '^':
                if (isInside(posX, posY - 1)) {
                    if (board[posY - 1][posX] == '.') {
                        board[posY - 1][posX] = '@';
                        board[posY][posX] = '.';
                        posY--;
                    } else if (board[posY - 1][posX] == '[') {
                        moveBox(posX, posY - 1, '^');
                        board[posY - 1][posX] = '@';
                        board[posY][posX] = '.';
                        posY--;

                    } else if (board[posY - 1][posX] == ']') {
                        moveBox(posX - 1, posY - 1, '^');
                        board[posY - 1][posX] = '@';
                        board[posY][posX] = '.';
                        posY--;

                    }
                }

                break;
            case '>':
                if (isInside(posX + 1, posY)) {
                    if (board[posY][posX + 1] == '.') {
                        board[posY][posX + 1] = '@';
                        board[posY][posX] = '.';
                        posX++;
                    } else if (board[posY][posX + 1] == '[') {
                        moveBox(posX + 1, posY, '>');
                        board[posY][posX + 1] = '@';
                        board[posY][posX] = '.';
                        posX++;

                    }

                }

                break;
            case 'v':
                if (isInside(posX, posY + 1)) {
                    if (board[posY + 1][posX] == '.') {
                        board[posY + 1][posX] = '@';
                        board[posY][posX] = '.';
                        posY++;
                    } else if (board[posY + 1][posX] == '[') {
                        moveBox(posX, posY + 1, 'v');
                        board[posY + 1][posX] = '@';
                        board[posY][posX] = '.';
                        posY++;
                    } else if (board[posY + 1][posX] == ']') {
                        moveBox(posX - 1, posY + 1, 'v');
                        board[posY + 1][posX] = '@';
                        board[posY][posX] = '.';
                        posY++;
                    }
                }


                break;
            case '<':
                if (isInside(posX - 1, posY)) {
                    if (board[posY][posX - 1] == '.') {
                        board[posY][posX - 1] = '@';
                        board[posY][posX] = '.';
                        posX--;
                    } else if (board[posY][posX - 1] == ']') {
                        moveBox(posX - 2, posY, '<');
                        board[posY][posX - 1] = '@';
                        board[posY][posX] = '.';
                        posX--;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    public boolean tryMove2(char direction) {
        switch (direction) {
            case '^':
                if (isInside(posX, posY - 1)) {
                    if (board[posY - 1][posX] == '.') {
                        return true;
                    } else if (board[posY - 1][posX] == '[') {
                        if (tryMoveBox(posX, posY - 1, '^')) {
                            return true;
                        }
                    } else if (board[posY - 1][posX] == ']') {
                        if (tryMoveBox(posX - 1, posY - 1, '^')) {
                            return true;
                        }
                    }
                }

                break;
            case '>':
                if (isInside(posX + 1, posY)) {
                    if ((board[posY][posX + 1] == '.') || ((board[posY][posX + 1] == '[') && (tryMoveBox(posX + 1, posY, '>')))) {
                        return true;
                    }
                }

                break;
            case 'v':
                if (isInside(posX, posY + 1)) {
                    if ((board[posY + 1][posX] == '.') ||
                            ((board[posY + 1][posX] == '[') && (tryMoveBox(posX, posY + 1, 'v'))) ||
                            ((board[posY + 1][posX] == ']') && (tryMoveBox(posX - 1, posY + 1, 'v')))) {
                        return true;
                    }
                }


                break;
            case '<':
                if (isInside(posX - 1, posY)) {
                    if ((board[posY][posX - 1] == '.') ||
                            ((board[posY][posX - 1] == ']') && (tryMoveBox(posX - 2, posY, '<')))) {
                        return true;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        return false;
    }

    boolean tryMoveBox(int x, int y, char dir) {
        switch (dir) {
            case '^':
                boolean success1 = true;
                boolean success2 = true;
                boolean success3 = true;
                if (isInside(x, y - 1)) {
                    if ((board[y - 1][x] == '.') && (board[y - 1][x + 1] == '.')) {
                        return true;
                    } else if (board[y - 1][x] == '#' || board[y - 1][x + 1] == '#') {
                        return false;
                    }
                    if (board[y - 1][x] == '[') {
                        success1 = tryMoveBox(x, y - 1, '^');
                    }
                    if (board[y - 1][x - 1] == '[') {
                        success2 = tryMoveBox(x - 1, y - 1, '^');
                    }
                    if (board[y - 1][x + 1] == '[') {
                        success3 = tryMoveBox(x + 1, y - 1, '^');
                    }
                    if (success1 && success2 && success3) {
                        return true;
                    }
                    return false;
                }
                break;
            case '>':
                if (isInside(x + 2, y)) {
                    if (board[y][x + 2] == '.') {
                        return true;
                    } else if (board[y][x + 2] == '[') {
                        boolean success = tryMoveBox(x + 2, y, '>');
                        if (success) {
                            return true;
                        }
                    } else if (board[y][x + 2] == '#') {
                        return false;
                    }
                }
                break;
            case 'v':
                boolean success4 = true;
                boolean success5 = true;
                boolean success6 = true;
                if (isInside(x, y + 1)) {
                    if ((board[y + 1][x] == '.') && (board[y + 1][x + 1] == '.')) {
                        return true;
                    } else if (board[y + 1][x] == '#' || board[y + 1][x + 1] == '#') {
                        return false;
                    }
                    if (board[y + 1][x] == '[') {
                        success4 = tryMoveBox(x, y + 1, 'v');
                    }
                    if (board[y + 1][x] == ']') {
                        success5 = tryMoveBox(x - 1, y + 1, 'v');
                    }
                    if (board[y + 1][x + 1] == '[') {
                        success6 = tryMoveBox(x + 1, y + 1, 'v');
                    }
                    if (success4 && success5 && success6) {
                        return true;
                    }
                    return false;
                }
                break;
            case '<':
                if (isInside(x - 1, y)) {
                    if (board[y][x - 1] == '.') {
                        return true;
                    } else if (board[y][x - 1] == ']') {
                        boolean success = tryMoveBox(x - 2, y, '<');
                        if (success) {
                            return true;
                        }
                    } else if (board[y][x - 1] == '#') {
                        return false;
                    }
                }
        }
        return false;
    }

    void moveBox(int x, int y, char dir) {
        switch (dir) {
            case '^':
                if (isInside(x, y - 1)) {
                    if ((board[y - 1][x] == '.') && (board[y - 1][x + 1] == '.')) {
                        board[y][x] = '.';
                        board[y][x + 1] = '.';
                        board[y - 1][x] = '[';
                        board[y - 1][x + 1] = ']';
                        return;
                    } else if (board[y - 1][x] == '#' || board[y - 1][x + 1] == '#') {
                        break;
                    }
                    if (board[y - 1][x] == '[') {
                        moveBox(x, y - 1, '^');
                    }
                    if (board[y - 1][x - 1] == '[') {
                        moveBox(x - 1, y - 1, '^');
                    }
                    if (board[y - 1][x + 1] == '[') {
                        moveBox(x + 1, y - 1, '^');
                    }
                    board[y][x] = '.';
                    board[y][x + 1] = '.';
                    board[y - 1][x] = '[';
                    board[y - 1][x + 1] = ']';

                }
                break;
            case '>':
                if (isInside(x + 2, y)) {
                    if (board[y][x + 2] == '.') {
                        board[y][x] = '.';
                        board[y][x + 1] = '[';
                        board[y][x + 2] = ']';
                    } else if (board[y][x + 2] == '[') {
                        moveBox(x + 2, y, '>');
                        board[y][x] = '.';
                        board[y][x + 1] = '[';
                        board[y][x + 2] = ']';
                    }
                }
                break;
            case 'v':
                if (isInside(x, y + 1)) {
                    if ((board[y + 1][x] == '.') && (board[y + 1][x + 1] == '.')) {
                        board[y][x] = '.';
                        board[y][x + 1] = '.';
                        board[y + 1][x] = '[';
                        board[y + 1][x + 1] = ']';
                        return;
                    }
                    if (board[y + 1][x] == '[') {
                        moveBox(x, y + 1, 'v');
                    }
                    if (board[y + 1][x] == ']') {
                        moveBox(x - 1, y + 1, 'v');
                    }
                    if (board[y + 1][x + 1] == '[') {
                        moveBox(x + 1, y + 1, 'v');
                    }
                    board[y][x] = '.';
                    board[y][x + 1] = '.';
                    board[y + 1][x] = '[';
                    board[y + 1][x + 1] = ']';
                }
                break;
            case '<':
                if (isInside(x - 1, y)) {
                    if (board[y][x - 1] == '.') {
                        board[y][x+1] = '.';
                        board[y][x - 1] = '[';
                        board[y][x] = ']';
                    } else if (board[y][x - 1] == ']') {
                        moveBox(x - 2, y, '<');
                        board[y][x+1] = '.';
                        board[y][x - 1] = '[';
                        board[y][x] = ']';
                    }
                }
        }
    }

    boolean isInside(int x, int y) {
        return x >= 0 && x < board[0].length && y >= 0 && y < board.length;
    }
}

