package com.olsson.aoc2024;

import com.olsson.aoc2024.days.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static java.io.IO.println;

public class Runner {

    private void allDays() {
        var total = Instant.now();
        var solutions = List.of(
                new Solutions(1, new Day01()),
                new Solutions(2, new Day02()),
                new Solutions(3, new Day03()),
                new Solutions(4, new Day04()),
                new Solutions(5, new Day05()),
                new Solutions(6, new Day06()),
                new Solutions(7, new Day07()),
                new Solutions(8, new Day08()),
                new Solutions(9, new Day09()),
                new Solutions(10, new Day10()),
                new Solutions(11, new Day11()),
                new Solutions(12, new Day12()),
                new Solutions(13, new Day13()),
                new Solutions(14, new Day14()),
                new Solutions(15, new Day15()),
                new Solutions(16, new Day16()),
                new Solutions(17, new Day17())
        );
        solutions.forEach(sol -> {
            var start = Instant.now();
            println("Day " + sol.num);
            println(sol.day.result());
            println("Took " + Duration.between(start, Instant.now()).toMillis() + "ms");
        });
        println("Total time took " + Duration.between(total, Instant.now()).toMillis() + "ms");
    }

    void main() {
        new Runner().allDays();
    }

    private record Solutions(int num, Day day) { }
}