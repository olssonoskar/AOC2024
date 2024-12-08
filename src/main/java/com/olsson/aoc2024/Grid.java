package com.olsson.aoc2024;

import java.util.List;

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
