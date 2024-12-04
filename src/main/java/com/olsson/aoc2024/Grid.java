package com.olsson.aoc2024;

import java.util.List;

public class Grid {

    private final List<String> data;

    public Grid(List<String> data) {
        this.data = data;
    }

    public String get(Point p) {
        try {
            return "" + data.get(p.y()).charAt(p.x());
        } catch (IndexOutOfBoundsException _) {
            return "";
        }
    }

    public int height() {
        return data.size();
    }

    public int width() {
        return data.getFirst().length();
    }
}
