import processing.core.PApplet;
import processing.core.PFont;

public class Minesweeper extends PApplet {
    public static void main(String[] args) {
        PApplet.main(new String[]{Minesweeper.class.getName()});
    }
    //Instance variable for the board
    private Board board;
    //Instance variable for the timer
    private Timer timer;
    //Boolean that records whether an option has been chosen from the difficulty menu
    private boolean clicked;
    //Constructer for the minesweeper class
    public Minesweeper() {  
        board = new Board(0, 0, 0);
        timer = new Timer(this);
        clicked = false;
    }
    //Sets the size of the difficulty menu
    public void settings() {
        if (!clicked) {
            size(500, 500);
        }
    }
    //Sets the background color and makes it so that the window can be resized
    public void setup() {
        surface.setResizable(true);
        background(152, 190, 100);
    }
    //Draws the board and creates all the text that shows up on the screen
    public void draw() {
        frameRate(60);
        PFont f = createFont("Arial", board.getTileSize(), true);
        stroke(175);
        textFont(f);
        fill(0);
        textAlign(CENTER);

        //Creates the difficulty selection menu
        if (!clicked) {
            float w = width / 2 - 125;
            fill(255);
            rect(w, height - 475, 250, 100, 20);
            rect(w, height - 300, 250, 100, 20);
            rect(w, height - 125, 250, 100, 20);
            fill(0);
            text("Beginner", width/2, height - 410);
            text("Intermediate", width/2, height - 240);
            text("Expert", width/2, height - 65);
        }
        //Creates the board for Minesweeper after difficulty has been selected
        if (clicked) {
            background(152, 190, 100);
            surface.setSize(board.getLength() * board.getTileSize(), board.getWidth() * board.getTileSize() + 100);
            //The text for the # of mines, the timer, and the Lost or Won screens
            if (!board.isLost() && !board.isWon()) {
                text(timer.getTime() / 1000, (float) width / 2 + 100, height - 55);
                text(board.getTotalMines() - board.getTotalFlags(), (float) width / 2 - 100, height - 55);
            } else if (board.isLost()) {
                text(timer.getTime() / 1000, (float) width / 2 + 100, height - 55);
                text(board.getTotalMines() - board.getTotalFlags(), (float) width / 2 - 100, height - 55);
                text("YOU LOST!", (float) width / 2, height - 5);
            } else if (board.isWon()) {
                text(timer.getTime() / 1000, (float) width / 2 + 100, height - 55);
                text(board.getTotalMines() - board.getTotalFlags(), (float) width / 2 - 100, height - 55);
                text("YOU WON!", (float) width / 2, height - 5);
            }
            board.draw(this);
        }

    }

    boolean started = false;
    //Checks if the mouse has been clicked
    public void mouseClicked() {
        int x = mouseX;
        int y = mouseY;
        //Lets user select difficulty
        if(!clicked){
            if(x>=width / 2 - 125 && x<=width / 2 + 125){
                if(y>=height-475 && y<=height-375){
                    board = new Board(9, 9, 10);
                    clicked = true;
                }
                if(y>=height-300 && y<=height-200){
                    board = new Board(16, 16, 40);
                    clicked = true;
                }
                if(y>=height-125 && y<=height-25){
                    board = new Board(16, 30, 99);
                    clicked = true;
                }
            }
        }
        //Lets user play Minesweeper
        else{
            if (x <= board.getLength() * board.getTileSize() && y <= board.getWidth() * board.getTileSize()) {
                //If left button is clicked it opens a tile
                if (mouseButton == LEFT) {
                    int r = x / board.getTileSize();
                    int c = y / board.getTileSize();
                    board.reveal(r, c);
                    if (!started) {
                        board.reveal(r, c);
                        board.start();
                        for (int row = r - 1; row <= r + 1; row++) {
                            for (int col = c - 1; col <= c + 1; col++) {
                                if (row >= 0 && col >= 0 && row < board.getLength() && col < board.getWidth()) {
                                    board.reveal(row, col);
                                }
                            }
                        }
                        started = true;
                    }
                }
                if (mouseButton == RIGHT) {
                    board.flag(x / board.getTileSize(), y / board.getTileSize());
                }
                if (mouseButton == CENTER) {
                    board.sweep(x / board.getTileSize(), y / board.getTileSize());
                }
            }

            if (timer.getEndTime() == 0 && (board.isLost() || board.isWon())) {
                timer.end();
            } else if (timer.getTime() == 0 && started) {
                timer.reset();
                timer.start();
            }
        }
    }
    //Checks if a key has been pressed
    public void keyPressed() {
        int x = mouseX;
        int y = mouseY;
        if (clicked) {
            if (mouseY <= board.getWidth() * board.getTileSize()) {
                //Flags and sweeps a tile using the space bar
                if (started && (key == ' ') && !board.isLost()) {
                    board.flag(x / board.getTileSize(), y / board.getTileSize());
                    board.sweep(x / board.getTileSize(), y / board.getTileSize());
                }
                if (timer.getEndTime() == 0 && (board.isLost() || board.isWon())) {
                    timer.end();
                }
                //Opens a tile using the keyboard
                if (key == 'c') {
                    int r = x / board.getTileSize();
                    int c = y / board.getTileSize();
                    board.reveal(r, c);
                    if (!started) {
                        board.reveal(r, c);
                        board.start();
                        for (int row = r - 1; row <= r + 1; row++) {
                            for (int col = c - 1; col <= c + 1; col++) {
                                if (row >= 0 && col >= 0 && row < board.getLength() && col < board.getWidth()) {
                                    board.reveal(row, col);
                                }
                            }
                        }
                        started = true;
                    }
                }
            }
            //Resets the board
            if (key == 'r') {
                timer.end();
                timer.reset();
                board = new Board(board.getWidth(), board.getLength(), board.getTotalMines());
                started = false;
            }
            //Goes back to the difficulty selection menu
            if(key == 'm'){
                timer.end();
                timer.reset();
                started = false;
                clicked = false;
                surface.setSize(500, 500);
            }
        }
    }
}
//@TODO recursive sweep, reveal all,
