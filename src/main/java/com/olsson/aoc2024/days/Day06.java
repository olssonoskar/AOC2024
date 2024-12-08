package com.olsson.aoc2024.days;

import com.olsson.aoc2024.Grid;
import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day06 implements Day {

    private static final Point NO_NEW_OBSTACLE = new Point(-1, -1);
    private static final Point NO_CYCLE = new Point(-1, -1);

    @Override
    public String part1() {
        return "" + part1("6");
    }

    @Override
    public String part2() {
        return "" + part2("6");
    }

    public long part1(String day) {
        var grid = new GridWithObstacle(InputUtils.getLines(day), NO_NEW_OBSTACLE);
        var visited = walk(start(grid), grid);
        return visited.stream()
                .map(Movement::p)
                .distinct()
                .count();
    }

    public long part2(String day) {
        var input = InputUtils.getLines(day);
        var grid = new GridWithObstacle(input, NO_NEW_OBSTACLE);
        var pointsWithCycles = walkWithCycleCheck(start(grid), input);
        pointsWithCycles.remove(NO_CYCLE);
        return pointsWithCycles.size();
    }

    private Point start(Grid grid) {
        for (int i = 0; i < grid.height(); i++) {
            for (int j = 0; j < grid.width(); j++) {
                var point = new Point(j, i);
                if (grid.get(point, false).equals("^")) {
                    return point;
                }
            }
        }
        throw new IllegalArgumentException("Grid does not contain an ^");
    }

    private Set<Movement> walk(Point start, GridWithObstacle grid) {
        var guard = new Guard(start, "N");
        var visited = new HashSet<Movement>();
        visited.add(Movement.from(guard));
        while (!grid.outOfBounds(guard.at()) && !grid.outOfBounds(guard.nextMove())) {
            guard = guard.patrol(grid);
            visited.add(Movement.from(guard));
        }
        return visited;
    }

    // Do the walk for part 1, but for each step check cycles if an obstacle were introduced
    private Set<Point> walkWithCycleCheck(Point start, List<String> input) {
        var guard = new Guard(start, "N");
        var visited = new HashSet<Movement>();
        var cycles = new HashSet<Point>();
        var grid = new GridWithObstacle(input, NO_NEW_OBSTACLE);
        visited.add(Movement.from(guard));
        while (!grid.outOfBounds(guard.at()) && !grid.outOfBounds(guard.nextMove())) {
            guard = guard.patrol(grid);
            var move = Movement.from(guard);
            if(!visited.contains(move)) {
                visited.add(move);
                cycles.add(checkForCycle(move, input, new HashSet<>(visited)));
            }
        }
        return cycles;
    }

    private Point checkForCycle(Movement toCheck, List<String> input, Set<Movement> visited) {
        var simulatedGuard = new Guard(toCheck.p(), toCheck.facing());
        var obstacleAt = simulatedGuard.nextMove();
        var grid = new GridWithObstacle(input, obstacleAt);
        var walkedNodes = visited.stream().map(Movement::p).collect(Collectors.toSet());
        // OOB or existing obstacle on the next move
        if (grid.outOfBounds(obstacleAt) || walkedNodes.contains(obstacleAt) || grid.get(obstacleAt).equals("#") || grid.get(obstacleAt).equals("^")) {
            return NO_CYCLE;
        }

        while (!grid.outOfBounds(simulatedGuard.at()) && !grid.outOfBounds(simulatedGuard.nextMove())) {
            simulatedGuard = simulatedGuard.patrol(grid);
            var next = Movement.from(simulatedGuard);
            if (visited.contains(next)) {
                return obstacleAt;
            }
            visited.add(next);
        }
        return NO_CYCLE;
    }

    private record Guard(Point at, String facing) {

        private Guard patrol(GridWithObstacle grid) {
            var next = nextMove();
            var look = grid.check(next);
            if (look.equals("#")) {
                return new Guard(this.at, turn(this.facing));
            } else {
                return new Guard(next, this.facing);
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

        private Point nextMove() {
            return switch (facing) {
                case "N" -> at.move(0, -1);
                case "E" -> at.move(1, 0);
                case "S" -> at.move(0, 1);
                case "W" -> at.move(-1, 0);
                default -> throw new IllegalStateException("Cant face :" + facing);
            };
        }
    }

    private record Movement(Point p, String facing){
        public static Movement from(Guard guard) {
            return new Movement(guard.at(), guard.facing());
        }
    }

    public static class GridWithObstacle extends Grid {
        private final Point obstacle;
        public GridWithObstacle(List<String> input, Point injectedObstacle) {
            super(input);
            this.obstacle = injectedObstacle;
        }

        private String check(Point p) {
            if (p.equals(obstacle)) {
                return "#";
            }
            return this.get(p, false);
        }
    }
}
