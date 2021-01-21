package minesweeper;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private static final int MAX_MINES = 9 * 9 - 1;
    private static final int MIN_MINES = 1;

    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);

        System.out.print("How many mines do you want on the field?");
        final var minesCount = scanner.nextInt();

        if (minesCount < MIN_MINES || minesCount > MAX_MINES) {
            System.out.printf("The mine count should be from %d to %d%n", MIN_MINES, MAX_MINES);
            return;
        }

        System.out.println(new Field(minesCount));

    }
}

class Field {
    public static final int SIZE = 9;
    private static final Random rnd = new Random();
    private final BitSet field = new BitSet(SIZE * SIZE);

    Field(final int mines) {
        int restMines = mines;
        while (restMines > 0) {
            int i = rnd.nextInt(SIZE * SIZE);
            if (field.get(i)) {
                continue;
            }
            field.set(i);
            restMines--;
        }
    }

    public boolean isMine(final int x, final int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && field.get(y * SIZE + x);
    }

    public String countMines(int index) {
        final var x = index % SIZE;
        final var y = index / SIZE;
        final var count = Arrays
                .stream(new int[][] {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}})
                .filter(offset -> isMine(x + offset[0], y + offset[1]))
                .count();
        return count == 0 ? "." : String.valueOf(count);
    }

    private String getSymbol(int index) {
        return (field.get(index) ? "X" : countMines(index)) + (index % SIZE == SIZE - 1 ? "\n" : "");
    }

    @Override
    public String toString() {
        return IntStream.range(0, SIZE * SIZE)
                .mapToObj(this::getSymbol)
                .collect(Collectors.joining());
    }
}