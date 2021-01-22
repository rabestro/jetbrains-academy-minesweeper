package minesweeper;

import java.util.Scanner;

public class Application implements Runnable {

    @Override
    public void run() {
        final var ui = new UI();
        final var scanner = new Scanner(System.in);

        final var game = new Field(ui.askMinesNumber());
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
