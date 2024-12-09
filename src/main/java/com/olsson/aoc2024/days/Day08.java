package com.olsson.aoc2024.days;

import com.olsson.aoc2024.Grid;
import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Day08 implements Day {

    @Override
    public String part1() {
        return "" + part1("8");
    }

    @Override
    public String part2() {
        return "" + part2("8");
    }

    public long part1(String day) {
        var grid = new Grid(InputUtils.getLines(day));
        var antennas = collectAntennas(grid);
        var result = antinodes(antennas, grid, this::antinodeLocation);
        return result.size();
    }

    public long part2(String day) {
        var grid = new Grid(InputUtils.getLines(day));
        var antennas = collectAntennas(grid);
        var result = antinodes(antennas, grid, this::allAntinodeLocation);
        // Antennas in line with 2 other antennas are also counted as antinodes, which seems to be all of them?
        var antennaLocations = antennas.values().stream().flatMap(Collection::stream);
        return Stream.concat(result.stream(), antennaLocations).distinct().count();
    }

    private Map<String, List<Point>> collectAntennas(Grid grid) {
        var allAntennas = new HashMap<String, List<Point>>();
        for (int i = 0; i < grid.height(); i++) {
            for (int j = 0; j < grid.width(); j++) {
                var antenna = grid.get(new Point(j, i), false);
                if (!antenna.equals(".")) {
                    allAntennas.computeIfAbsent(antenna, _ -> new ArrayList<>());
                    allAntennas.get(antenna).add(new Point(j, i));
                }
            }
        }
        return allAntennas;
    }

    // For every antenna group, check every antenna pair and generate antinodes based on the provided function
    private Set<Point> antinodes(Map<String, List<Point>> antennas, Grid grid, TriFunction<Point, Point, Grid, Set<Point>> nodeLocations) {
        var antinodes = new HashSet<Point>();
        for(List<Point> antennaGroup : antennas.values()) {
            for (int i = 0; i < antennaGroup.size() - 1; i++) {
                for (int j = i + 1; j < antennaGroup.size(); j++) {
                    var p1 = antennaGroup.get(i);
                    var p2 = antennaGroup.get(j);
                    antinodes.addAll(nodeLocations.apply(p1, p2, grid));
                    antinodes.addAll(nodeLocations.apply(p2, p1, grid));
                }
            }
        }
        return antinodes;
    }

    // This will yield the first antinode point behind 'first' on the line between first and second
    private Set<Point> antinodeLocation(Point first, Point second, Grid grid) {
        var diffX = first.x() - second.x();
        var diffY = first.y() - second.y();
        var antinode = first.move(diffX, diffY);
        if (!grid.outOfBounds(antinode)) {
            return Set.of(antinode);
        }
        return Collections.emptySet();
    }

    // This will yield all the antinode points behind 'first' on the line between first and second within the grid
    private Set<Point> allAntinodeLocation(Point first, Point second, Grid grid) {
        var allAntinodes = new HashSet<Point>();
        var diffX = first.x() - second.x();
        var diffY = first.y() - second.y();
        // Continue until the candidate is outside the grid to yield all points
        for (int i = 1;; i++) {
            var candidate = first.move(diffX * i, diffY * i);
            if (grid.outOfBounds(candidate)) {
                return allAntinodes;
            }
            allAntinodes.add(candidate);
        }
    }

    @FunctionalInterface
    public interface TriFunction<T1, T2, U, R> {
        R apply(T1 t1, T2 t2, U u);
    }
}
