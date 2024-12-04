package com.olsson.aoc2024.days;

import com.olsson.aoc2024.Grid;
import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day04 implements Day{
    @Override
    public String part1() {
        return "" + part1("4") ;
    }

    @Override
    public String part2() {
        return "" + part2("4");
    }

    public long part1(String day) {
        var input = new Grid(InputUtils.getLines(day));
        BiFunction<Grid, Point, Long> counter = this::matchAll;
        return iterate(input, counter);
    }

    public long part2(String day) {
        var input = new Grid(InputUtils.getLines(day));
        BiFunction<Grid, Point, Long> counter = this::matchCross;
        return iterate(input, counter);
    }

    private long iterate(Grid grid, BiFunction<Grid, Point, Long> counter) {
        var sum = 0L;
        for (int i = 0; i < grid.height(); i++) {
            for (int j = 0; j < grid.width(); j++) {
                var p = new Point(j, i);
                sum += counter.apply(grid, p);
            }
        }
        return sum;
    }

    // Find 'XMAS' in row, column and diagonal, matching even if spelled backwards
    private long matchAll(Grid grid, Point p) {
        var init = grid.get(p);
        var downRight = new StringBuilder().append(init);
        var downLeft = new StringBuilder().append(init);
        var forward = new StringBuilder().append(init);
        var down = new StringBuilder().append(init);
        for(int i = 1; i < 4; i++) {
            forward.append(grid.get(p.move(i, 0)));
            down.append(grid.get(p.move(0, -i)));
            downRight.append(grid.get(p.move(i, -i)));
            downLeft.append(grid.get(p.move(-i, -i)));
        }
        return Stream.of(forward.toString(), down.toString(), downRight.toString(), downLeft.toString())
                .filter(isXmas)
                .count();
    }

    private long matchCross(Grid grid, Point p) {
        if (!grid.get(p).equals("A")) {
            return 0;
        }
        var line1 = grid.get(p.move(-1, -1)) + "A" + grid.get(p.move(1, 1)) ;
        var line2 = grid.get(p.move(1, -1)) + "A" + grid.get(p.move(-1, 1));
        // If pattern forms an X with two 'MAS' crossing each other
        return isMas.test(line1) && isMas.test(line2) ? 1 : 0;
    }

    private final Predicate<String> isXmas = xmas -> xmas.equals("XMAS") || xmas.equals("SAMX");
    private final Predicate<String> isMas = xmas -> xmas.equals("MAS") || xmas.equals("SAM");


}
