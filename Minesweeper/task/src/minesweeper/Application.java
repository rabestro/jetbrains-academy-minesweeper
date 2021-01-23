package minesweeper;

import minesweeper.game.Game;

public class Application implements Runnable {

    @Override
    public void run() {

         Game.create().run();


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
