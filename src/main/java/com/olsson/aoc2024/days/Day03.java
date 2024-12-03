package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day03 implements Day {
    @Override
    public String part1() {
        return "" + part1("3");
    }

    @Override
    public String part2() {
        return "" + part2("3");
    }

    public long part1(String day) {
        var input = InputUtils.getLines(day);
        return input.stream()
                .map(this::sumMatches)
                .reduce(Long::sum).orElse(0L);
    }

    public long part2(String day) {
        var input = InputUtils.getLines(day);
        ResultWithFlag result = new ResultWithFlag(0, true);
        for (String line : input) {
            result = result.update(toggleSum(line, result.multEnabled));
        }
        return result.sum;
    }

    // Sum the matches of the line, with toggle for do and don't operation
    // Return flag to keep track between lines
    private ResultWithFlag toggleSum(String line, boolean multEnabled) {
        var matches = matchLine(line, multWithDisable()).toList();
        boolean enabled = multEnabled;
        var sum = 0L;
        for (String match: matches) {
            if (match.equals("do()")) {
                enabled = true;
            } else if (match.equals("don't()")) {
                enabled = false;
            } else if (enabled) {
                sum += mult(match);
            }
        }
        return new ResultWithFlag(sum, enabled);
    }

    private long sumMatches(String line) {
        return matchLine(line, mult())
                .map(this::mult)
                .reduce(Long::sum).orElse(0L);
    }

    private Stream<String> matchLine(String line, Pattern pattern) {
        return pattern.matcher(line)
                .results()
                .map(MatchResult::group);
    }

    private long mult(String mult) {
        var params = mult.substring(4, mult.length() - 1).split(",");
        return Long.parseLong(params[0]) * Long.parseLong(params[1]);
    }

    private Pattern mult() {
        return Pattern.compile("mul\\(\\d+,\\d+\\)");
    }

    private Pattern multWithDisable() {
        return Pattern.compile("(mul\\(\\d+,\\d+\\))|(do\\(\\))|(don't\\(\\))");
    }

    private record ResultWithFlag(long sum, boolean multEnabled) {
        public ResultWithFlag update(ResultWithFlag p) {
            return new ResultWithFlag(this.sum + p.sum, p.multEnabled);
        }
    }
}