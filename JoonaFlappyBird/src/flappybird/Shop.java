package flappybird;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.animation.PauseTransition;
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
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Program name: Shop Class
 * Programmer: Joona Huomo
 * Date: January 31st 2021
 * Description: Shop class that contains the shop, all the buyable skins, payment, and buttons for the shop
 */

public class Shop {

	// all fxml stuff is linked, and gc is declared
	Scene shopScene;
	GraphicsContext gc;
	@FXML
	Canvas shopCanvas;
	@FXML
	Canvas shop1, shop2, shop3, shop4, shop5, shop6;

	@FXML
	Button button1, button2, button3, button4, button5, button6;

	@FXML
	Label currentlyEquippedText;

	// String currentSkin is set to default
	static String currentSkin = "Default";

	// creates new score called score
	Score score;

	// creates static stage secondaryStage
	static Stage secondaryStage;

	// method that sets the currently equipped text to what is currently equipped
	public void whatIsEquipped() {

		// attempts to read the current skin from the skin file
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/current_skin.txt"));
			String skinType = reader.readLine();
			reader.close();
			currentlyEquippedText.setText("Currently Equipped: " + skinType);
		} catch (IOException e1) {
		}
	}

	// method that updates the coins value in the top corner
	public void updateCoins() {

		// sets background of the shop, and reloads everything in the shop
		setBackground("images/daybackground.png");
		setShop();

		// tries to display the amount of coins to the scoreboard
		try {
			score.displayCoins();
		} catch (IOException e) {
		}
	}

	// method that gets the number of coins that the user has
	public int getCoins() throws IOException {

		// reads the number of coins in the coins file
		BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/coins.txt"));
		String insideTxt = reader.readLine();
		reader.close();

		// creates prevcoins int equal to 0
		int prevCoins = 0;

		// attempts to set the value from the coins file to prevcoins
		try {
			prevCoins = Integer.parseInt(insideTxt);
		} catch (Exception e) {
		}

		// returns prevcoins
		return prevCoins;
	}

	// gets the current stage, and sets shopscene to it
	public void getScene(Stage primaryStage) {
		shopScene = primaryStage.getScene();
	}

	// method that sets the background of the shop, as well as displays the coins on
	// it
	public void setBackground(String imageName) {

		// creates new image, and draws it onto the gc
		Image image = new Image(imageName);
		gc = shopCanvas.getGraphicsContext2D();
		gc.drawImage(image, 0, 0);

		// creates new score, and draws the coins on it
		score = new Score(shopCanvas, gc);
		try {
			score.displayCoins();
		} catch (IOException e) {
		}
	}

	// method that sets the images onto the shop
	public void setShop() {

		// all images are created from their locations
		Image image = new Image("images/bird1.png");
		Image image1 = new Image("images/greenbird1.png");
		Image image2 = new Image("images/bluebird1.png");
		Image image3 = new Image("images/purplebird1.png");
		Image image4 = new Image("images/invisiblebird1.png");
		Image image5 = new Image("images/zombiebird1.png");

		// all images are drawn onto their respective canvases
		gc = shop1.getGraphicsContext2D();
		gc.drawImage(image, 0, 0);

		gc = shop2.getGraphicsContext2D();
		gc.drawImage(image1, 0, 0);

		gc = shop3.getGraphicsContext2D();
		gc.drawImage(image2, 0, 0);

		gc = shop4.getGraphicsContext2D();
		gc.drawImage(image3, 0, 0);

		gc = shop5.getGraphicsContext2D();
		gc.drawImage(image4, 0, 0);

		gc = shop6.getGraphicsContext2D();
		gc.drawImage(image5, 0, 0);

	}

	// method that runs when a button is clicked
	public void onButtonClick(ActionEvent evt) throws IOException {

		// Stores the label of the button that was clicked
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();

		// if the button that was clicked is return to menu, it will return to the start
		// screen
		if (buttonLabel.equals("Return To Menu")) {

			// gets the current stage and sets it to stage
			Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

			// sets title of stage to flappy bird
			stage.setTitle("Flappy Bird");

			// loads fxml file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
			BorderPane root = (BorderPane) loader.load();

			// creates new scene
			Scene scene = new Scene(root, 750, 768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// gets startscreencontroller
			StartScreenController controller = loader.getController();

			// shows the start screen with the background
			stage.setScene(scene);
			controller.getScene(stage);
			controller.setBackground("images/startScreen4.png");
			stage.setResizable(false);
			stage.show();
		}

		// else the button was one of the buy buttons
		else {

			// stores the id of the button that was clicked in buttonID variable
			String buttonID = clickedButton.getId();

			// if buttonID is button 1, it sets the skin to default, makes purchase sound,
			// and pop up window
			if (buttonID.equals("button1")) {
				sfx("src/sounds/purchaseSound.mp3");
				Bird.setBirdImage("images/bird1.png", "images/bird2up.png", "images/bird3up.png");
				currentSkin = "Default";
				purchaseMade("images/bird1.png");
			}

			// if buttonID is button 2, and have 10 or more coins it sets the skin to green,
			// and subtracts 10 coins, makes purchase sound, and pop up window
			else if (buttonID.equals("button2") && getCoins() >= 10) {
				sfx("src/sounds/purchaseSound.mp3");
				Bird.setBirdImage("images/greenbird1.png", "images/greenbird2up.png", "images/greenbird3up.png");
				currentSkin = "Green";
				minusFunds(10);
				purchaseMade("images/greenbird1.png");
			}

			// if buttonID is button 3, and have 15 or more coins, it sets the skin to blue,
			// and subtracts 15 coins, makes purchase sound, and pop up window
			else if (buttonID.equals("button3") && getCoins() >= 15) {
				sfx("src/sounds/purchaseSound.mp3");
				Bird.setBirdImage("images/bluebird1.png", "images/bluebird2up.png", "images/bluebird3up.png");
				currentSkin = "Blue";
				minusFunds(15);
				purchaseMade("images/bluebird1.png");
			}

			// if buttonID is button 4, and have 25 or more coins, it sets the skin to
			// purple, and subtracts 25 coins, makes purchase sound, and pop up window
			else if (buttonID.equals("button4") && getCoins() >= 25) {
				sfx("src/sounds/purchaseSound.mp3");
				Bird.setBirdImage("images/purplebird1.png", "images/purplebird2up.png", "images/purplebird3up.png");
				currentSkin = "Purple";
				minusFunds(25);
				purchaseMade("images/purplebird1.png");
			}

			// if buttonID is button 5, and have 50 or more coins, it sets the skin to
			// invisible, and subtracts 50 coins, makes purchase sound, and pop up window
			else if (buttonID.equals("button5") && getCoins() >= 50) {
				sfx("src/sounds/purchaseSound.mp3");
				Bird.setBirdImage("images/invisiblebird1.png", "images/invisiblebird2up.png",
						"images/invisiblebird3up.png");
				currentSkin = "Invisible";
				minusFunds(50);
				purchaseMade("images/invisiblebird1.png");
			}

			// if buttonID is button 5, and have 100 or more coins, it sets the skin to
			// zombie, and subtracts 10 coins, makes purchase sound, and pop up window
			else if (buttonID.equals("button6") && getCoins() >= 100) {
				sfx("src/sounds/purchaseSound.mp3");
				Bird.setBirdImage("images/zombiebird1.png", "images/zombiebird2up.png", "images/zombiebird3up.png");
				currentSkin = "Zombie";
				minusFunds(100);
				purchaseMade("images/zombiebird1.png");
			}

			// else you do not have enough coins to get that skin, insufficient funds
			// displayed, and sound plays
			else {
				sfx("src/sounds/insufficientFundsSound.mp3");
				currentlyEquippedText.setText("Insufficient Funds!");
			}

			// updates the coins, and skin
			updateCoins();
			updateSkin(currentSkin);

			// 1 second later it updates what is currently equipped
			PauseTransition delay = new PauseTransition(Duration.seconds(1));
			delay.setOnFinished(event -> whatIsEquipped());
			delay.play();
		}
	}

	// method that subtracts an amount from the current amount of coins
	public void minusFunds(int amount) {
		try {

			// reads the amount of coins from the coins file
			BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/coins.txt"));
			String insideTxt = reader.readLine();
			reader.close();

			// creates int prevcoins and sets it to 0
			int prevCoins = 0;

			// attempts to parse the information from the coins file into the prevcoins
			// variable
			try {
				prevCoins = Integer.parseInt(insideTxt);
			} catch (Exception e) {
			}

			// writes the old amount of coins minus the amount of coins to be subtracted
			// into the coins file
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/flappybird/coins.txt"));
			writer.write(Integer.toString(prevCoins - amount));
			writer.close();
		} catch (IOException e) {
		}
	}

	// method that updates the skin
	public void updateSkin(String skinType) {
		try {

			// writes the current skin into the current skin file
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/flappybird/current_skin.txt"));
			writer.write(skinType);
			writer.close();
		} catch (IOException e1) {
		}
	}

	// method that opens the purchase made pop up
	public void purchaseMade(String imagePurchase) {
		try {

			// load the pop up
			Pane keyChange = (Pane) FXMLLoader.load(getClass().getResource("Purchase.fxml"));

			// create a new scene
			Scene keyChange1 = new Scene(keyChange, 300, 300);

			// gets the canvas from the scene
			Canvas purchaseCanvas = (Canvas) keyChange.getChildren().get(1);

			// stage to put scene in
			secondaryStage = new Stage();
			secondaryStage.setScene(keyChange1);
			secondaryStage.setResizable(false);

			// sets the image of the skin that was purchased onto the canvas
			Image imagePurchased = new Image(imagePurchase);
			gc = purchaseCanvas.getGraphicsContext2D();
			gc.drawImage(imagePurchased, 0, 0);
			secondaryStage.showAndWait();
		} catch (Exception e) {
		}
	}

	// method that closes the secondarystage
	public void okClose() {
		secondaryStage.close();
	}

	// makes new static mediaplayer mediaplayer
	static MediaPlayer mediaPlayer;

	// method for a sound effect
	public static void sfx(String s) {

		// takes s, and sets it's path to media h
		Media h = new Media(Paths.get(s).toUri().toString());

		// new mediaplayer that plays the sound once at 0.06 volume
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setVolume(0.06);
		mediaPlayer.play();
	}
}
