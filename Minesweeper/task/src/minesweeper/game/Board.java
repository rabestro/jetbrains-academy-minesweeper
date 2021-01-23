package minesweeper.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toUnmodifiableSet;

public class Board {
    private final int size;
    private final int cellsCount;
    private final CellState[] state;
    private final Set<Integer> mines;

    public Board(final int minesCount) {
        size = Game.SIZE;
        cellsCount = size * size;
        state = new CellState[size * size];
        Arrays.fill(state, CellState.UNKNOWN);

        final var random = new Random();
        mines = new HashSet<>(minesCount);

        while (mines.size() < minesCount) {
            mines.add(random.nextInt(cellsCount));
        }
    }

    boolean isAllMineMarked() {
        return IntStream
                .range(0, cellsCount)
                .filter(i -> state[i] == CellState.MARK)
                .boxed()
                .collect(toUnmodifiableSet())
                .equals(mines);
    }

    boolean isAllExplored() {
        return Arrays.stream(state)
                .filter(CellState.UNEXPLORED::contains)
                .count() == mines.size();
    }

    void exploreCell(int index) {
        final int number = countMines(index);
        state[index] = CellState.values()[number];

        if (number == 0) {
            neighbors(index).filter(this::isUnexplored).forEach(this::exploreCell);
        }
    }

    private int countMines(final int index) {
        return (int) neighbors(index).filter(mines::contains).count();
    }

    public boolean hasMine(final int index) {
        return mines.contains(index);
    }

    public boolean isUnexplored(int index) {
        return CellState.UNEXPLORED.contains(state[index]);
    }

    public void mark(final int index) {
        state[index] = (state[index] == CellState.MARK) ? CellState.UNKNOWN : CellState.MARK;
    }

    public void showMines() {
        mines.forEach(index -> state[index] = CellState.MINE);
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

}