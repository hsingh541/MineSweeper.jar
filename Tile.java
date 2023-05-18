import java.util.Random;

public class Tile {
    private final int CLOSED = 0;
    private final int FLAGGED = 1;
    private final int OPEN = 2;
    private int state;
    private boolean hasMine;
    private int nearMines;

    public Tile() {
        hasMine = false;
        nearMines = 0;
        state = CLOSED;
    }
    //Returns true if the tile is flagged
    public boolean isFlagged() {
        return state == FLAGGED;
    }
    //Returns true is the tile is open
    public boolean isOpen() {
        return state == OPEN;
    }
    //Returns true if the tile is closed
    public boolean isClosed() {
        return state == CLOSED;
    }
    //Returns true if the tile is a mine
    public boolean isMine() {
        return hasMine;
    }

    //Returns the amount of mines near the tile
    public int getNearMines() {
        return nearMines;
    }

    //Sets tile as a flag only if it is closed and removes flag if it is already flagged.
    public void toggleFlag() {
        if (isFlagged()) {
            state = CLOSED;
        } else if (isClosed()) {
            state = FLAGGED;
        }
    }

    public void setNearMines(int mines) {
        nearMines = mines;
    }
    //It turns the tile into a mine
    public void makeMine(){
        hasMine = true;
    }
    //If the tile is closed then it opens it
    public void open() {
        if (isClosed()) {
            state = OPEN;
        }
    }

}