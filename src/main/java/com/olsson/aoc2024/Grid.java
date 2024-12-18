package com.olsson.aoc2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Grid {

    private final List<String> data;

    public Grid(List<String> data) {
        this.data = data;
    }

    public String get(Point p) {
        return get(p, true);
    }

    public String get(Point p, boolean safe) {
        try {
            return "" + data.get(p.y()).charAt(p.x());
        } catch (IndexOutOfBoundsException e) {
            if (safe) {
                return "";
            }
            throw e;
        }
    }

    public List<Point> allMatching(String value) {
        var values = new ArrayList<Point>();
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                var p = new Point(j, i);
                var onGrid = get(new Point(j, i), false);
                if (onGrid.equals(value)) {
                    values.add(p);
                }
            }
        }
        return values;
    }

    public List<Point> adjacentNeighbors(Point p) {
        return Stream.of(p.move(0, 1),
                        p.move(0, -1),
                        p.move(1, 0),
                        p.move(-1, 0)
                ).filter(neighbor -> !outOfBounds(neighbor))
                .toList();
    }

    public Point find(String character) {
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                var test = new Point(j, i);
                if (get(test).equals(character)) {
                    return test;
                }
            }
        }
        return new Point(-1, -1);
    }

    public boolean outOfBounds(Point p) {
        return p.x() >= width() || p.x() < 0 || p.y() >= height() || p.y() < 0;
    }

    public int height() {
        return data.size();
    }

    public int width() {
        return data.getFirst().length();
    }
}
