package fr.ensim_GO_Game;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Translate;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;



public class GoBoard extends Pane {

    private Rectangle background;

    private GoPiece[][] board;

    private boolean[][] can_reverse;

    private double cell_height;

    private double cell_width;

    private int current_player = 1;

    private InterfaceGoLogic goLogic;


    // makeup of the horizontal and vertical grid lines
    private Line[] horizontal;

    // arrays holding translate objects for the horizontal and vertical grid lines
    private Translate[] horizontal_t;

    // is the game currently in play
    private boolean in_play = true;

    private int passCount = 0;

    // current scores of player 1 and player 2
    private int player1_score;

    private int player2_score;

    // 3x3 array that holds the pieces that surround a given piece
    private int[][] surrounding;

    private Line[] vertical;

    private Translate[] vertical_t;

    GoBoard(){

    	/*Representation of the board*/
        board = new GoPiece [7][7];

        horizontal = new Line [7];
        vertical = new Line [7];

        horizontal_t = new Translate[7];
        vertical_t = new Translate[7];

        surrounding = new int[3][3];

        can_reverse = new boolean[3][3];

        /*Initialization of board*/
        initialiseBoard();
        goLogic = new GoLogic(board);

        initialiseLinesBackground();
        
        resetGame();

    }

    public void endGame() {
        goLogic.endGame();
    }
    /*Get scores from GoLogic*/
    public int [] get_score(){
        int scores []  = new int [2];

        scores[0] = goLogic.playerOneScore();
        scores[1] = goLogic.playerTwoScore();

        return scores;
    }
    /*To complete*/
    private int getPiece(final int x, final int y) {

        int pieceSelected;

        if(x >= 0 && y >= 0 && x <7 && y < 7 ) {
            pieceSelected = board[x][y].getPiece();
        }else {
            pieceSelected = -1;
        }

        return pieceSelected;
    }
    /*Resizing and relocating the horizontal lines*/
    private void horizontalResizeRelocate(final double width) {
        // set a new y on the horizontal lines and translate them into place
        for(int i=0; i<7 ; i++) {
            horizontal_t[i].setY((i + 0.5)* cell_height);
            horizontal[i].setEndX(width - cell_width/2);
            horizontal[i].setStartX(cell_width/2);
        }
    }
    /*Initialize everything in the board array*/
    private void initialiseBoard() {
        //set the array to null object
        for(int i = 0; i < 7; i++)
            for(int j = 0; j < 7; j++) {
                board[i][j] = new GoPiece(i, j, 0);
                getChildren().add(board[i][j]);
            }

    }
    /*Initialize the background and the lines*/
    private void initialiseLinesBackground() {

        //add background color
        background = new Rectangle();
        // TODO: add background
        Image image = new Image("/ensim_GO_Game/assets/space.jpg");
        ImagePattern imagepattern = new ImagePattern(image);
        background.setId("pane");
        background.setFill(imagepattern);


        /*Translate and draw the end line*/
        for(int i =0; i < 7; i++) {
            horizontal[i] = new Line();
            horizontal[i].setStroke(Color.BLACK);
            horizontal[i].setStartX(0);
            horizontal[i].setStartY(0);
            horizontal[i].setEndY(0);

            vertical[i] = new Line();
            vertical[i].setStroke(Color.BLACK);
            vertical[i].setStartX(0);
            vertical[i].setStartY(0);
            vertical[i].setEndX(0);

            horizontal_t[i] = new Translate(0,0);
            horizontal[i].getTransforms().add(horizontal_t[i]);

            vertical_t[i] = new Translate(0,0);
            vertical[i].getTransforms().add(vertical_t[i]);
        }


        getChildren().add(background);

        /*Add the rectangles and lines to the group*/
        for(int i =0; i < 7; i++) {
            getChildren().add(horizontal[i]);
            getChildren().add(vertical[i]);
        }
    }
    /*To complete*/
    private boolean isSuicidePlace(int cellx, int celly, int player) {
        // TODO: check if is it suicide place
        return false;
    }
    public void pass() throws Exception {

        //increment pass count and check if the player have pass 1 after another
        // if yes Game is over
        passCount = passCount +1;
        if(passCount >= 2){
            in_play = false;
        }
        if(passCount >=2)throw new Exception("Game is over");
        swapPlayers();
    }
    /*Resize and relocate all the pieces*/
    private void pieceResizeRelocate() {

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                board[i][j].relocate(i * cell_width, j * cell_height);
                board[i][j].resize(cell_width, cell_height);
            }
        }

    }
    /*Position is taken ? yes or no*/
    public void placePiece(final double x, final double y) throws Exception{
        final int cellx = (int) (x / cell_width);
        final int celly = (int) (y / cell_height);

        // if the game is not in play then throw an exception which display game over
        if(!in_play)throw new Exception("Game is over");



        goLogic.placePiece(cellx, celly, current_player);
        swapPlayers();

        /*init passCount (End of the game)*/
        passCount =0; 
    }
    // private method that will reset the renders
    private void resetBoard() {
        for(int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j].setPiece(0);
            }
        }
    }
    /*Resetting the game*/
    public void resetGame() {

        resetBoard();
        initialiseBoard();

        goLogic = new GoLogic(board);

        this.current_player = 1;
        this.player1_score = 0;
        this.player2_score = 0;
    }
    @Override
    public void resize(double width, double height) {

        super.resize(width, height);

        /*Width and height of a cell*/
        cell_width = width / 7.0;
        cell_height = height / 7.0;

        /*Resize the rectangle to take the full size*/
        background.setWidth(width); background.setHeight(height);

        /*Resize and relocate horizontal line*/
        horizontalResizeRelocate(width);
        verticalResizeRelocate(height);


        /*Warning : Reset the sizes and positions of all XOPieces that were placed*/
        pieceResizeRelocate();
    }
    private void swapPlayers() {
        this.current_player = current_player == 1 ? 2 : 1;
    }
    /*To complete*/
    private void updateScores() {

        player1_score = 0;
        player2_score = 0;

        for(int i = 0; i < board.length ;i++) {
            for(int j = 0; j < board[i].length; j++) {

                if(this.board[i][j].getPiece() == 1) {
                    player1_score ++;

                }else if (this.board[i][j].getPiece() == 2) {
                    player2_score ++;

                }
            }
        }

        // TODO: update label view

    }
    /*Resizing and relocating the vertical lines*/
    private void verticalResizeRelocate(final double height) {
        for(int i=0; i<7 ; i++) {
            vertical_t[i].setX((i + 0.5)* cell_width);
            vertical[i].setEndY(height -cell_height/2);
            vertical[i].setStartY(cell_height/2);
        }
    }

}
