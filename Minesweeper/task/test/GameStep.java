import java.util.function.BiPredicate;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public class GameStep {
    static final int SIZE = 9;
    static final int CELLS_COUNT = SIZE * SIZE;
    private static final String EXPECTED_SYMBOLS = "[./*X1-9]*";

    private GameStep(String board, String message) {
        this.message = message;
        this.board = board;
    }

    static GameStep parse(final String output) {
        final var data = output.toLowerCase().strip();
        final var lines = data.lines().toArray(String[]::new);

        Assert.that(lines.length >= 13, "less_then_13_lines", lines.length);
        Assert.contains(data, "123456789", "board_header_numbers");

        final var board = data.replaceAll("(?s).*?1│|│.{1,3}\\d│|│.*", "");
        final var cellsCount = SIZE * SIZE;

        Assert.that(board.length() == cellsCount, "cells_number_incorrect", cellsCount, board.length());
        Assert.matches(board, EXPECTED_SYMBOLS, "illegal_symbol", board.replaceAll(EXPECTED_SYMBOLS, ""));

        final BiPredicate<Character, Character> noNeighbors = (first, second) -> range(0, cellsCount)
                .filter(index -> board.charAt(index) == first)
                .noneMatch(index -> neighbors(index).map(board::charAt).anyMatch(second::equals));

        Assert.that(noNeighbors.test('.', '/'), "impossible_slash_dot");
        Assert.that(noNeighbors.test('x', '/'), "impossible_slash_x");
        Assert.that(noNeighbors.test('*', '/'), "impossible_slash_asterisk");

        final var prompt = lines[lines.length - 1];
        Assert.that(board.indexOf('x') == -1 || prompt.contains("failed"), "no_failed_and_x");

        return new GameStep(board, prompt);
    }

    private final String message;
    private final String board;

    boolean isFailed() {
        return message.contains("failed");
    }

    boolean isWin() {
        return message.contains("congratulations");
    }

    boolean isPlaying() {
        return message.contains("unset mines");
    }

    private static IntStream neighbors(final int index) {
        return IntStream
                .of(-SIZE - 1, -SIZE, -SIZE + 1, -1, 1, SIZE - 1, SIZE, SIZE + 1)
                .filter(offset -> inRange(index, offset))
                .map(offset -> index + offset);
    }

    private static boolean inRange(final int index, final int offset) {
        return inRange(index % SIZE + offset - offset / (SIZE - 1) * SIZE)
                && inRange(index / SIZE + offset / (SIZE - 1));
    }

    private static boolean inRange(final int x) {
        return x >= 0 && x < SIZE;
    }

    int countSymbol(final char symbol) {
        return (int) board.chars().filter(c -> c == symbol).count();
    }

    static IntStream allIndexes() {
        return range(0, CELLS_COUNT);
    }
}
