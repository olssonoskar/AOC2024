package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day01 implements Day{
    @Override
    public String part1() {
        return part1("1");
    }

    @Override
    public String part2() {
        return part2("1");
    }

    public String part1(String day) {
        var input = InputUtils.getLines(day);
        return "" + sortedDiff(input);
    }

    public String part2(String day) {
        var input = InputUtils.getLines(day);
        return "" + similarityScore(input);
    }

    private int similarityScore(List<String> input) {
        var first = collectColumn(input, 0).toList();
        var second = collectColumn(input, 1).collect(Collectors.toMap(k -> k, _ -> 1, Integer::sum));
        return first.stream()
                .map(num -> num * second.getOrDefault(num, 0))
                .reduce(Integer::sum).orElse(-1);
    }

    private int sortedDiff(List<String> input) {
        var first = collectColumn(input, 0).sorted().toList();
        var second = collectColumn(input, 1).sorted().toList();
        return IntStream.range(0, first.size())
                .mapToObj(i -> first.get(i) - second.get(i))
                .map(Math::abs)
                .reduce(Integer::sum).orElse(0);
    }

    private Stream<Integer> collectColumn(List<String> input, int idx) {
        return input.stream().map(line -> line.split(" ".repeat(3)))
                .map(arr -> arr[idx])
                .map(Integer::parseInt);
    }
}
