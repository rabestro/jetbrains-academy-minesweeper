import java.util.function.BiPredicate;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public class GameStep {
    private static final String EXPECTED_SYMBOLS = "[./*x1-9]*";
    static final int SIZE = 9;
    static final int CELLS_COUNT = SIZE * SIZE;

    private final String message;
    private final String board;

    private GameStep(String board, String message) {
        this.message = message;
        this.board = board;
    }

    static GameStep parse(final String output) {
        final var data = output.toLowerCase().strip()
                .replace("|", "│")
                .replace("-", "—");

        final var lines = data.lines().toArray(String[]::new);
        Assert.that(lines.length > 0, "no_output_found", lines.length);
        final var message = lines[lines.length - 1];
        // TODO check the error message: There is a number here!

        Assert.that(lines.length >= 13, "less_then_13_lines", lines.length);

        Assert.find(message,"failed|congratulations|unset mines", "no_last_message");

        Assert.contains(data, "│123456789│", "board_header_numbers");

        final var board = data.replaceAll("(?s).*?│.{1,3}1│|│.{1,3}\\d│|│.*", "");
        final var cellsCount = SIZE * SIZE;

        Assert.that(board.length() == cellsCount, "cells_number_incorrect", cellsCount, board.length());
        Assert.matches(board, EXPECTED_SYMBOLS, "illegal_symbol", board.replaceAll(EXPECTED_SYMBOLS, ""));

        final BiPredicate<Character, Character> noNeighbors = (first, second) -> range(0, cellsCount)
                .filter(index -> board.charAt(index) == first)
                .noneMatch(index -> neighbors(index).map(board::charAt).anyMatch(second::equals));

        Assert.that(noNeighbors.test('.', '/'), "impossible_slash_dot");
        Assert.that(noNeighbors.test('x', '/'), "impossible_slash_x");
        Assert.that(noNeighbors.test('*', '/'), "impossible_slash_asterisk");

        Assert.that(board.indexOf('x') == -1 || message.contains("failed"), "no_failed_and_x");

        return new GameStep(board, message);
    }

    boolean isFailed() {
        return message.contains("failed");
    }

    boolean isWin() {
        return message.contains("congratulations");
    }

    boolean isPlaying() {
        return message.contains("unset mines");
    }

    boolean isKnowsMines(final int index) {
        return isNumber(index) &&
                getNumber(index) == neighbors(index).filter(this::isUnexplored).count();
    }

    boolean isUnexplored(final int index) {
        return board.charAt(index) == '.' || board.charAt(index) == '*';
    }

    int getNumber(final int index) {
        return isNumber(index) ? Character.digit(board.charAt(index), 10) : -1;
    }

    boolean isNumber(final int index) {
        return '0' < board.charAt(index) && board.charAt(index) <= '9';
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

    boolean isDot(int index) {
        return board.charAt(index) == '.';
    }
}
