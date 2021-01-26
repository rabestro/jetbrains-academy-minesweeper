public class GameStep {

    private GameStep(String board, String message) {
        this.message = message;
        this.board = board;
    }

    static GameStep parse(final String output) {
        final var lines = output.lines().toArray(String[]::new);
        Assert.that(lines.length >= 13, "less_then_13_lines", lines.length);
        Assert.contains(output, "123456789", "board_header_numbers");

        final var board = output.replaceAll("(?s).*?1│|│.{1,3}\\d│|│.*", "");
        Assert.matches(board, "[./*X1-9]*", "illegal_symbol");

        return new GameStep(board, "");
    }

    private final String message;
    private final String board;

}
