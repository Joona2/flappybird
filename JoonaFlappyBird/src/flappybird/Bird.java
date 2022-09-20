package flappybird;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/*
	 * Program name: Bird Class 
	 * Programmer: Joona Huomo 
	 * Date: January 31st 2021
	 * Description: Bird class that contains all the information about the bird,
	 * including collision, speed, spawn position, lives, and movement
	 */

public class Bird {

	// declaring movement and positioning variables
	double x = 100;
	double y = 300;
	int speed = 3;
	double dy = 0;
	double acceleration = 1;
	int imageCount = 0;
	int imageCount2 = 0;

	// angle variable
	int intAngle = 0;

	// amount of lives at the start
	int lives = 1;

	// The movement button
	static String up = "UP";

	// monkey image created
	Image image = new Image("images/bird1.png");

	// each images are set
	static String image1Name = "images/bird1.png";
	static Image imageFlat = new Image(image1Name);

	static String image2Name = "images/bird2up.png";
	static Image imageUp1 = new Image(image2Name);

	static String image3Name = "images/bird3up.png";
	static Image imageUp2 = new Image(image3Name);

	// creating gc and linking canvas from fxml file
	GraphicsContext gc;
	@FXML
	Canvas gameCanvas;

	// creates an arraylist of type string for input
	ArrayList<String> input;

	// method that sets the skin of the bird to the type of skin that the user
	// bought
	public void setSkin(String skinType) {

		// whatever type of skin was chosen, it gets set using set bird image method
		if (skinType.equals("Default")) {
			setBirdImage("images/bird1.png", "images/bird2up.png", "images/bird3up.png");
		} else if (skinType.equals("Green")) {
			setBirdImage("images/greenbird1.png", "images/greenbird2up.png", "images/greenbird3up.png");
		} else if (skinType.equals("Blue")) {
			setBirdImage("images/bluebird1.png", "images/bluebird2up.png", "images/bluebird3up.png");
		} else if (skinType.equals("Purple")) {
			setBirdImage("images/purplebird1.png", "images/purplebird2up.png", "images/purplebird3up.png");
		} else if (skinType.equals("Invisible")) {
			setBirdImage("images/invisiblebird1.png", "images/invisiblebird2up.png", "images/invisiblebird3up.png");
		} else if (skinType.equals("Zombie")) {
			setBirdImage("images/zombiebird1.png", "images/zombiebird2up.png", "images/zombiebird3up.png");
		}
	}

	// method that creates a bird object
	public Bird(GraphicsContext gc, Canvas gameCanvas, ArrayList<String> input) {
		this.gc = gc;
		this.gameCanvas = gameCanvas;
		this.input = input;
	}

	// method that gets the hitbox of the bird, depending on it's rotation
	public Rectangle2D getBoundary() {
		if (acceleration < 4) {
			return new Rectangle2D(this.x, this.y, 80, 64);
		} else
			return new Rectangle2D(this.x, this.y, 64, 64);
	}

	// method that determines collision between top pipe and bird
	public boolean collisionPipesTop(Pipes pipes) {
		boolean collide = this.getBoundary().intersects(pipes.getBoundaryTop());
		return collide;
	}

	// method that determines collision between bottom pipe and bird
	public boolean collisionPipesBot(Pipes pipes) {
		boolean collide = this.getBoundary().intersects(pipes.getBoundaryBot());
		return collide;
	}

	// method that determines collision between floor and bird
	public boolean collisionFloor(Floor floor) {
		boolean collide = this.getBoundary().intersects(floor.getBoundary());

		if (this.y >= 700) {
			collide = true;
		}
		return collide;
	}

	// method that sets the images of the bird
	public static void setBirdImage(String image1Name, String image2Name, String image3Name) {
		imageFlat = new Image(image1Name);
		imageUp1 = new Image(image2Name);
		imageUp2 = new Image(image3Name);
	}

	// method that moves the bird
	public void move() {

		// if the button set to up is pressed, acceleration is set to -12
		if (this.input.contains(up)) {
			acceleration = -12;
		} else {
			this.dy = 0;
		}

		// save old position
		double y = this.y;

		// move the bird
		this.y += acceleration;

		// if it hits the top or bottom of the screen, it cannot move any further in
		// that direction
		if (this.y < 0 || this.y > gameCanvas.getHeight() - 15 - this.image.getHeight()) {
			this.y = y;
		}

		// determines which image of the bird to use based on the acceleration
		determineImage(acceleration);

		// draws the bird at the moved position
		this.gc.drawImage(this.image, this.x, this.y);

	}

	// method that puts gravity on the bird
	public void gravity() {

		// save old position
		double y = this.y;

		// acceleration increases by 1
		acceleration += 1;

		// acceleration gets added to this y
		this.y += acceleration;

		// if it hits the top or bottom of the screen, it cannot move any further in
		// that direction
		if (this.y < 0 || this.y > gameCanvas.getHeight() - 15 - this.image.getHeight()) {
			this.y = y;
		}

		// determines which image of the bird to use based on the acceleration
		determineImage(acceleration);

		// draws the bird at the moved position
		this.gc.drawImage(this.image, this.x, this.y);
	}

	// method that determines which rotation of the image to use based on
	// acceleration
	public void determineImage(double acceleration) {

		// if acceleration is greater than 5 it will use the first image of the bird,
		// and constantly rotate it until it hits 90 degrees
		if (acceleration > 5) {

			// sets image to the flat image
			this.image = imageFlat;
			ImageView iv = new ImageView(this.image);

			// rotates image by int angle
			iv.setRotate(intAngle);

			// if int angle is less than 90, 5 gets added to it
			if (intAngle < 90) {
				intAngle += 5;
			}

			// takes a snapshot of that rotated image and sets that to this image
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			this.image = iv.snapshot(params, null);
		}

		// if acceleration is less than -1 it will use the upwards tilted images with
		// the wings flapping
		else if (acceleration < -1) {

			// int angle set to 0
			intAngle = 0;

			// if the image count is between 0 and 2, it sets the image to the first
			// flapping image and increases image count by 1
			if (imageCount >= 0 && imageCount <= 2) {
				this.image = imageUp1;

				imageCount++;
			}

			// if image count is greater than or equal to 2, it sets the image to the second
			// flapping image and increases image count2 by 1, then once it has run twice,
			// it will reset image count and image count 2 to 0
			else if (imageCount >= 2) {
				this.image = imageUp2;
				imageCount2++;
				if (imageCount2 >= 2) {
					imageCount = 0;
					imageCount2 = 0;
				}
			}

		}
	}

}
