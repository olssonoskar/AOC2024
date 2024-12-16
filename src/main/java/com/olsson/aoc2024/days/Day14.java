package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class Day14 implements Day {
    @Override
    public String part1() {
        return "" + part1("14", 100, 101, 103);
    }

    @Override
    public String part2() {
        return "" + part2("14", 101, 103);
    }

    public long part1(String day, int rounds, int width, int height) {
        var bots = setup(InputUtils.getLines(day));
        var simulated = bots.stream()
                .map(bot -> bot.move(rounds, width, height))
                .collect(groupingBy(bot -> quadGroup(bot, width, height)));
        return simulated.entrySet().stream()
                .filter(entry -> entry.getKey() != -1)
                .map(entry -> entry.getValue().size())
                .reduce((a, b) -> a * b).orElse(-1);
    }

    /*
     We could have printed out the layouts and found the pattern looking like an Xmas tree (since we didn't know beforehand)
     However, as per assumption made by Todd here https://todd.ginsberg.com/post/advent-of-code/2024/day14/
     the solution is the initial image and does not have any overlapping robots, so we can found the first such state instead
     */
    public long part2(String day, int width, int height) {
        var bots = setup(InputUtils.getLines(day));
        for (int i = 0; i < 20_000; i ++) {
            bots = bots.stream()
                    .map(bot -> bot.move(1, width, height))
                    .toList();
            var distinctPos = bots.stream().map(Bot::position).distinct().count();
            if (distinctPos == bots.size()) {
                debug(width, height, bots);
                return i + 1L;
            }
        }
        return -1;
    }

    private record Bot(Point position, Point velocity) {
        Bot move(int rounds, int width, int height) {
            var xDiff = velocity.x() * rounds;
            var yDiff = velocity.y() * rounds;
            return new Bot(new Point(
                    Math.floorMod(this.position.x() + xDiff, width),
                    Math.floorMod(this.position.y() + yDiff, height)),
                    this.velocity);
        }

        static Bot of(String x, String y, String velX, String velY) {
            return new Bot(
                    new Point(
                            Integer.parseInt(x),
                            Integer.parseInt(y)),
                    new Point(
                            Integer.parseInt(velX),
                            Integer.parseInt(velY)));
        }
    }

    private int quadGroup(Bot bot, int width, int height) {
        var middle = new Point(width / 2, height / 2);
        if (bot.position.x() == middle.x() || bot.position.y() == middle.y()) {
            return -1;
        }
        var left = bot.position.x() < middle.x();
        if (bot.position.y() < middle.y()) {
            return left ? 1 : 2;
        }
        return left ? 3 : 4;
    }

    private List<Bot> setup(List<String> input) {
        return input.stream()
                .map(line -> {
                    var params = line.split(" ");
                    var pos = params[0].substring(2).split(",");
                    var velocity = params[1].substring(2).split(",");
                    return Bot.of(pos[0], pos[1], velocity[0], velocity[1]);
                }).toList();
    }

    private void debug(int width, int height, List<Bot> bots) {
        for (int i = 0; i < height; i++) {
            var y = i;
            var line = new StringBuilder();
            for (int j = 0; j < width; j++) {
                var x = j;
                var result = bots.stream()
                        .filter(bot -> bot.position.x() == x && bot.position.y() == y)
                        .count();
                if (result == 0) {
                    line.append(".");
                } else {
                    line.append(result);
                }
            }
            System.out.println(line);
        }
        System.out.println(" ");
    }
}
