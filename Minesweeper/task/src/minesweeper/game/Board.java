package minesweeper.game;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {
    public static final int SIZE = 9;
    public static final int CELLS = SIZE * SIZE;

    private final int size = 9;
    private final CellState[] state;
    private final BitSet mines;

    public Board(final int minesCount) {
        state = new CellState[size * size];
        Arrays.fill(state, CellState.UNKNOWN);

        mines = new BitSet(size * size);
        final var random = new Random();

        while (mines.cardinality() < minesCount) {
            mines.set(random.nextInt(CELLS));
        }
    }

    boolean isAllMineMarked() {
        return IntStream
                .range(0, size * size)
                .filter(i -> state[i] == CellState.MARK)
                .boxed()
                .collect(Collectors.toSet())
                .equals(mines.stream().boxed().collect(Collectors.toSet()));
    }

    boolean isAllExplored() {
        return Arrays.stream(state).filter(CellState.UNEXPLORED::contains).count() == mines.cardinality();
    }

    void exploreCell(int index) {
        final int number = countMines(index);
        state[index] = CellState.values()[number];

        if (number == 0) {
            neighbors(index).filter(this::isUnexplored).forEach(this::exploreCell);
        }
    }

    private int countMines(final int index) {
        return (int) neighbors(index).filter(mines::get).count();
    }

    public boolean isUnexplored(int index) {
        return CellState.UNEXPLORED.contains(state[index]);
    }

    public void mark(final int index) {
        state[index] = (state[index] == CellState.MARK) ? CellState.UNKNOWN : CellState.MARK;
    }

    public void showMine(final int index) {
        state[index] = CellState.MINE;
    }

    IntStream neighbors(final int index) {
        return IntStream
                .of(-size - 1, -size, -size + 1, -1, 1, size - 1, size, size + 1)
                .filter(offset -> inRange(index, offset))
                .map(offset -> index + offset);
    }

    boolean inRange(final int index, final int offset) {
        return inRange(index % size + offset - offset / (size - 1) * size)
                && inRange(index / size + offset / (size - 1));
    }

    boolean inRange(final int x) {
        return x >= 0 && x < size;
    }

    @Override
    public String toString() {
        final var output = new StringBuilder()
                .append(" │123456789│").append(System.lineSeparator())
                .append("—│—————————│").append(System.lineSeparator());
        int index = 0;
        for (int row = 1; row <= size; ++row) {
            output.append(row).append('│');
            for (int col = 1; col <= size; ++col) {
                output.append(state[index++].getSymbol());
            }
            output.append('│').append(System.lineSeparator());
        }
        return output.append("—│—————————│").toString();
    }

    public void setNumber(int index, int m) {
        state[index] = CellState.values()[m];
    }
}