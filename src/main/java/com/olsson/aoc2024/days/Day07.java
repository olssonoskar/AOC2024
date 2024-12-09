package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day07 implements Day {


    @Override
    public String part1() {
        return "" + part1("7");
    }

    @Override
    public String part2() {
        return "" + part2("7");
    }

    public long part1(String day) {
        var input = InputUtils.getLines(day);
        return runCalibrationWith(input, this::combine);
    }

    public long part2(String day){
        var input = InputUtils.getLines(day);
        return runCalibrationWith(input, this::combinePart2);
    }

    private long runCalibrationWith(List<String> input, Function<Calibration, Long> tester) {
        return input.stream()
                .map(this::setup)
                .map(tester)
                .filter(positive -> positive > 0)
                .reduce(Long::sum).orElse(0L);
    }

    private Calibration setup(String line) {
        var split = line.split(":");
        var numbers = Arrays.stream(split[1].trim().split(" "))
                .map(Long::parseLong)
                .toList();
        return new Calibration(numbers, Long.parseLong(split[0]));
    }

    // Generate all combinations by inserting the next number added/multiplied for all previous ones
    // Find any matching the goal and return if found
    private long combine(Calibration calibration) {
        List<Long> sums = new ArrayList<>();
        sums.add(calibration.numbers.getFirst());
        for (int i = 1; i < calibration.numbers.size(); i++) {
            var next = calibration.numbers.get(i);
            var mult = sums.stream().map(prod -> prod * next);
            var add = sums.stream().map(term -> term + next);
            sums = Stream.concat(add, mult).toList();
        }
        return sums.stream()
                .filter(any -> any.equals(calibration.goal))
                .findFirst().orElse(-1L);
    }

    // Same as part1 but introduce a new operator that concat two numbers
    // concat 12 345 -> 12345
    private long combinePart2(Calibration calibration) {
        List<Long> sums = new ArrayList<>();
        sums.add(calibration.numbers.getFirst());
        for (int i = 1; i < calibration.numbers.size(); i++) {
            var next = calibration.numbers.get(i);
            var mult = sums.stream().map(prod -> prod * next);
            var add = sums.stream().map(term -> term + next);
            var concat = sums.stream().map(term -> Long.parseLong(term.toString() + next));
            sums = Stream.concat(Stream.concat(add, mult), concat).toList();
        }
        return sums.stream()
                .filter(any -> any.equals(calibration.goal))
                .findFirst().orElse(-1L);
    }

    private record Calibration(List<Long> numbers, long goal){}
}
