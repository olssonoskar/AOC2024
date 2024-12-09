package com.olsson.aoc2024.days;

import com.olsson.aoc2024.InputUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day09 implements Day {

    @Override
    public String part1() {
        return "" + part1("9");
    }

    @Override
    public String part2() {
        return "" + part2("9");
    }

    public long part1(String day) {
        var diskmap = InputUtils.getLines(day).getFirst();
        var fs = filesystem(diskmap);
        var compact = new CompactFileSystemReader(fs).compact();
        return compactSum(compact);
    }

    public long part2(String day) {
        var diskmap = InputUtils.getLines(day).getFirst();
        var fs = filesystem(diskmap);
        var nonFragmented = new CompactFileSystemReader(fs).nonFragmented();
        return defragmentSum(nonFragmented);
    }

    private List<Integer> filesystem(String diskmap) {
        var build = new ArrayList<Integer>();
        for (int i = 0; i < diskmap.length(); i += 2) {
            var fileLength = Integer.parseInt(String.valueOf(diskmap.charAt(i)));
            int freeSpace;
            try {
                freeSpace = Integer.parseInt(String.valueOf(diskmap.charAt(i + 1)));
            } catch (IndexOutOfBoundsException _) {
                freeSpace = 0;
            }
            for(int j = 0; j < fileLength; j++) {
                build.add(i/2);
            }
            for(int j = 0; j < freeSpace; j++) {
                build.add(-1);
            }
        }
        return build;
    }

    private long compactSum(List<Integer> compact) {
        var sum = 0L;
        for (int i = 0; i < compact.size(); i++) {
            var value = compact.get(i) * i;
            sum += value;
        }
        return sum;
    }

    private long defragmentSum(List<Integer> defragmented) {
        var sum = 0L;
        for (int i = 0; i < defragmented.size(); i++) {
            long value = defragmented.get(i);
            if (value == -1L) {
                continue;
            }
            sum += value * i;
        }
        return sum;
    }

    private static class CompactFileSystemReader {

        private int indexLeft;
        private int indexRight;
        private final List<Integer> fs;
        private int lastIdFromRight;

        private CompactFileSystemReader(List<Integer> fs) {
            this.fs = fs;
            this.indexLeft = 0;
            this.indexRight = fs.size() - 1;
            this.lastIdFromRight = Integer.MAX_VALUE;
        }

        List<Integer> compact() {
            var compactBuilder = new ArrayList<Integer>();
            while(hasNext()) {
                compactBuilder.add(readBlock());
            }
            return compactBuilder;
        }

        List<Integer> nonFragmented() {
            while(!fullyDefragmented()) {
                var file = readFileFromEnd();
                file.ifPresent(this::findSpace);
            }
            return fs;
        }

        private boolean hasNext() {
            return indexLeft <= indexRight;
        }

        private boolean fullyDefragmented() {
            return indexRight <= 0;
        }

        private Integer readBlock() {
           var next = fs.get(indexLeft);
           indexLeft += 1;
           if (next.equals(-1)) {
               return getNextBlockFromEnd();
           }
           return next;
        }

        private Integer getNextBlockFromEnd() {
            for (int i = indexRight; indexLeft < i; i--) {
                if (!fs.get(i).equals(-1)) {
                    indexRight = i - 1;
                    return (fs.get(i));
                }
            }
            return 0;
        }

        // Mutates fs
        // Iterate from left and find a space large enough to store the file, swap it in place
        private void findSpace(File file) {
            var spaceCount = 0;
            for(int i = 0; i < fs.size() && i <= indexRight; i++) {
                if (fs.get(i).equals(-1)) {
                    spaceCount++;
                } else {
                    spaceCount = 0;
                }
                if (spaceCount == file.blocks) {
                    swap(i, file);
                    return;
                }
            }
        }

        private void swap(int i, File file) {
            for (int j = 0; j < file.blocks; j++) {
                var swap = i + 1 - file.blocks + j;
                var remove = indexRight + 1 + j;
                fs.set(swap, file.id);
                fs.set(remove, -1);
            }
        }

        private Optional<File> readFileFromEnd() {
            for (int i = indexRight; i > 0 ; i--) {
                var next = fs.get(i);
                if (next.equals(-1) || next > lastIdFromRight) {
                    continue;
                }
                // First id is 0 and cant be moved, so nothing left to do here
                if (next == 0) {
                    indexRight = 0;
                    return Optional.empty();
                }
                for (int fileIndex = i; fileIndex > 0; fileIndex--) {
                    if (!fs.get(fileIndex).equals(next)) {
                        indexRight = fileIndex;
                        lastIdFromRight = next;
                        return Optional.of(new File(next, i - fileIndex));
                    }
                }
            }
            return Optional.empty();
        }

        private record File(int id, int blocks){ }

    }
}
