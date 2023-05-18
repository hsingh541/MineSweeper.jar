import processing.core.PApplet;

import java.util.Random;

public class Board {
    //Boolean that checks if the mines have been set
    private boolean minesSet;
    //Boolean that checks if the game has started
    private boolean gameStart;
    //Boolean the returns true if the game is won
    private boolean won = false;
    //Boolean the returns true if the game is lost
    private boolean lost = false;
    //Total number of mines
    private final int totalMines;
    private Tile[][] tiles;
    //Tile size variable to make it easier to debug and change tile size
    private final int tileSize = 35;
    //Constructer for the board class
    public Board(int length, int width, int mines) {
        tiles = new Tile[width][length];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                tiles[row][col] = new Tile();
            }
        }
        minesSet = false;
        gameStart = false;
        totalMines = mines;
    }
    //Returns the length of the board
    public int getLength() {
        return tiles.length;
    }
    //Returns the width of the board
    public int getWidth() {
        return tiles[0].length;
    }
    //Returns the number of mines
    public int getTotalMines() {
        return totalMines;
    }
    //Returns the size of the tile
    public int getTileSize(){
        return tileSize;
    }
    //Returns the number of flags currently on the board
    public int getTotalFlags() {
        int count = 0;
        for (int r = 0; r < getLength(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                if (tiles[r][c].isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }
    //Starts the game and allows the mines to be set
    public void start() {
        minesSet = false;
        gameStart = true;
    }
    //Returns true if the game has been lost by the user
    public boolean isLost() {
        if (won) {
            return false;
        }
        for (Tile[] tile : tiles) {
            for (int col = 0; col < getWidth(); col++) {
                if (tile[col].isOpen() && tile[col].isMine()) {
                    lost = true;
                    return true;
                }
            }
        }
        return false;
    }

    //Returns true if the game has been won by the user
    public boolean isWon() {
        if (lost) {
            return false;
        }
        for (Tile[] tile : tiles) {
            for (int col = 0; col < getWidth(); col++) {
                if (!tile[col].isOpen() && !tile[col].isMine() && !lost) {
                    return false;
                }
            }
        }
        won = true;
        return true;
    }
    //Sets the tile to a mine
    public void setMines(int r, int c) {
        tiles[r][c].makeMine();
    }
    //Returns the number of mines near a tile
    private int getNearMines(int r, int c) {
        if (tiles[r][c].isMine()) {
            return -1;
        }
        int numMines = 0;
        for (int row = r - 1; row <= r + 1; row++) {
            for (int col = c - 1; col <= c + 1; col++) {
                if (row >= 0 && col >= 0 && row < getLength() && col < getWidth()) {
                    if (tiles[row][col].isMine()) {
                        numMines++;
                    }
                }
            }
        }
        return numMines;
    }
    //Returns the numeber of flags near a tile
    private int getNearFlags(int r, int c) {
        if (tiles[r][c].isFlagged()) {
            return 0;
        }
        int numFlags = 0;
        for (int row = r - 1; row <= r + 1; row++) {
            for (int col = c - 1; col <= c + 1; col++) {
                if (row >= 0 && col >= 0 && row < getLength() && col < getWidth()) {
                    if (tiles[row][col].isFlagged()) {
                        numFlags++;
                    }
                }
            }
        }
        return numFlags;
    }
    /*Takes in the coordinates r and c and opens the tile
    If there are tiles around the initial tile that has no mines around it, it recursively opens that tile as well
    * */
    public void reveal(int r, int c) {
        tiles[r][c].open();

        if (!minesSet) return;
        int numMines = tiles[r][c].getNearMines();

        if (numMines == 0) {

            int numRows = getLength();
            int numCols = getWidth();

            for (int row = r - 1; row <= r + 1; row++) {
                for (int col = c - 1; col <= c + 1; col++) {
                    if (row >= 0 && row < numRows && col >= 0 && col < numCols && tiles[row][col].isClosed()) {
                        reveal(row, col);
                    }
                }
            }
        }
    }
    //Takes in the row and column coordinate and then flags the tile
    public void flag(int r, int c) {
        tiles[r][c].toggleFlag();
    }
    //If the number of flags is equal to number of mines around a tile you can sweep it to open any unflagged tiles around it
    public void sweep(int r, int c) {
        if (getNearFlags(r, c) == getNearMines(r, c) && tiles[r][c].isOpen()) {
            int numRows = getLength();
            int numCols = getWidth();
            for (int row = r - 1; row <= r + 1; row++) {
                for (int col = c - 1; col <= c + 1; col++) {
                    if (row >= 0 && row < numRows && col >= 0 && col < numCols && tiles[row][col].isClosed()) {
                        tiles[row][col].open();
                        sweep(row, col);
                    }
                }
            }
        }
    }
    //Reveals the board after the game has been won or lost
    public void revealAll() {
        for (int r = 0; r < getLength(); r++) {
            for (int c = 0; c < getWidth(); c++) {
                if (tiles[r][c].isFlagged()) {
                    flag(r, c);
                }
                tiles[r][c].open();
            }
        }
    }
    //Draws the board for Minesweeper
    public void draw(PApplet p) {
        if (gameStart && !minesSet) {
            int numMines = getTotalMines();
            int numRows = getLength();
            int numCols = getWidth();
            Random rand = new Random();
            while (numMines > 0) {
                int row = rand.nextInt(numRows);
                int col = rand.nextInt(numCols);
                if (!tiles[row][col].isMine() && !tiles[row][col].isOpen()) {
                    setMines(row, col);
                    numMines--;
                }
            }
            int r = -1;
            int c = -1;
            for (int row = 0; row < getLength(); row++) {
                for (int col = 0; col < getWidth(); col++) {
                    if (tiles[row][col].isOpen()) {
                        r = row;
                        c = col;

                    }
                    tiles[row][col].setNearMines(getNearMines(row, col));
                    System.out.println("position = " + row + ", " + col);
                    System.out.println("getNearMines = " + getNearMines(row, col));
                }
            }
            //Lets the reveal function work because you reveal any zeroes around the clicked tile to open.
            minesSet = true;
            if (r >= 0 && c >= 0) {
                reveal(r, c);

                for (int row = r - 2; row <= r; row++) {
                    for (int col = c - 2; col <= c; col++) {
                        if (row >= 0 && col >= 0 && row < getLength() && col < getWidth()) {
                            if (tiles[row][col].getNearMines() == 0) {
                                reveal(row, col);
                            }
                        }
                    }
                }
            }
        }
        //Colors the tile based off of whether they are open, flagged, a mine, etc.
        for (int r = 0; r < getLength(); r++) {
            for (int c = 0; c < getWidth(); c++) {

                if (isWon() || isLost()) {
                    revealAll();
                }

                if (tiles[r][c].isClosed()) {
                    p.fill(100);
                }

                if (tiles[r][c].isOpen()) {
                    p.fill(100, 255, 0);

                }

                if (tiles[r][c].isFlagged()) {
                    p.fill(204, 102, 0);
                }

                if (tiles[r][c].isMine() && tiles[r][c].isOpen()) {
                    p.fill(255, 0, 0);
                }
                p.rect(r * tileSize, c * tileSize, tileSize, tileSize);

                if (tiles[r][c].isOpen()) {
                    p.fill(0);
                    if (!tiles[r][c].isMine()) {
                        int numMines = tiles[r][c].getNearMines();
                        if (numMines != 0) {
                            p.text(numMines, r * tileSize + tileSize / 2, c * tileSize + tileSize - (int) ((tileSize / 10.0 - 1) * 2));
                        }
                    }
                }
            }
        }
    }
}
