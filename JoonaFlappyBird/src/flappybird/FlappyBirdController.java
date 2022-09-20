package flappybird;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Program name: Flappy Bird Controller
 * Programmer: Joona Huomo 
 * Date: January 31st 2021
 * Description: Flappy bird class that contains the game, draws everything onto the screen, moves everything, handles points, death, and collision
 */

public class FlappyBirdController {

	// linking things from fxml file
	@FXML
	Canvas gameCanvas;
	GraphicsContext gc;
	Scene gameScene;

	// makes static snapshot
	static WritableImage snapshot;

	// declaring booleans to be used
	static boolean birdIsDead = false;
	boolean collidedPipe = false;
	boolean collidedFloor = false;
	boolean clicked = false;
	boolean buttonPressed = false;

	// randomly chosen background
	int randomBackground = (int) (Math.random() * 2);
	
	//bird flap wings int
	int birdFlap = 0;

	// static points declared
	static int points = 0;

	// count declared
	int count = 0;

	// speed change boolean created
	boolean speedChange = false;

	// pipepoint boolean array made
	boolean[] pipePoint = new boolean[3];

	// static booleans for played and fall made
	static boolean played = false;
	static boolean fall = false;
	
	// method that gets the current stage, and sets gameScene to it
	public void getScene(Stage primaryStage) {
		gameScene = primaryStage.getScene();
	}

	// gameloop method that runs the game
	public void gameLoop() {
		
		// sets gc to the 2dgraphicscontext of game canvas
		gc = gameCanvas.getGraphicsContext2D();
	
		// long buttonclick variable made, and set to the current time millis
		Long buttonClick = System.currentTimeMillis();

		// arraylist for inputs is made
		ArrayList<String> input = new ArrayList<String>();

		// declares bird, floor, and score
		Bird bird = new Bird(gc, gameCanvas, input);
		Floor floor = new Floor(gc, gameCanvas);
		Score score = new Score(gameCanvas, gc);

		// arraylist for pipes is made
		ArrayList<Pipes> pipesList = new ArrayList<Pipes>();

		// if it is the first time running through game loop
		if (count == 0) {

			// for loop that runs through 3 times, and creates 3 pipes setting them to the
			// pipes arraylist and giving them random y values
			for (int i = 0; i < 3; i++) {
				pipesList.add(new Pipes(gc, gameCanvas));
				pipesList.get(i).pipesY();
			}

			// sets the positions of the 2nd and 3rd pipes and adds 1 to count
			pipesList.get(1).setX(1086);
			pipesList.get(2).setX(1422);
			count++;
		}

		// checks the skin of the bird
		checkSkin(bird);

		// starts the animation
		new AnimationTimer() {
			// actual game loop that repeats
			@Override
			public void handle(long currentNanoTime) {

				//if button to start game has not been pressed
				if(!buttonPressed) {
				
				//displays the background, as well as the press button to start on the canvas
				drawBackground(gc);
				pressButtonToStart();
				floor.draw();
				
				//if and else which make the bird flap its wings
				if (birdFlap<=3) {
				gc.drawImage(Bird.imageUp1, 100.0, 300.0);
				birdFlap++;
				}
				
				else {
				gc.drawImage(Bird.imageUp2, 100.0, 300.0);
				birdFlap++;
				if (birdFlap>=6) {
					birdFlap=0;
				}
				}
				
				//when a key is pressed, the game will start
				gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent e) {
					buttonPressed = true;
					clicked = true;
					
					String code = e.getCode().toString();
					input.add(code);
					// makes sure that you cannot just hold down the up button, you actually have to
					// click it constantly
					checkTimeBetweenButtonClicks(bird, buttonClick);
						}
					});
				}
				
				//if button to start has been pressed
				if(buttonPressed) {
				// clear the whole canvas each frame
				gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

				drawBackground(gc);

				// if the bird is not dead
				if (!birdIsDead) {
					// when key is pressed
					gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
						@Override
						public void handle(KeyEvent e) {

							// key that was pressed is changed into a string
							String code = e.getCode().toString();
							// if the input doesn't already contain that code it gets added to the input
							// arraylist
							if (!input.contains(code))
								input.add(code);

							// clicked is set to true
							clicked = true;
						}
					});

