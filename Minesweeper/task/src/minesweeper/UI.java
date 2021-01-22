package minesweeper;

import java.util.Scanner;

public class UI {
    private static final Scanner scanner = new Scanner(System.in);

    public Suggestion askSuggestion() {
        System.out.print("Set/unset mines marks or claim a cell as free: ");
        return new Suggestion(scanner.nextInt(), scanner.nextInt(), scanner.next());
    }

    public int askMinesNumber() {
        while (true) {
            System.out.print("How many mines do you want on the field?");
            final var minesCount = scanner.nextInt();
            if (minesCount >= Field.MIN_MINES && minesCount <= Field.MAX_MINES) {
                return minesCount;
            }
            System.out.printf("The mine count should be from %d to %d%n", Field.MIN_MINES, Field.MAX_MINES);
        }
    }

}
