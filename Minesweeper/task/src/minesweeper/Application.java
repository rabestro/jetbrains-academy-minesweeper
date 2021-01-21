package minesweeper;

import java.util.Scanner;

public class Application implements Runnable {

    @Override
    public void run() {
        final var scanner = new Scanner(System.in);

        System.out.print("How many mines do you want on the field?");
        final var minesCount = scanner.nextInt();

        if (minesCount < Field.MIN_MINES || minesCount > Field.MAX_MINES) {
            System.out.printf("The mine count should be from %d to %d%n", Field.MIN_MINES, Field.MAX_MINES);
            return;
        }
        final var game = new Field(minesCount);
        System.out.println(game);

        while (!game.isOver()) {
            System.out.println("Set/delete mines marks (x and y coordinates):");
            final var x = scanner.nextInt();
            final var y = scanner.nextInt();
            final var index = game.toIndex(x, y);

            if (game.isDigit(index)) {
                System.out.println("There is a number here!");
                continue;
            }
            game.setMark(index);
            System.out.println(game);
        }
        System.out.println("Congratulations! You found all the mines!");
    }
}
