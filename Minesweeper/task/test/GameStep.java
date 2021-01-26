public class GameStep {

    private GameStep(String board, String message) {
        this.message = message;
        this.board = board;
    }

    static GameStep parse(final String output) {
        Assert.contains(output, "123456789", "board_header_numbers");
        return new GameStep("", "");
    }

    private final String message;
    private final String board;

}
