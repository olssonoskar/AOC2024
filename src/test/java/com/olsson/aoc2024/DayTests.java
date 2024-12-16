package com.olsson.aoc2024;

import com.olsson.aoc2024.days.Day01;
import com.olsson.aoc2024.days.Day02;
import com.olsson.aoc2024.days.Day03;
import com.olsson.aoc2024.days.Day04;
import com.olsson.aoc2024.days.Day05;
import com.olsson.aoc2024.days.Day06;
import com.olsson.aoc2024.days.Day07;
import com.olsson.aoc2024.days.Day08;
import com.olsson.aoc2024.days.Day09;
import com.olsson.aoc2024.days.Day10;
import com.olsson.aoc2024.days.Day11;
import com.olsson.aoc2024.days.Day12;
import com.olsson.aoc2024.days.Day13;
import com.olsson.aoc2024.days.Day14;
import com.olsson.aoc2024.days.Day15;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayTests {

    @Test
    void test01_1() {
        assertEquals("11", new Day01().part1("1_1"));
    }

    @Test
    void test01_2() {
        assertEquals("31", new Day01().part2("1_1"));
    }

    @Test
    void test02_1() {
        assertEquals(2, new Day02().part1("2_1"));
    }

    @Test
    void test02_2() {
        assertEquals(4, new Day02().part2("2_1"));
    }

    @Test
    void test03_1() {
        assertEquals(161, new Day03().part1("3_1"));
    }

    @Test
    void test03_2() {
        assertEquals(48, new Day03().part2("3_2"));
    }

    @Test
    void test04_1() {
        assertEquals(18, new Day04().part1("4_1"));
    }

    @Test
    void test04_2() {
        assertEquals(9, new Day04().part2("4_2"));
    }

    @Test
    void test05_1() {
        assertEquals(143, new Day05().part1("5_1"));
    }

    @Test
    void test05_2() {
        assertEquals(123, new Day05().part2("5_1"));
    }

    @Test
    void test06_1() {
        assertEquals(41, new Day06().part1("6_1"));
    }

    @Test
    void test06_2() {
        assertEquals(6, new Day06().part2("6_1"));
    }

    @Test
    void test07_1() {
        assertEquals(3749, new Day07().part1("7_1"));
    }

    @Test
    void test07_2() {
        assertEquals(11387, new Day07().part2("7_1"));
    }

    @Test
    void test08_1() {
        assertEquals(14, new Day08().part1("8_1"));
    }

    @Test
    void test08_2() {
        assertEquals(34, new Day08().part2("8_1"));
    }

    @Test
    void test09_1() {
        assertEquals(1928, new Day09().part1("9_1"));
    }

    @Test
    void test09_2() {
        assertEquals(2858, new Day09().part2("9_1"));
    }

    @Test
    void test10_1() {
        assertEquals(36, new Day10().part1("10_1"));
    }

    @Test
    void test10_2() {
        assertEquals(81, new Day10().part2("10_1"));
    }

    @Test
    void test11_1() {
        assertEquals(55312, new Day11().part1("11_1"));
    }

    @Test
    void test12_1() {
        assertEquals(1930, new Day12().part1("12_1"));
    }

    @Test
    void test12_2() {
        assertEquals(1206, new Day12().part2("12_1"));
    }

    @Test
    void test13_1() {
        assertEquals(480, new Day13().solveSystem("13_1", false));
    }

    @Test
    void test14_1() {
        assertEquals(12, new Day14().part1("14_1", 100, 11, 7));
    }

    @Test
    void test15_11() {
        assertEquals(2028, new Day15().part1("15_1small"));
    }

    @Test
    void test15_12() {
        assertEquals(10092, new Day15().part1("15_1large"));
    }

    @Test
    void test15_21() {
        assertEquals(2028, new Day15().part2("15_2small"));
    }

    @Test
    void test15_22() {
        assertEquals(9021, new Day15().part2("15_1large"));
    }
}
