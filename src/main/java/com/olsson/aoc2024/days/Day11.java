package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.Arrays;
import java.util.HashMap;

public class Day11 implements Day {
    @Override
    public String part1() {
        return "" + part1("11");
    }

    @Override
    public String part2() {
        return "" + part2("11");
    }

    public long part1(String day) {
        return iterate(day, 25);
    }

    public long part2(String day) {
        return iterate(day, 75);
    }

    private long iterate(String day, int times) {
        var input = InputUtils.getLines(day);
        var transformer = StoneTransformations.instance();
        return Arrays.stream(input.getFirst().split(" "))
                .map(stone -> transformer.transform(stone, times))
                .reduce(Long::sum).orElse(-1L);
    }

    // Transformer with a cache to keep track of how many stones a certain stone will transform into based on amount of blinks left
    private record StoneTransformations(HashMap<Key, Long> cache) {

        static StoneTransformations instance() {
            return new StoneTransformations(new HashMap<>());
        }

        private Long transform(String stone, int blinks) {
            // If no more blinks, we keep the current stone
            if (blinks == 0) {
                return 1L;
            }
            var current = new Key(stone, blinks);
            var cached = cache.get(current);
            if (cached != null) {
                return cached;
            } else {
                long result;
                if (stone.equals("0")) {
                    result = transform("1", blinks - 1);
                } else if (stone.length() % 2 == 0) {
                    var first = transform(stone.substring(0, stone.length() / 2), blinks - 1);
                    var second = transform(String.valueOf(Long.parseLong(stone.substring(stone.length() / 2))), blinks - 1);
                    result = first + second;
                } else {
                    result = transform(String.valueOf(Long.parseLong(stone) * 2024), blinks - 1);
                }
                cache.put(current, result);
                return result;
            }
        }
    }

    private record Key(String stone, int blinksLeft){}
}
