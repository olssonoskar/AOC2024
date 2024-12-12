package com.olsson.aoc2024.days;

import com.olsson.aoc2024.Grid;
import com.olsson.aoc2024.InputUtils;
import com.olsson.aoc2024.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Day10 implements Day {


    @Override
    public String part1() {
        return "" + part1("10");
    }

    @Override
    public String part2() {
        return "" + part2("10");
    }

    public long part1(String day) {
        var grid = new Grid(InputUtils.getLines(day));
        return grid.allMatching("0").stream()
                .map(Track::start)
                .map(track -> followTrack(track, grid))
                .map(reachable -> reachable.stream().distinct().count())
                .reduce(Long::sum).orElse(-1L);
    }

    public long part2(String day) {
        var grid = new Grid(InputUtils.getLines(day));
        return grid.allMatching("0").stream()
                .map(Track::start)
                .map(track -> followTrack(track, grid))
                .map(reachable -> (long) reachable.size())
                .reduce(Long::sum).orElse(-1L);
    }

    private List<Point> followTrack(Track track, Grid grid) {
        if (track.explore.isEmpty()) {
            return track.reachable;
        }
        var current = track.explore.pop();
        var nextLevel = String.valueOf(Integer.parseInt(current.level) + 1);
        var validPaths = grid.adjacentNeighbors(current.pos).stream()
                .filter(pos -> grid.get(pos).equals(nextLevel));
        if (current.level.equals("8")) {
            validPaths.forEach(track.reachable::add);
        } else {
            validPaths.forEach(pos -> track.explore.push(new PathNode(pos, nextLevel)));
        }
        return followTrack(track, grid);
    }

    private record Track(ArrayDeque<PathNode> explore, List<Point> reachable) {
        static Track start(Point at) {
            var queue = new ArrayDeque<PathNode>();
            queue.add(new PathNode(at, "0"));
            return new Track(queue, new ArrayList<>());
        }
    }

    private record PathNode(Point pos, String level) {
    }
}
