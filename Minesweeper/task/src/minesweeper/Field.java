package minesweeper;

import java.util.BitSet;
import java.util.Collections;
import java.util.logging.Logger;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class Field {
    private static final Logger log = Logger.getLogger(Field.class.getName());

    private static final int[][] OFFSETS =
            new int[][]{{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

    public static final int SIZE = 9;
    public static final int MAX_MINES = SIZE * SIZE - 1;
    public static final int MIN_MINES = 1;


    private final BitSet mines = new BitSet(SIZE * SIZE);
    private final BitSet suggestions = new BitSet(SIZE * SIZE);
    private final int[] numbers;

    Field(final int mines) {
        for (int i = 0; i < mines; i++) {
            this.mines.set(getRandomFreeCell());
        }

        numbers = range(0, SIZE * SIZE).map(this::countMines).toArray();
        log.config(this.mines::toString);
        suggestions.or(this.mines);
        log.config(this::toString);
        suggestions.clear();
        //log.config(Arrays.toString(numbers));
    }

    private int getRandomFreeCell() {
        final var freeCells = range(0, SIZE * SIZE)
                .filter(i -> !mines.get(i)).boxed().collect(toList());
        Collections.shuffle(freeCells);
        return freeCells.get(0);
    }

    public boolean isOver() {
        return mines.equals(suggestions);
    }

    public boolean isDigit(int index) {
        return Character.isDigit(numbers[index]);
    }

    public void setMark(int index) {
        if (suggestions.get(index)) {
            suggestions.clear(index);
        } else {
            suggestions.set(index);
        }
    }

    public int toIndex(final int x, final int y) {
        return (y - 1) * Field.SIZE + (x - 1);
    }

    @Override
    public String toString() {
        return " │123456789│\n—│—————————│\n"
                + range(0, SIZE * SIZE).mapToObj(this::getSymbol).collect(joining())
                + "—│—————————│";
    }

    private String getSymbol(int index) {
        return (index % SIZE == 0 ? (index / SIZE + 1) + "|" : "")
                + (suggestions.get(index) ? '*' : mines.get(index) ? '.' : countMines(index))
                + (index % SIZE == SIZE - 1 ? "|\n" : "");
    }

    public char countMines(int index) {
        if (mines.get(index)) return 0;

        final var x = index % SIZE;
        final var y = index / SIZE;
        final var count = stream(OFFSETS).filter(o -> isMine(x + o[0], y + o[1])).count();
        log.fine(String.format("Index: %d, x = %d, y = %d, Count: %d", index, x, y, count));
        return count == 0 ? '.' : Character.forDigit((int) count, 10);
    }

    public boolean isMine(final int x, final int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && mines.get(y * SIZE + x);
    }

}
