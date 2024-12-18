package com.olsson.aoc2024.days;

import com.olsson.aoc2024.Grid;
import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Day16 implements Day {

    @Override
    public String part1() {
        return "" + part1("16");
    }

    @Override
    public String part2() {
        return "" + part2("16");
    }

    public long part1(String day) {
        var labyrinth = new Grid(InputUtils.getLines(day));
        var path = findBestPath(labyrinth);
        // Steps include goal, exclude from result
        return path - 1;
    }

    public long part2(String day) {
        var labyrinth = new Grid(InputUtils.getLines(day));
        var path = findAllPaths(labyrinth);
        // Steps include goal, exclude from result
        return path.stream().flatMap(p -> p.steps.stream()).distinct().count();
    }

    private long findBestPath(Grid grid) {
        var pathCost = new HashMap<Key, Integer>();
        var queue = new PriorityQueue<>(lowScore);
        var start = grid.find("S");
        queue.add(Path.start(start));

        while (!queue.isEmpty()) {
            var current = queue.poll();
            var position = current.steps().getLast();
            var cost = current.steps.size() + current.turns.size() * 1000;
            if (grid.get(position).equals("E")) {
                return cost;
            } else if (pathCost.getOrDefault(current.key(), Integer.MAX_VALUE) >= cost) {
                pathCost.put(current.key(), cost);
                var neighbors = grid.adjacentNeighbors(position).stream()
                        .filter(point -> !grid.get(point).equals("#"))
                        .filter(point -> !current.steps().contains(point))
                        .toList();
                neighbors.forEach(point -> queue.add(current.extend(point)));
            }
        }
        return -1;
    }

    private List<Path> findAllPaths(Grid grid) {
        int min = Integer.MAX_VALUE;
        var goalPath = new ArrayList<Path>();
        var pathCost = new HashMap<Key, Integer>();
        var queue = new PriorityQueue<>(lowScore);
        var start = grid.find("S");
        queue.add(Path.start(start));

        while (!queue.isEmpty()) {
            var current = queue.poll();
            var position = current.steps().getLast();
            var cost = current.steps.size() + current.turns.size() * 1000;
            if (grid.get(position).equals("E") && cost <= min) {
                min = cost;
                goalPath.add(current);
            } else if (pathCost.getOrDefault(current.key(), Integer.MAX_VALUE) >= cost && cost <= min) {
                pathCost.put(current.key(), cost);
                var neighbors = grid.adjacentNeighbors(position).stream()
                        .filter(point -> !grid.get(point).equals("#"))
                        .filter(point -> !current.steps().contains(point))
                        .toList();
                neighbors.forEach(point -> queue.add(current.extend(point)));
            }
        }
        return goalPath;
    }

    private record Path(List<Point> steps, List<Point> turns, String direction) {
        static Path start(Point start) {
            var steps = new ArrayList<>(List.of(start));
            return new Path(steps, new ArrayList<>(), "E");
        }

        Key key() {
            return new Key(steps.getLast(), direction);
        }

        Path extend(Point next) {
            var currentPos = this.steps.getLast();
            var diff = new Point(currentPos.x() - next.x(), currentPos.y() - next.y());
            var nextDir = directionOf(diff);
            if (this.direction.equals(nextDir)) {
                return this.step(next);
            }
            return this.turn(nextDir, next);
        }

        private String directionOf(Point diff) {
            if (diff.x() == 0 && diff.y() == -1) {
                return "N";
            } else if (diff.x() == 1 && diff.y() == 0) {
                return "E";
            } else if (diff.x() == 0 && diff.y() == 1) {
                return "S";
            } else if (diff.x() == -1 && diff.y() == 0) {
                return "W";
            } else {
                throw new IllegalArgumentException("Not a valid diff");
            }
        }

        Path step(Point next) {
            var steps = new ArrayList<>(this.steps.stream().toList());
            steps.add(next);
            var turns = new ArrayList<>(this.turns.stream().toList());
            return new Path(
                    steps,
                    turns,
                    this.direction
            );
        }

        Path turn(String direction, Point at) {
            var steps = new ArrayList<>(this.steps.stream().toList());
            steps.add(at);
            var turns = new ArrayList<>(this.turns.stream().toList());
            turns.add(at);
            return new Path(
                    steps,
                    turns,
                    direction
            );
        }
    }

    private record Key(Point p, String dir){}

    Comparator<Path> lowScore = Comparator.comparingLong(o -> o.steps.size() + o.turns.size() * 1000L);
}
