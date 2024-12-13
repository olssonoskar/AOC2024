package com.olsson.aoc2024.days;

import com.olsson.aoc2024.Grid;
import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day12 implements Day {
    @Override
    public String part1() {
        return "" + part1("12");
    }

    @Override
    public String part2() {
        return "" + part2("12");
    }

    public long part1(String day) {
        var grid = new Grid(InputUtils.getLines(day));
        var regions = scanField(grid);
        return regions.stream()
                .map(region -> region.perimeter.stream().reduce(Long::sum).orElse(-1L) * region.plots.size())
                .reduce(Long::sum).orElse(-1L);
    }

    public long part2(String day) {
        var grid = new Grid(InputUtils.getLines(day));
        var regions = scanField(grid);
        return regions.stream()
                .map(region -> region.corners.stream().reduce(Long::sum).orElse(-1L) * region.plots.size())
                .reduce(Long::sum).orElse(-1L);
    }

    private List<Region> scanField(Grid grid) {
        var regions = new ArrayList<Region>();
        var visited = new HashSet<Point>();
        for (int i = 0; i < grid.height(); i++) {
            for (int j = 0; j < grid.width(); j++) {
                var p = new Point(j, i);
                if (!visited.contains(p)) {
                    var next = region(p, grid);
                    visited.addAll(next.plots);
                    regions.add(next);
                }
            }
        }
        return regions;
    }

    private Region region(Point p, Grid grid) {
        var toExplore = new ArrayDeque<Point>();
        var region = Region.ofType(grid.get(p));
        toExplore.add(p);
        while (!toExplore.isEmpty()) {
            var current = toExplore.pop();
            if (region.plots.contains(current)) {
                continue;
            }
            var adjacentOfType = grid.adjacentNeighbors(current).stream()
                    .filter(neighbor -> grid.get(neighbor).equals(region.type))
                    .toList();
            region.perimeter.add(4L - adjacentOfType.size());
            region.plots.add(current);
            region.corners.add(countCorners(region.type, current, grid));
            toExplore.addAll(adjacentOfType);
        }
        return region;
    }

    // Count corner plots, two corners required to form a side, but each is used in two sides, so corners = sides
    private long countCorners(String type, Point p, Grid grid) {
        var north = grid.get(p.move(0, -1));
        var south = grid.get(p.move(0, 1));
        var east = grid.get(p.move(1, 0));
        var west = grid.get(p.move(-1, 0));
        return Stream.of(
                new Corner(north, east, p.move(1, -1)),
                new Corner(south, east, p.move(1, 1)),
                new Corner(north, west, p.move(-1, -1)),
                new Corner(south, west, p.move(-1, 1))
        ).filter(corner ->
                // Corner if the two sides are of a different type or inner corner if they are the same type but diagonal is different
                (!corner.side.equals(type) && !corner.otherSide.equals(type)) ||
                    (corner.side.equals(type) && corner.otherSide.equals(type) && !grid.get(corner.corner).equals(type))
        ).count();
    }

    private record Region(String type, List<Long> perimeter, Set<Point> plots, List<Long> corners){
        static Region ofType(String type) {
            return new Region(type, new ArrayList<>(), new HashSet<>(), new LinkedList<>());
        }
    }

    private record Corner(String side, String otherSide, Point corner) {

    }
}
