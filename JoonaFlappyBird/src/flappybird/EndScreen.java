package flappybird;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Program name: End Screen
 * Programmer: Joona Huomo 
 * Date: January 31st 2021
 * Description: End screen class which contains everything that goes on the end screen, such as the score, and buttons that will move the user to the next screen, or quit.
 */

public class EndScreen {

	// all things linked from fxml
	@FXML
	Canvas endCanvas;
	Scene endScene;
	GraphicsContext gc;
	@FXML
	Label endPoints, highScore;
	@FXML
	Canvas gameOver;

	Score score;

	// method that gets the current scene and sets it to endscene
	public void getScene(Stage primaryStage) {
		endScene = primaryStage.getScene();
	}

	// method that sets the background to an image, and draws the points that was
	// ended with, as well as the game over canvas
	public void setBackground(Image image) {

		// sets endcanvas to gc and draws the image onto it
		gc = endCanvas.getGraphicsContext2D();
		gc.drawImage(image, 0, 0);

		// sets the end points text, as well as the score
		endPoints.setText("You ended with: " + FlappyBirdController.points);
		score = new Score(endCanvas, gc);

		// displays the amount of coins that the user has onto the screen
		try {
			score.displayCoins();
		} catch (IOException e) {
		}

		// puts the game over image onto the canvas
		Image gameOverText = new Image("images/game_over.png");
		gc = gameOver.getGraphicsContext2D();
		gc.drawImage(gameOverText, 0, 0);

		// puts the high score onto the end screen from the file
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/high_score.txt"));
			String insideTxt = reader.readLine();
			reader.close();
			highScore.setText("Highscore: " + insideTxt);
		} catch (IOException e) {
		}
	}

	// method that runs when a button is clicked, and does an action based on which
	// button was clicked, play again, quit, or return to menu
	public void onButtonClick(ActionEvent evt) throws IOException {

		// Stores the label of the button that was clicked
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();

		// if the label is retry, it restarts the game
		if (buttonLabel.equals("Retry")) {

			// stops end screen music
			stopMusic();

			// sets played to false, fall to false, bird is dead to false, points to 0, and
			// speed to 0
			FlappyBirdController.played = false;
			FlappyBirdController.fall = false;
			FlappyBirdController.birdIsDead = false;
			FlappyBirdController.points = 0;
			Pipes.speed = 6;

			// gets the stage, and sets it to stage
			Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

			// sets stage title to Flappy Bird
			stage.setTitle("Flappy Bird");

			// loads the game
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FlappyBird.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene sceneTwo = new Scene(root, 750, 768);

			// gets the controller
			FlappyBirdController controller = loader.getController();

			// sets the scene on the stage to the game scene
			stage.setScene(sceneTwo);

			// sets the scene from the controller to the stage, starts game loop, and music,
			// then shows it
			controller.getScene(stage);
			controller.gameLoop();
			controller.music();
			stage.show();
		}

		// if the label is Quit, then exits out
		else if (buttonLabel.equals("Quit")) {
			Platform.exit();
		}

		// if the label is return to menu it will return to the menu
		else if (buttonLabel.equals("Return To Menu")) {

			// stops end screen music and starts main screen music
			stopMusic();
			Main.music();

			// sets played to false, fall to false, bird is dead to false, points to 0, and
			// speed to 0
			FlappyBirdController.played = false;
			FlappyBirdController.fall = false;
			FlappyBirdController.birdIsDead = false;
			FlappyBirdController.points = 0;
			Pipes.speed = 6;

			// gets the stage, and sets it to stage
			Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

			// sets stage title to Flappy Bird
			stage.setTitle("Flappy Bird");

			// loads the game
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root, 750, 768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// gets the controller of the start screen
			StartScreenController controller = loader.getController();

			// sets the scene on the stage to the start scene
			stage.setScene(scene);
			controller.getScene(stage);

			// sets the background of the start scene, and shows the scene
			controller.setBackground("images/startScreen4.png");
			stage.setResizable(false);
			stage.show();
		}
	}

	// static mediaplayer created
	static MediaPlayer mediaPlayer;

	// method that plays end screen music indefinitely
	public void music() {

		// sets the path of the end screen music file to s
		String s = "src/sounds/endScreenMusic.mp3";

		// sets media to the path of s
		Media h = new Media(Paths.get(s).toUri().toString());

		// makes new mediaplayer, and sets it to h, sets volume to 0.05, duration to the
		// total duration of the song, and makes it play indefinitely
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setVolume(0.05);
		mediaPlayer.setStartTime(Duration.seconds(0));
		mediaPlayer.setStopTime(Duration.seconds(63));
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}

	// method that stops music from being played
	public void stopMusic() {
		mediaPlayer.stop();
	}
}
