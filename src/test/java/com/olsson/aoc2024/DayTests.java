package com.olsson.aoc2024;

import com.olsson.aoc2024.days.Day01;
import com.olsson.aoc2024.days.Day02;
import com.olsson.aoc2024.days.Day03;
import com.olsson.aoc2024.days.Day04;
import com.olsson.aoc2024.days.Day05;
import com.olsson.aoc2024.days.Day06;
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
}
