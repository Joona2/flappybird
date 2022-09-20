package flappybird;
	
import java.io.File;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.fxml.FXMLLoader;

/*
 * Program name: Main Class
 * Programmer: Joona Huomo 
 * Date: January 31st 2021
 * Description: Main class that boots up the game, and starts the start screen, and plays start screen music
 */

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		music();
		try {
			
			//stage title is set to flappy bird
			primaryStage.setTitle("Flappy Bird");
			
			//loads fxml file for start screen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
			BorderPane root = (BorderPane) loader.load();
					
			//makes new scene
			Scene scene = new Scene(root,750,768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//loads start screen controller
			StartScreenController controller = loader.getController();
			
			//sets the start screen scene to the stage
			primaryStage.setScene(scene);
			controller.getScene(primaryStage);
			
			//sets the background of the start screen to start screen background 4.0 since i had to edit it many times due to finding out that the game was too big to fit on Mr. Holik's screen
			controller.setBackground("images/startScreen4.png");
			primaryStage.setResizable(false);
			primaryStage.show();
			
			//runs the changeKey method which will take user input of the key that they would like to use
			controller.changeKey();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//makes new static mediaplayer
	static MediaPlayer mediaPlayer;
	
	//method that plays the start screen music
	public static void music() {
		
		// gets path to start screen music, and sets it to media h
		String s = "src/sounds/startScreenMusic.mp3";
		Media h = new Media(Paths.get(s).toUri().toString());
		
		// creates new mediaplayer set to h, and volume set to 0.03, playing indefinitely
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setVolume(0.03);
		mediaPlayer.setStartTime(Duration.seconds(0));
		mediaPlayer.setStopTime(Duration.seconds(63));
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}
	
	// method that stops the music
	public static void stopMusic() {
		mediaPlayer.stop();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
