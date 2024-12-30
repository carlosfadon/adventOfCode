package org.example.year2024.day16;

public class Node {
    Position pos;
    int score;
    Direction dir;
    Node prev;

    Node(Position pos, int score, Direction dir, Node prev) {
        this.pos = pos;
        this.score = score;
        this.dir = dir;
        this.prev = prev;
    }
}
