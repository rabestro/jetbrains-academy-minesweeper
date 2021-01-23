package minesweeper.game;

import java.util.Scanner;

public class Game {
    public static final int SIZE = 9;
    private static final Scanner scanner = new Scanner(System.in);

    private final Board board;
    private State state = State.PLAY;

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
            if (suggestion.getType() == Suggestion.Type.MINE) {
                board.mark(suggestion.getIndex());
                state = board.isAllMineMarked() ? State.WIN : State.PLAY;
                continue;
            }
            if (board.hasMine(suggestion.getIndex())) {
                board.showMines();
                state = State.LOSE;
                break;
            }
            board.exploreCell(suggestion.getIndex());
            state = board.isAllExplored() ? State.WIN : State.PLAY;
        } while (state == State.PLAY);

        System.out.println(board);
        System.out.println(state == State.WIN
                ? "Congratulations! You found all the mines!"
                : "You stepped on a mine and failed!");
    }


    public Suggestion askSuggestion() {
        while (true) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            final var suggestion = new Suggestion(scanner.nextInt(), scanner.nextInt(), scanner.next());
            if (board.isUnexplored(suggestion.getIndex())) {
                return suggestion;
            }
            System.out.println("The cell is already explored!");
        }
    }

    enum State {
        PLAY, WIN, LOSE
    }
}
