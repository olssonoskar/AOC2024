package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.MutableGrid;
import com.olsson.aoc2024.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Day15 implements Day {

    @Override
    public String part1() {
        return "" + part1("15");
    }

    @Override
    public String part2() {
        return "" + part2("15");
    }

    public long part1(String day) {
        // When moving boxes, check end of the line to see if spot open, if so move box there and then set robot at next pos
        // if no free space, nothing moves
        var input = InputUtils.getLines(day);
        var grid = new MutableGrid(input.stream().takeWhile(line -> !line.isBlank()).toList());
        var commands = input.stream().dropWhile(line -> !line.isBlank()).skip(1).reduce(String::concat).orElse("");
        var bot = grid.find("@");

        for (int i = 0; i < commands.length(); i++) {
            var change = changeDirection(commands.charAt(i));
            bot = moveBotAndBoxes(grid, bot, change);
        }
        return gpsSummary(grid, "O");
    }

    public long part2(String day) {
        var input = InputUtils.getLines(day);
        var scaled = input.stream()
                .takeWhile(line -> !line.isBlank())
                .map(this::scaleRoom)
                .toList();
        var grid = new MutableGrid(scaled);
        var commands = input.stream()
                .dropWhile(line -> !line.isBlank()).skip(1)
                .reduce(String::concat).orElse("");
        var bot = grid.find("@");

        for (int i = 0; i < commands.length(); i++) {
            var change = changeDirection(commands.charAt(i));
            var changingCrates = canBeMoved(grid, bot, change);
            moveAll(grid, changingCrates, change);
            bot = moveBot(grid, bot, change, !changingCrates.isEmpty());
        }
        return gpsSummary(grid, "[");
    }

    public long gpsSummary(MutableGrid grid, String sumSign) {
        long sum = 0;
        for (int i = 0; i < grid.height(); i++) {
            for (int j = 0; j < grid.width(); j++) {
                if (grid.get(new Point(j, i)).equals(sumSign)) {
                    sum += j + (100L * i);
                }
            }
        }
        return sum;
    }

    public String scaleRoom(String line) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '#' -> builder.append("##");
                case 'O' -> builder.append("[]");
                case '.' -> builder.append("..");
                case '@' -> builder.append("@.");
                default -> throw new IllegalStateException("This should not be part of the input");
            }
        }
        return builder.toString();
    }

    private Point changeDirection(char command) {
        return switch (command) {
            case '>' -> new Point(1, 0);
            case '<' -> new Point(-1, 0);
            case '^' -> new Point(0, -1);
            case 'v' -> new Point(0, 1);
            default -> throw new IllegalArgumentException("Not allowed to move in specified direction:" + command);
        };
    }

    private Point moveBotAndBoxes(MutableGrid grid, Point bot, Point change) {
        var adjacent = bot.move(change);
        var onAdjacent = grid.get(adjacent);
        if (onAdjacent.equals("#")) {
            return bot;
        } else if (onAdjacent.equals(".")) {
            grid.write(adjacent, "@");
            grid.write(bot, ".");
            return adjacent;
        }
        // Obstacle
        Point nextPos = adjacent.move(change);
        String onFloor = grid.getOrDefault(nextPos, "#");
        while (onFloor.equals("O") || onFloor.equals(".")) {
            if (onFloor.equals(".")) {
                grid.write(nextPos, "O");
                grid.write(adjacent, "@");
                grid.write(bot, ".");
                return adjacent;
            }
            if (grid.outOfBounds(nextPos)) {
                return bot;
            }
            nextPos = nextPos.move(change);
            onFloor = grid.getOrDefault(nextPos, "#");
        }
        return bot;
    }

    private List<Point> canBeMoved(MutableGrid grid, Point bot, Point change) {
        var toBePushed = new ArrayList<Point>();
        var pushed = new HashSet<Point>();
        pushed.add(NO_CRATE);
        var queue = new ArrayDeque<Point>();
        queue.add(bot.move(change));

        var adjacent = grid.get(bot.move(change));
        if (adjacent.equals(".") || adjacent.equals("#")) {
            return Collections.emptyList();
        }
        // Add current and queue next/adjacent, if we ever hit a wall the boxes cant move
        while (!queue.isEmpty()) {
            var current = queue.pop();
            if (pushed.contains(current)) {
                continue;
            }
            pushed.add(current);
            queue.add(addHalfIfVertical(grid, current, change));
            var next = current.move(change);
            var nextFloor = grid.get(next);
            if (nextFloor.equals("#")) {
                return Collections.emptyList();
            } else if ("[]".contains(nextFloor)) {
                queue.add(next);
            }
            toBePushed.add(current);
        }
        return toBePushed;
    }

    private void moveAll(MutableGrid grid, List<Point> points, Point change) {
        points.reversed()
                .forEach(p -> {
                    var moving = grid.get(p);
                    grid.write(p.move(change), moving);
                    grid.write(p, ".");
                });
    }

    private Point addHalfIfVertical(MutableGrid grid, Point current, Point change) {
        String onFloor = grid.getOrDefault(current, "#");
        if (verticalPush(change)) {
            return switch (onFloor) {
                case "[" -> current.move(1, 0);
                case "]" -> current.move(-1, 0);
                default -> NO_CRATE;
            };
        }
        return NO_CRATE;
    }

    private Point moveBot(MutableGrid grid, Point bot, Point change, boolean movingCrates) {
        var next = bot.move(change);
        if (grid.get(next).equals(".") || movingCrates) {
            grid.write(next, "@");
            grid.write(bot, ".");
            return next;
        }
        return bot;
    }

    private boolean verticalPush(Point p) {
        return p.x() == 0 && p.y() != 0;
    }

    private static final Point NO_CRATE = new Point(-1, -1);
}