					// when key is released
					gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
						@Override
						public void handle(KeyEvent e) {

							// key that was released is changed into a string
							String code = e.getCode().toString();

							// if the code is already in the input arraylist it gets removed
							if (input.contains(code))
								input.remove(code);

							// clicked is set to false
							clicked = false;
						}

					});
					// for loop that runs through once for each set of pipes, and gets their x
					// values, if they are between 200 and 750, then pipepoint at that pipe gets set
					// to false, then pipes are moved, if pipepoint at i is false, then it gives a
					// point, plays point sound, and sets pipepoint to true, and for every 5 times
					// it runs through, speed increases by 1
					for (int i = 0; i < 3; i++) {
						if (pipesList.get(i).getX() >= 200 && pipesList.get(i).getX() <= 750) {
							pipePoint[i] = false;
						}
						pipesList.get(i).move();
						if (!pipePoint[i]) {
							if (pipesList.get(i).getX() <= 90) {
								sfx("src/sounds/pointSound.mp3");
								points++;
								pipePoint[i] = true;
								if (points % 5 == 0) {
									speedChange = true;
								}
							}
						}
					}

					// makes sure that you cannot just hold down the up button, you actually have to
					// click it constantly
					checkTimeBetweenButtonClicks(bird, buttonClick);

					// if speedchange is true, then the speed increases by 1 and speedchange gets
					// set to false
					if (speedChange) {
						Pipes.speed += 1;
						speedChange = false;
					}

					// moves the floor
					floor.move();

					// if bird collided with the floor, then bird is dead is set to true, death
					// sound is played, and gravity is activated
					collidedFloor = bird.collisionFloor(floor);
					if (collidedFloor) {
						sfx1("src/sounds/deathSound.mp3");
						birdIsDead = true;
						bird.gravity();
					}

					// for the amount of pipes, gets the pipe at i, if that pipe had collided with
					// the bird, then bird is dead is set to true, death sound is played, and
					// gravity is activated
					for (int i = 0; i < 3; i++) {

						Pipes pipe = pipesList.get(i);
						collidedPipe = bird.collisionPipesTop(pipe);
						if (collidedPipe) {
							sfx1("src/sounds/deathSound.mp3");
							birdIsDead = true;
							bird.gravity();
						}
						collidedPipe = bird.collisionPipesBot(pipe);
						if (collidedPipe) {
							sfx1("src/sounds/deathSound.mp3");
							birdIsDead = true;
							bird.gravity();
						}
					}

					// points are updated
					score.display(points);

				}

				// else the bird is dead
				else {

					// if fall is false
					if (!fall) {

						// high score gets set to the file if it is higher than past high score
						try {
							BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/high_score.txt"));
							String insideTxt = reader.readLine();
							reader.close();
							if (points > Integer.parseInt(insideTxt)) {
								BufferedWriter writer = new BufferedWriter(
										new FileWriter("src/flappybird/high_score.txt"));
								writer.write(Integer.toString(points));
								writer.close();
							}
						} catch (IOException e1) {
						}

						// coins are updated onto coin file
						try {
							BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/coins.txt"));
							String insideTxt = reader.readLine();
							reader.close();

							int prevCoins = 0;
							try {
								prevCoins = Integer.parseInt(insideTxt);
							} catch (Exception e) {
							}

							BufferedWriter writer = new BufferedWriter(new FileWriter("src/flappybird/coins.txt"));
							writer.write(Integer.toString(points + prevCoins));
							writer.close();
						} catch (IOException e) {
						}

						// bird falling sound is played and fall is set to true
						sfx2("src/sounds/fallingSound.mp3");
						fall = true;
					}

					// main game music is stopped
					mediaPlayer.stop();

					// draws each pipe, but doesn't move them
					for (int i = 0; i < 3; i++) {
						pipesList.get(i).draw();
					}

					// bird falls
					bird.gravity();

					// draws floor, but doesn't move it
					floor.draw();

					// gets the stage, and takes a snapshot of it
					Stage stage = (Stage) gameScene.getWindow();
					snapshot = stage.getScene().snapshot(null);

					// 2 seconds later, the game will get stopped
					PauseTransition delay = new PauseTransition(Duration.seconds(2));
					delay.setOnFinished(event -> stop());
					delay.play();

					// 2 seconds later, it will go to the end screen
					PauseTransition delay2 = new PauseTransition(Duration.seconds(2));
					delay2.setOnFinished(event -> {
						try {
							goToEnd(stage);
						} catch (IOException e) {
						}
					});
					delay2.play();
				}

			}}
		}.start();

	}

	// method that switches the game to the end screen
	public void goToEnd(Stage stage) throws IOException {

		stage.setTitle("End Screen");

		// loads end screen
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EndScreen.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene sceneTwo = new Scene(root, 750, 768);

		// loads end screen controller
		EndScreen controller = loader.getController();

		// if played is false, then plays end screen music, and sets played to true
		if (!played) {
			controller.music();
			played = true;
		}

		stage.setScene(sceneTwo);

		// sets the scene to end scene, and sets the background of the end scene to the
		// snapshot of the bird death screen
		controller.getScene(stage);
		controller.setBackground(snapshot);
		stage.show();

	}

	// method that makes sure that there is a certain amount of time between each
	// button click
	private void checkTimeBetweenButtonClicks(Bird bird, long buttonClick) {

		// long difference made that gets the current time, and subtracts the time when
		// the button was clicked, and divides it by 300
		long difference = (System.currentTimeMillis() - buttonClick) / 300;

		// if the difference is less than 0.001, and clicked is true, then the bird will
		// move up, and flap sound will play, and clicked will be set to false
		if (difference >= .001 && clicked) {
			sfx3("src/sounds/flapWings.mp3");
			clicked = false;
			bird.move();
		}

		// else, bird falls down
		else {
			bird.gravity();
		}
	}

	// mediaplayer declared
	MediaPlayer mediaPlayer;

	// method that plays music
	public void music() {

		// gets path to background music, and sets it to media h
		String s = "src/sounds/backGroundMusic.mp3";
		Media h = new Media(Paths.get(s).toUri().toString());

		// creates new mediaplayer set to h, and volume set to 0.2, playing indefinitely
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setVolume(0.2);
		mediaPlayer.setStartTime(Duration.seconds(0));
		mediaPlayer.setStopTime(Duration.seconds(63));
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}

	// mediaplayer2 declared
	MediaPlayer mediaPlayer2;

	// method to run the 1st sound effect
	public void sfx(String s) {

		// gets path to sound effect from parameters, and sets it to media h
		Media h = new Media(Paths.get(s).toUri().toString());

		// creates new mediaplayer2 set to h, and volume set to 0.1, playing once
		mediaPlayer2 = new MediaPlayer(h);
		mediaPlayer2.setVolume(0.1);
		mediaPlayer2.play();
	}

	// mediaplayer3 declared
	MediaPlayer mediaPlayer3;

	// method to run the 2nd sound effect
	public void sfx1(String s) {

		// gets path to sound effect from parameters, and sets it to media h
		Media h = new Media(Paths.get(s).toUri().toString());

		// creates new mediaplayer3 set to h, and volume set to 0.1, playing once
		mediaPlayer3 = new MediaPlayer(h);
		mediaPlayer3.setVolume(0.1);
		mediaPlayer3.play();
	}

	// mediaplayer4 declared
	MediaPlayer mediaPlayer4;

	// method to run the 3rd sound effect
	public void sfx2(String s) {

		// gets path to sound effect from parameters, and sets it to media h
		Media h = new Media(Paths.get(s).toUri().toString());

		// creates new mediaplayer4 set to h, and volume set to 0.1, playing once
		mediaPlayer4 = new MediaPlayer(h);
		mediaPlayer4.setVolume(0.1);
		mediaPlayer4.play();
	}

	// mediaplayer5 declared
	MediaPlayer mediaPlayer5;

	// method to run the 4th sound effect
	public void sfx3(String s) {

		// gets path to sound effect from parameters, and sets it to media h
		Media h = new Media(Paths.get(s).toUri().toString());

		// creates new mediaplayer4 set to h, and volume set to 0.1, playing once
		mediaPlayer5 = new MediaPlayer(h);
		mediaPlayer5.setVolume(0.1);
		mediaPlayer5.play();
	}

	// method that checks the skin of the bird
	public void checkSkin(Bird bird) {

		// reads the skin document and sets the skin to whatever is on that document
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/current_skin.txt"));
			String skinType = reader.readLine();
			reader.close();

			bird.setSkin(skinType);

		} catch (IOException e) {
		}
	}
	
	//method that draws press button to start onto the screen
	public void pressButtonToStart() {
		gc.setFont(Font.font("System", FontWeight.BOLD, 36));
		gc.setFill(Color.ORANGE);
		gc.fillText("Press Any Button To Start!", 150, 150);
	}
	
	//method that draws background
	public void drawBackground(GraphicsContext gc) {
		// day or night background is chosen based on the random background variable
		Image background = new Image("images/daybackground.png");
		if (randomBackground == 0) {
			background = new Image("images/daybackground.png");
		} else {
			background = new Image("images/nightbackground.png");
		}

		// background is drawn
		gc.drawImage(background, 0, 0);
	}
}
