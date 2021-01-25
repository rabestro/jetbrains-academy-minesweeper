package minesweeper.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class Board {
    private final int size;
    private final CellState[] field;
    private final Set<Integer> mines;

    public Board(final int minesCount) {
        size = Game.SIZE;
        field = new CellState[size * size];
        Arrays.fill(field, CellState.UNKNOWN);

        final var indexes = cellsIndexes().boxed().collect(toList());
        Collections.shuffle(indexes);
        mines = Set.copyOf(indexes.subList(0, minesCount));
    }

    IntStream cellsIndexes() {
        return IntStream.range(0, field.length);
    }

    private boolean isAllExplored() {
        return Arrays.stream(field)
                .filter(CellState.UNEXPLORED::contains)
                .count() == mines.size();
    }

    private int countMines(final int index) {
        return (int) neighbors(index).filter(mines::contains).count();
    }

    boolean isUnexplored(final int index) {
        return CellState.UNEXPLORED.contains(field[index]);
    }

    GameState getState(final Suggestion suggestion) {
        final int index = suggestion.getIndex();
        if (suggestion.isMine) {
            flipMark(index);
            return isAllMinesMarked() ? GameState.WIN : GameState.PLAYING;
        }
        if (mines.contains(index)) {
            showMines();
            return GameState.LOSE;
        }
        exploreCell(index);
        return isAllExplored() ? GameState.WIN : GameState.PLAYING;
    }

    private boolean isAllMinesMarked() {
        return cellsIndexes()
                .filter(i -> field[i] == CellState.MARK)
                .boxed()
                .collect(toUnmodifiableSet())
                .equals(mines);
    }

    private void flipMark(final int index) {
        field[index] = field[index] == CellState.MARK ? CellState.UNKNOWN : CellState.MARK;
    }

    private void showMines() {
        mines.forEach(index -> field[index] = CellState.MINE);
    }

    private void exploreCell(final int index) {
        final int number = countMines(index);
        field[index] = CellState.values()[number];

        if (number == 0) {
            neighbors(index).filter(this::isUnexplored).forEach(this::exploreCell);
        }
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
                output.append(field[index++].getSymbol());
            }
            output.append('│').append(System.lineSeparator());
        }
        return output.append("—│—————————│").toString();
    }

    class Suggestion {
        private final int index;
        private final boolean isMine;

        public Suggestion(final int x, final int y, final String type) {
            this.index = (y - 1) * size + (x - 1);
            isMine = "mine".equalsIgnoreCase(type);
        }

        public int getIndex() {
            return index;
        }

    }
}