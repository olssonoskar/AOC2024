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
                new Solutions(7, new Day07())
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