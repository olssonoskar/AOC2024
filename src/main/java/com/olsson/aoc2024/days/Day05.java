package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Day05 implements Day {
    @Override
    public String part1() {
        return "" + part1("5");
    }

    @Override
    public String part2() {
        return "" + part2("5");
    }

    public long part1(String day) {
        var input = InputUtils.getLines(day);
        var ordering = new Ordering(input);
        var correctOrder = allUpdates(input)
                .filter(update -> ordered(update, ordering))
                .toList();
        return sumMiddleElement(correctOrder);
    }

    public long part2(String day) {
        var input = InputUtils.getLines(day);
        var ordering = new Ordering(input);
        var corrections = allUpdates(input)
                .filter(update -> !ordered(update, ordering))
                .map(update -> correctOrder(update, ordering))
                .toList();
        return sumMiddleElement(corrections);
    }

    private boolean ordered(List<String> update, Ordering ordering) {
        return update.parallelStream()
                .allMatch(page -> pageInOrder(page, update, ordering));
    }

    private boolean pageInOrder(String page, List<String> update, Ordering ordering) {
        var order = ordering.from(page);
        var pageIndex = update.indexOf(page);
        return update.stream()
                .filter(order::contains)
                .noneMatch(dependency -> update.indexOf(dependency) < pageIndex);
    }

    // Insert page when all of its present dependencies are in (if none we can insert instantly)
    // This method will never return for an invalid combination since we try until all inserts have been made
    private List<String> correctOrder(List<String> update, Ordering ordering) {
        var correctOrder = new ArrayList<String>();
        while (correctOrder.size() != update.size()) {
            for (String candidate : update) {
                if (correctOrder.contains(candidate)) {
                    continue;
                }
                var orderForPage = ordering.from(candidate);
                var dependencies = update.stream().filter(orderForPage::contains).toList();
                if (orderForPage.isEmpty() || correctOrder.containsAll(dependencies)) {
                    correctOrder.add(candidate);
                }
            }
        }
        return correctOrder;
    }

    private long sumMiddleElement(List<List<String>> updates) {
        return updates.stream()
                .map(update -> update.get(Math.floorDiv(update.size(), 2)))
                .map(Long::parseLong)
                .reduce(Long::sum).orElse(0L);
    }

    private Stream<List<String>> allUpdates(List<String> input) {
        return input.stream()
                .dropWhile(row -> !row.isBlank())
                .skip(1)
                .map(row -> Arrays.stream(row.split(",")).toList());
    }

    private static class Ordering {

        private final Map<String, Set<String>> requirements;

        Ordering(List<String> rules) {
            this.requirements = ordering(rules);
        }

        private Map<String, Set<String>> ordering(List<String> rules) {
            var pageOrdering = new HashMap<String, Set<String>>();
            rules.stream()
                    .takeWhile(row -> !row.isBlank())
                    .forEach(rule -> {
                        var params = rule.split("\\|");
                        pageOrdering.computeIfAbsent(params[0], _ -> new HashSet<>());
                        pageOrdering.get(params[0]).add(params[1]);
                    });
            return pageOrdering;
        }

        private Set<String> from(String page) {
            return requirements.getOrDefault(page, Collections.emptySet());
        }
    }

}
