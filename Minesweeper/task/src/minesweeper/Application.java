package minesweeper;

import minesweeper.game.Game;
import minesweeper.game.UI;

public class Application implements Runnable {

    @Override
    public void run() {
        final var ui = new UI();

        final var game = new Game(ui.askMinesNumber());
        System.out.println(game.getBoard());
        final var suggestion = ui.askSuggestion();


        /*
        while (!game.isOver()) {
            final var suggestion = ui.askSuggestion();

            if (game.isDigit(suggestion.getIndex())) {
                System.out.println("There is a number here!");
                continue;
            }
            if (suggestion.getState() == State.MINE) {
                game.setMark(suggestion.getIndex());
                System.out.println(game);
                continue;
            }
            if (game.isMine(suggestion.getIndex())) {
                game.markMines();
                System.out.println(game);
                System.out.println("You stepped on a mine and failed!");
                break;
            }
        }
        */
        System.out.println("Congratulations! You found all the mines!");
    }
}
