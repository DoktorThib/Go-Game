package fr.ensim_GO_Game;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class GoController  extends Control {


    // constructor for the class
    public GoController() {
        //generate the board game and add Go controls
        setSkin(new GoControllerSkin(this));
        rb_board = new GoBoard();
        rb_board.setPrefSize(600, 600);
        getChildren().add(rb_board);


        //Mouse event handler
        setOnMouseClicked(new EventHandler <MouseEvent> () {
            //when mouse is click get X and Y coordinate of the click
        	
            public void handle (MouseEvent event) {
                try{
                    rb_board.placePiece(event.getX(),event.getY());

                    SideController.update_display();
                }catch (Exception e){
                    SideController.update_display(e.getMessage());
                }

            }

        });

        //keyboard event handler
        setOnKeyPressed(new EventHandler<KeyEvent>() {

            // when player select space reset the game
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.SPACE)
                    rb_board.resetGame();
            }
        });

    }

    public void setSideControl(SideController SideController) {
        this.SideController = SideController;
    }

    // overridden version of the resize method
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        rb_board.resize(width, height);
    }

    //the player pass
    public void pass() throws Exception {
        rb_board.pass();
    }

    //update score
    public int [] update_score(){
        return rb_board.get_score();
    }

    public void endGame() {
        rb_board.endGame();
    }

    //the player click on reset the game
    public void reset(){
        rb_board.resetGame();
    }

    // private fields of a go board
    GoBoard rb_board;
    SideController SideController;
}
