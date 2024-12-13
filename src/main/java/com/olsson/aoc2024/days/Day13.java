package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.ArrayList;

public class Day13 implements Day {

    int pow = 13;

    @Override
    public String part1() {
        return "" + solveSystem("13", false);
    }

    @Override
    public String part2() {
        return "" + solveSystem("13", true);
    }

    // Initially tried cheap button and then added expensive, but wouldn't work for pt 2
    // instead - Two linear equations with two unknowns
    // x button presses on A and y button presses on B that results in x y movement
    // xAx + yBx = goalX
    // xAy + yBx = goalY
    // x = (goalX - yBx) / Ax
    // ... substitute and solve for y, solved on paper
    // y = (goalY - (goalX/Ax) * Ay) / (By - (Bx/Ax) * Ay)
    public long solveSystem(String day, boolean part2) {
        var input = InputUtils.getLines(day);
        var results = new ArrayList<Presses>();
        for (int i = 0; i <= input.size(); i += 4) {
            var btnA = ButtonPress.from(input.get(i));
            var btnB = ButtonPress.from(input.get(i + 1));
            var goal = input.get(i + 2).split("=");
            long goalX = Long.parseLong(goal[1].substring(0, goal[1].length() - 3));
            long goalY = Long.parseLong(goal[2]);

            if (part2) {
                goalX += 10000000000000L;
                goalY += 10000000000000L;
            }

            long y = Math.round(((goalY - btnA.y * ((double) goalX / btnA.x)) / (btnB.y - ((double) btnB.x / btnA.x) * btnA.y)));
            long x = Math.round((double) (goalX - y * btnB.x) / btnA.x);

            var actualX = x * btnA.x + y * btnB.x;
            var actualY = x * btnA.y + y * btnB.y;

            if (actualX == goalX && actualY == goalY) {
                results.add(new Presses(x, y));
            } else {
                results.add(new Presses(0, 0));
            }
        }
        return results.stream()
                .filter(result -> result.btnA != 0 && result.btnB != 0 )
                .map(result -> result.btnA * 3 + result.btnB)
                .reduce(Long::sum).orElse(-1L);
    }


    private record ButtonPress(int x, int y){
        static ButtonPress from(String input) {
            var split = input.split(" ");
            var x = Integer.parseInt(split[2].substring(2, split[0].length() - 2));
            var y = Integer.parseInt(split[3].substring(2));
            return new ButtonPress(x, y);
        }
    }

    private record Presses(long btnA, long btnB){}
}
