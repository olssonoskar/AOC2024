package com.olsson.aoc2024;

public record Point(int x, int y) {

    public Point move(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }
}
