package minesweeper.game;

import java.util.Scanner;

public class Game {
    public static final int SIZE = 9;
    private static final Scanner scanner = new Scanner(System.in);

    private final Board board;

    private Game(final Board board) {
        this.board = board;
    }

    public static Game create() {
        System.out.print("How many mines do you want on the field? ");
        return new Game(new Board(scanner.nextInt()));
    }

    public void run() {
        var gameState = GameState.PLAYING;

        while (gameState == GameState.PLAYING) {
            System.out.println(board);
            gameState = board.getState(askSuggestion());
        }

        System.out.println(board);
        System.out.println(gameState);
    }

    public Board.Suggestion askSuggestion() {
        while (true) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            final var suggestion = board.new Suggestion(scanner.nextInt(), scanner.nextInt(), scanner.next());
            if (board.isUnexplored(suggestion.getIndex())) {
                return suggestion;
            }
            System.out.println("The cell is already explored!");
        }
    }

}
