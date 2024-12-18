package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.Arrays;
import java.util.List;


public class Day17 implements Day {
    @Override
    public String part1() {
        return part1("17");
    }

    @Override
    public String part2() {
        return "" + part2("17");
    }

    public String part1(String day) {
        var input = InputUtils.getLines(day);
        var regs = input.stream()
                .limit(3)
                .map(line -> line.split(":")[1].trim())
                .map(Integer::parseInt)
                .toList();
        var computer = new ThreeBit(regs.getFirst(), regs.get(1), regs.getLast());
        var program = program(input.getLast());
        computer.run(program);
        return computer.stdOut;
    }

    public int part2(String day) {
        var input = InputUtils.getLines(day);
        var program = program(input.getLast());
        String output = "";
        int attempt = Integer.MAX_VALUE / 2;
        while(!output.equals(program.toString())) {
            var computer = new ThreeBit(attempt, 0, 0);
            computer.run(program);
            output = "[" + computer.stdOut + "]";
            System.out.println(output);
            attempt += 1;
        }
        return attempt - 1;
    }

    public List<Integer> program(String input) {
        return Arrays.stream(input.split(":")[1].trim().split(","))
                .map(Integer::parseInt)
                .toList();
    }

    private static class ThreeBit {

        int regA;
        int regB;
        int regC;
        int pc = 0;
        String stdOut = "";

        ThreeBit(int a, int b, int c) {
            this.regA = a;
            this.regB = b;
            this.regC = c;
        }

        void run(List<Integer> program) {
            pc = 0;
            while(pc < program.size()) {
                var jumpCheck = pc;
                execute(program.get(pc), program.get(pc + 1));
                if (jumpCheck == pc) {
                    pc += 2;
                }
            }
        }

        void execute(int op, int arg) {
            switch (op) {
                case 0 -> adv(combo(arg));
                case 1 -> bxl(arg);
                case 2 -> bst(combo(arg));
                case 3 -> jnz(arg);
                case 4 -> bxc();
                case 5 -> out(combo(arg));
                case 6 -> bdv(combo(arg));
                case 7 -> cdv(combo(arg));
                default -> throw new IllegalArgumentException("CRASH: Faulty op" + op);
            }
        }

        int combo(int operand) {
            if (operand < 4) {
                return operand;
            }
            return switch (operand) {
                case 4 -> regA;
                case 5 -> regB;
                case 6 -> regC;
                default -> throw new IllegalArgumentException("CRASH: Faulty operand" + operand);
            };
        }

        void adv(int combo) {
            regA = divA(combo);
        }

        void bxl(int in) {
            regB = regB ^ in;
        }

        void bst(int combo) {
            regB = combo % 8;
        }

        void jnz(int combo) {
            if (regA == 0) {
                return;
            }
            this.pc = combo;
        }

        void bxc() {
            regB = regB ^ regC;
        }

        void out(int combo) {
            if (!stdOut.isEmpty()) {
                stdOut += (", ");
            }
            stdOut += (combo % 8);
        }

        void bdv(int combo) {
            regB = divA(combo);
        }

        void cdv(int combo) {
            regC = divA(combo);
        }

        int divA(int combo) {
            return (int)(regA / Math.pow(2, combo));
        }
    }
}
