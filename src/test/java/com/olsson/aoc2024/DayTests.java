package com.olsson.aoc2024;

import com.olsson.aoc2024.days.Day01;
import com.olsson.aoc2024.days.Day02;
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
}