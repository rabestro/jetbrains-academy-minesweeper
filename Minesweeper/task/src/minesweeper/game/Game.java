package minesweeper.game;

import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Game {
    public static final int SIZE = 9;
    public static final int CELLS = SIZE * SIZE;

    private static final Random rnd = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    private final BitSet mines = new BitSet(Board.CELLS);
    //    private final BitSet suggestions = new BitSet(Board.CELLS);
    private final Board board = new Board();

    public static Game create() {
        System.out.print("How many mines do you want on the field? ");
        return new Game(scanner.nextInt());
    }

    private Game(final int minesCount) {
        while (mines.cardinality() < minesCount) {
            mines.set(rnd.nextInt(CELLS));
        }
    }

    public void run() {
        System.out.println(board);

        final var suggestion = askSuggestion();

        if (suggestion.getType() == Suggestion.Type.MINE) {
            board.mark(suggestion.getIndex());
        } else {
            if (mines.get(suggestion.getIndex())) {
                mines.stream().forEach(board::showMine);
            } else {
//                neighbors(suggestion.getIndex()).forEach(System.out::println);
                explore(suggestion.getIndex());
            }
        }

        System.out.println(board);
    }

    void explore(int index) {
        int m = countMines(index);
        board.setNumber(index, m);
        if (m == 0) {
            neighbors(index).filter(board::isUnexplored).forEach(this::explore);
        }
    }

    int countMines(final int index) {
        return (int) neighbors(index).filter(mines::get).count();
    }

    IntStream neighbors(final int index) {
        return IntStream
                .of(-SIZE - 1, -SIZE, -SIZE + 1, -1, 1, SIZE - 1, SIZE, SIZE + 1)
                .filter(offset -> inRange(index, offset))
                .map(offset -> index + offset);
    }

    boolean inRange(final int index, final int offset) {
        return inRange(index % SIZE + offset - offset / (SIZE - 1) * SIZE)
                && inRange(index / SIZE + offset / (SIZE - 1));
    }

    boolean inRange(final int x) {
        return x >= 0 && x < SIZE;
    }

    public Board getBoard() {
        return board;
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
}
