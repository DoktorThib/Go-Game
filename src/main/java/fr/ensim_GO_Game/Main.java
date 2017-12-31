package fr.ensim_GO_Game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
		
    private HBox mainlayout;
    private GoController go;
    private SideController sideController;

	public static void main(String[] args) {
       launch(args);
	}

    @Override
    public void init(){
        //initialize mainlayout and reverse control
        mainlayout = new HBox();
        go = new GoController();
        mainlayout.getChildren().add(go);

        sideController = new SideController(go);
        mainlayout.getChildren().add(sideController);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        // set title scene and display scene
        primaryStage.setTitle("Go");
        Scene scene = new Scene(mainlayout, 900, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("go.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
