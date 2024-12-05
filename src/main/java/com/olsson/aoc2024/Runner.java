package com.olsson.aoc2024;

import com.olsson.aoc2024.days.*;

import java.util.List;

import static java.io.IO.println;

public class Runner {

    private void allDays() {
        var solutions = List.of(
                new Solutions(1, new Day01()),
                new Solutions(2, new Day02()),
                new Solutions(3, new Day03()),
                new Solutions(4, new Day04()),
                new Solutions(5, new Day05())
        );
        solutions.forEach(sol -> {
            println("Day " + sol.num);
            println(sol.day.result());
        });
    }

    void main() {
        new Runner().allDays();
    }

    private record Solutions(int num, Day day) { }
}