package com.olsson.aoc2024.days;

public interface Day {

    String part1();
    String part2();

    default String result() {
        return "Part1: " + part1() + System.lineSeparator() + "Part2: " + part2();
    }
}
