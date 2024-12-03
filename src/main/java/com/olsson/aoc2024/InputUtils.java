package com.olsson.aoc2024;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InputUtils {

    private InputUtils() {
        // Utility class
    }

    public static List<String> getLines(String day) {
        var path = findPath(day);
        try {
            return Files.readAllLines(path);
        } catch (IOException ex) {
            throw new IllegalArgumentException(path + " - unable to read contents");
        }
    }

    public static List<String[]> getSplitLines(String day, String separator) {
        return getLines(day)
                .stream()
                .map(s -> s.split(separator))
                .toList();
    }

    private static Path findPath(String day) {
        var fileName = "day" + day + ".txt";
        var url = InputUtils.class.getClassLoader().getResource(fileName);
        if (url == null) {
            throw new IllegalArgumentException(fileName + " - file was not found");
        }
        try {
            return Path.of(url.toURI());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

}
