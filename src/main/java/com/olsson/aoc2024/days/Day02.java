package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day02 implements Day{
    @Override
    public String part1() {
        return "" + part1("2");
    }

    @Override
    public String part2() {
        return "" + part2("2");
    }

    public long part1(String day) {
        return InputUtils.getLines(day).stream()
                .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).toList())
                .filter(this::isSafe)
                .count();
    }

    public long part2(String day) {
        return InputUtils.getLines(day).stream()
                .map(line -> Arrays.stream(line.split(" ")).map(Integer::parseInt).toList())
                .filter(this::isSafWithDampener)
                .count();
    }

    private boolean isSafe(List<Integer> report) {
        var isInc = report.getFirst() < report.getLast();
        for (int i = 0; i < report.size() - 1; i++) {
            if (isInc && invalidIncrease(report.get(i + 1), report.get(i)) ||
                    !isInc && invalidDecrease(report.get(i + 1), report.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafWithDampener(List<Integer> report) {
        if (isSafe(report)) {
            return true;
        } else {
            // Test every combination where 1 level is removed and see if it passes as valid
            return IntStream.range(0, report.size())
                    .mapToObj(num -> copy(report, num))
                    .map(this::isSafe)
                    .filter(it -> it)
                    .findAny().orElse(false);
        }
    }

    private List<Integer> copy(List<Integer> org, int skip) {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < org.size(); i++) {
            if (i != skip) {
                list.add(org.get(i));
            }
        }
        return list;
    }

    private boolean invalidIncrease(int next, int previous) {
        return next < previous + 1 || next > previous + 3;
    }

    private boolean invalidDecrease(int next, int previous) {
        return next > previous - 1 || next < previous - 3;
    }
}