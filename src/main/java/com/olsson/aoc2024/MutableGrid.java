package com.olsson.aoc2024;

import java.util.List;

public class MutableGrid {

    private final List<char[]> data;

    public MutableGrid(List<String> input) {
        this.data = input.stream().map(String::toCharArray).toList();
    }

    public String get(Point p) {
        return getOrDefault(p, "?");
    }

    public String getOrDefault(Point p, String fallback) {
        try {
            return "" + data.get(p.y())[p.x()];
        } catch (IndexOutOfBoundsException e) {
            return fallback;
        }
    }

    public void write(Point p1, String character) {
        data.get(p1.y())[p1.x()] = character.charAt(0);
    }

    public int height() {
        return data.size();
    }

    public int width() {
        return data.getFirst().length;
    }

    public boolean outOfBounds(Point p) {
        return p.x() >= width() || p.x() < 0 || p.y() >= height() || p.y() < 0;
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

    public void debugData() {
        for (int i = 0; i < data.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < data.getFirst().length; j++) {
                stringBuilder.append(get(new Point(j, i)));
            }
            System.out.println(stringBuilder);
        }
        System.out.println(" ");
    }
}
