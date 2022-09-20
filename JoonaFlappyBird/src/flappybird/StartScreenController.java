package flappybird;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Program name: Start Screen Controller
 * Programmer: Joona Huomo
 * Date: January 31st 2021
 * Description: Decides which scene to move to next, depending on if the user chose to Play, quit, go to the store, or look at the rules
 */

public class StartScreenController {

	// creates the secondary stage for pop-up windows
	static Stage secondaryStage;

	// creates scene and gc
	Scene startScene;
	GraphicsContext gc;

	// links canvas and label from fxml file
	@FXML
	Canvas startCanvas;
	@FXML
	Label highScore;

	// creates new score
	Score score;

	boolean rules = false;

	// gets the current stage, and sets startscene to it
	public void getScene(Stage primaryStage) {
		startScene = primaryStage.getScene();
	}

	// method that draws the background, and coins, and the high score
	public void setBackground(String imageName) {

		// new image background gets drawn onto the gc
		Image image = new Image(imageName);
		gc = startCanvas.getGraphicsContext2D();
		gc.drawImage(image, 0, 0);

		// coins get displayed
		score = new Score(startCanvas, gc);
		try {
			score.displayCoins();
		} catch (IOException e) {
		}

		// reads the high score from the high score file, and displays it
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/high_score.txt"));
			String insideTxt = reader.readLine();
			reader.close();
			highScore.setText("Highscore: " + insideTxt);
		} catch (IOException e) {
		}
	}

	// method that changes the scene depending on what button was pressed
	public void changeSceneButtonHandler(ActionEvent evt) throws IOException {

		// Stores the label of the button that was clicked
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();

		// if the label is start, it goes to game screen
		if (buttonLabel.equals("Start")) {

			// stops the start screen music
			Main.stopMusic();

			// creates a new stage called flappy bird
			Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
			stage.setTitle("Flappy Bird");

			// loads the flappy bird fxml file onto the scene
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FlappyBird.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene sceneTwo = new Scene(root, 750, 768);

			// gets the flappy bird controller
			FlappyBirdController controller = loader.getController();

			// makes the gameloop, starts the game music, and shows the scene
			stage.setScene(sceneTwo);
			controller.getScene(stage);
			controller.music();
			controller.gameLoop();
			stage.show();

		}

		// if the label is shop, it goes to shop screen
		if (buttonLabel.equals("Shop")) {

			// creates a new stage called flappy bird shop
			Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
			stage.setTitle("Flappy Bird Shop");

			// loads the shop fxml file onto the scene
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Shop.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene sceneTwo = new Scene(root, 750, 768);

			// gets the shop controller
			Shop controller = loader.getController();

			// sets shop background, sets shop images, and sets what is equipped, and then
			// shows the scene
			stage.setScene(sceneTwo);
			controller.setBackground("images/daybackground.png");
			controller.setShop();
			controller.whatIsEquipped();
			controller.getScene(stage);
			stage.show();
		}

		// if the label is change key, then runs change key
		else if (buttonLabel.equals("Change Key")) {
			changeKey();
		}

		// if the label is Quit, then exits out
		else if (buttonLabel.equals("Quit")) {
			Platform.exit();
		}

		// if label is how to play, goes to game rules
		else if (buttonLabel.equals("How To Play")) {
			try {

				// load the pop up
				Pane rules = (Pane) FXMLLoader.load(getClass().getResource("HowToPlay.fxml"));

				// create a new scene
				Scene rulesScene = new Scene(rules, 600, 600);
				
				// gets the button from the scene
				Button readOutLoud = (Button) rules.getChildren().get(3);
				Image image = new Image("images/readOutLoud.png");
				readOutLoud.setGraphic(new ImageView(image));
				
				// create new stage to put scene in
				secondaryStage = new Stage();
				secondaryStage.setScene(rulesScene);
				secondaryStage.setResizable(false);
				secondaryStage.show();
			} catch (Exception e) {
			}
		}
	}

	// method that runs when the button was changed
	public void changeButton(KeyEvent key) {

		// closes the secondary stage
		secondaryStage.close();

		// sets keypressed to the code of the key that was pressed
		String keyPressed = key.getCode().toString();

		// sets bird.up to keypressed
		Bird.up = keyPressed;
	}

	// method that closes the secondarystage
	public void okClose() {
		mediaPlayer.stop();
		rules = false;
		secondaryStage.close();
	}

	static MediaPlayer mediaPlayer;

	// method that plays the rules sound
	public void readRules() {
		
		// takes s, and sets it's path to media h
		Media h = new Media(Paths.get("src/sounds/howToPlaySound.mp3").toUri().toString());

		// new mediaplayer that plays the sound once at 0.06 volume
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setVolume(0.6);

		// if rules was pressed once, it plays the sound, if it is pressed again, it
		// stops the sound
		if (!rules) {
			mediaPlayer.play();
			rules = true;
		} else {
			mediaPlayer.stop();
			rules = false;
		}
	}

	// method that loads the change key scene
	public void changeKey() {
		try {

			// load the pop up
			Pane keyChange = (Pane) FXMLLoader.load(getClass().getResource("ButtonChange.fxml"));

			// create a new scene
			Scene keyChange1 = new Scene(keyChange, 300, 300);

			// create new stage to put scene in
			secondaryStage = new Stage();
			secondaryStage.setScene(keyChange1);
			secondaryStage.setResizable(false);
			secondaryStage.showAndWait();
		} catch (Exception e) {
		}
	}

}
