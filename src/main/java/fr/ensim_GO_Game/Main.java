package fr.ensim_GO_Game;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
		
    private HBox mainlayout;
    private GoController go;
    private SideController sideController;;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

    @Override
    public void init(){
        //initialise mainlayout and reversi control
        mainlayout = new HBox();
        go = new GoController();
        mainlayout.getChildren().add(go);

        sideController = new SideController(go);
        mainlayout.getChildren().add(sideController);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
    	/*To complete*/

    }

}
