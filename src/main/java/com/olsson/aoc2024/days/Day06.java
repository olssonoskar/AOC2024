package com.olsson.aoc2024.days;

import com.olsson.aoc2024.Grid;
import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.HashSet;
import java.util.Set;

public class Day06 implements Day {

    private static final String MOVE = "M";
    private static final String TURN = "T";

    @Override
    public String part1() {
        return "" + part1("6");
    }

    @Override
    public String part2() {
        return "";
    }

    public long part1(String day) {
        var grid = new Grid(InputUtils.getLines(day));
        var visited = walk(start(grid), grid);
        return visited.size();
    }

    private Point start(Grid grid) {
        for (int i = 0; i < grid.height(); i++) {
            for (int j = 0; j < grid.width(); j++) {
                var point = new Point(j, i);
                if (grid.get(point).equals("^")) {
                    return point;
                }
            }
        }
        throw new IllegalArgumentException("Grid does not contain an ^");
    }

    private Set<Point> walk(Point start, Grid grid) {
        var guard = new Guard(start, "N", "M");
        var visited = new HashSet<Point>();
        visited.add(guard.at());
        while (!grid.outOfBounds(guard.at())) {
            guard = guard.patrol(grid);
            visited.add(guard.at());
        }
        // Last move was illegal
        visited.remove(guard.at());
        return visited;
    }

    private record Guard(Point at, String facing, String lastAction) {

        private Guard patrol(Grid grid) {
            var next = nextMove(this.at, this.facing);
            var look = grid.get(next);
            if (look.equals("#")) {
                return new Guard(this.at, turn(this.facing), TURN);
            } else {
                return new Guard(next, this.facing, MOVE);
            }
        }

        private String turn(String facing) {
            return switch (facing) {
                case "N" -> "E";
                case "E" -> "S";
                case "S" -> "W";
                case "W" -> "N";
                default -> throw new IllegalStateException("Cant face :" + facing);
            };
        }

        private Point nextMove(Point from, String facing) {
            return switch (facing) {
                case "N" -> from.move(0, -1);
                case "E" -> from.move(1, 0);
                case "S" -> from.move(0, 1);
                case "W" -> from.move(-1, 0);
                default -> throw new IllegalStateException("Cant face :" + facing);
            };
        }
    }
}
