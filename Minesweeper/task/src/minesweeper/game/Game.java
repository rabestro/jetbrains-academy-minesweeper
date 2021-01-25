package minesweeper.game;

import java.util.Scanner;

public class Game {
    public static final int SIZE = 9;
    private static final Scanner scanner = new Scanner(System.in);

    private final Board board;
    private GameState state = GameState.PLAY;

    private Game(final Board board) {
        this.board = board;
    }

    public static Game create() {
        System.out.print("How many mines do you want on the field? ");
        return new Game(new Board(scanner.nextInt()));
    }

    public void run() {
        do {
            System.out.println(board);
            final var suggestion = askSuggestion();
            if (suggestion.isMine()) {
                board.mark(suggestion.getIndex());
                state = board.isAllMineMarked() ? GameState.WIN : GameState.PLAY;
                continue;
            }
            if (board.hasMine(suggestion.getIndex())) {
                board.showMines();
                state = GameState.LOSE;
                break;
            }
            board.exploreCell(suggestion.getIndex());
            state = board.isAllExplored() ? GameState.WIN : GameState.PLAY;
        } while (state == GameState.PLAY);

        System.out.println(board);
        System.out.println(state == GameState.WIN
                ? "Congratulations! You found all the mines!"
                : "You stepped on a mine and failed!");
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

    enum GameState {
        PLAY, WIN, LOSE
    }
}
