package minesweeper.game;

public final class Suggestion {
    private final int index;
    private final Type type;

    public Suggestion(int x, int y, String type) {
        this.index = (y - 1) * Game.SIZE + (x - 1);
        this.type = Type.valueOf(type.toUpperCase());
    }

    public int getIndex() {
        return index;
    }

    public Type getType() {
        return type;
    }

    enum Type {
        FREE, MINE
    }
}
