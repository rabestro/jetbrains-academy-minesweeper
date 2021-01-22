package minesweeper;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.logging.Logger;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class Field {
    private static final Logger log = Logger.getLogger(Field.class.getName());

    private static final int STATE_UNEXPLORED = -1;
    private static final int STATE_MARK = -2;
    private static final int STATE_MINE = -3;
    private static final int STATE_FREE = 0;
    private static final int[][] OFFSETS =
            new int[][]{{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

    public static final int SIZE = 9;
    public static final int CELLS = SIZE * SIZE;
    public static final int MIN_MINES = 1;
    public static final int MAX_MINES = CELLS - 1;

    private final BitSet mines = new BitSet(CELLS);
    private final BitSet suggestions = new BitSet(CELLS);
    private final int[] numbers;
    private final int[] state = new int[CELLS];

    Field(final int mines) {
        Arrays.fill(state, STATE_UNEXPLORED);

        for (int i = 0; i < mines; i++) {
            this.mines.set(getRandomFreeCell());
        }

        numbers = range(0, CELLS).map(this::countMines).toArray();
        log.config(this.mines::toString);
        suggestions.or(this.mines);
        log.config(this::toString);
        suggestions.clear();
    }

    private int getRandomFreeCell() {
        final var freeCells = range(0, CELLS).filter(i -> !mines.get(i)).boxed().collect(toList());
        Collections.shuffle(freeCells);
        return freeCells.get(0);
    }

    public boolean isOver() {
        return mines.equals(suggestions);
    }

    public boolean isDigit(int index) {
        return Character.isDigit(numbers[index]);
    }

    public boolean isMine(int index) {
        return mines.get(index);
    }

    public void markMines() {
        mines.stream().forEach(i -> state[i] = STATE_MINE);
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
                + range(0, CELLS).mapToObj(this::getSymbol).collect(joining())
                + "—│—————————│";
    }

    private char getState(final int index) {
        switch (state[index]) {
            case STATE_UNEXPLORED:
                return '.';
            case STATE_MARK:
                return '*';
            case STATE_MINE:
                return 'X';
            case STATE_FREE:
                return '/';
            default:
                return Character.forDigit(state[index], 10);
        }
    }

    private String getSymbol(int index) {
        return (index % SIZE == 0 ? (index / SIZE + 1) + "|" : "")
                + getState(index)
                + (index % SIZE == SIZE - 1 ? "|\n" : "");
    }

    public char countMines(int index) {
        if (mines.get(index)) return 0;

        final var x = index % SIZE;
        final var y = index / SIZE;
        final var count = stream(OFFSETS).filter(o -> isMine(x + o[0], y + o[1])).count();
        return count == 0 ? '.' : Character.forDigit((int) count, 10);
    }

    public boolean isMine(final int x, final int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && mines.get(y * SIZE + x);
    }

}
