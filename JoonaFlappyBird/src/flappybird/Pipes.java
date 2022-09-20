package flappybird;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/*
 * Program name: Pipes Class 
 * Programmer: Joona Huomo 
 * Date: January 31st 2021
 * Description: Pipes class that contains all the information about the pipes,
 * including collision, speed, and movement
 */

public class Pipes {

	// creates static int speed set to 6
	static int speed = 6;

	// declaring positioning variables
	double x = 750;
	double yTop = -600;
	double yBot = 600;
	double dy = 0;
	int imageCount = 0;
	int imageCount2 = 0;

	// int move pipes which will be the y movement of the pipes
	int movePipes = 0;

	// boolean switchback set to false
	boolean switchBack = false;

	// top pipe image created
	String image1Name = "images/down_pipe.png";
	Image pipeTop = new Image(image1Name);

	// bottom pipe image created
	String image2Name = "images/up_pipe.png";
	Image pipeBot = new Image(image2Name);

	// declaring gc, and linking canvas from fxml file
	GraphicsContext gc;
	@FXML
	Canvas gameCanvas;

	// method that gets the x value of the pipes
	public double getX() {
		return this.x;
	}

	// method that sets the x value of the pipes
	public void setX(double dx) {
		this.x = dx;
	}

	// method that resets the x value of the pipes
	public void resetX() {
		this.x = 888;
	}

	// method that creates a pipes object
	public Pipes(GraphicsContext gc, Canvas gameCanvas) {
		this.gc = gc;
		this.gameCanvas = gameCanvas;
	}

	// method that handles the y movement of the pipes
	public void pipesMoving(int y) {

		// if y is positive
		if (y > 0) {

			// if switchback is false
			if (!switchBack) {

				// pipes move down
				this.yTop += y;
				this.yBot += y;

				// if bottom pipe reaches 650, switchback is set to true
				if (yBot > 650) {
					switchBack = true;
				}
			}

			// if switchback is true
			else {

				// pipes move up
				this.yTop -= y;
				this.yBot -= y;

				// if top pipe reaches -700, switchback is set to true
				if (yTop < -700) {
					switchBack = false;
				}
			}
		}

		// if y is negative
		else {

			// if switchback is false
			if (!switchBack) {

				// pipes move up
				this.yTop -= y;
				this.yBot -= y;

				// if bottom pipe reaches 650, switchback is set to true
				if (yBot > 650) {
					switchBack = true;
				}
			}

			// if switchback is true
			else {

				// pipes move down
				this.yTop += y;
				this.yBot += y;

				// if top pipe reaches -700, switchback is set to true
				if (yTop < -700) {
					switchBack = false;
				}
			}
		}
	}

	// method that gets the hitbox of the top pipe
	public Rectangle2D getBoundaryTop() {
		return new Rectangle2D(this.x, this.yTop, this.pipeTop.getWidth(), this.pipeTop.getHeight());
	}

	// method that gets the hitbox of the bottom pipe
	public Rectangle2D getBoundaryBot() {
		return new Rectangle2D(this.x, this.yBot, this.pipeBot.getWidth(), this.pipeBot.getHeight());
	}

	// method that determines the spawning y positions of the pipes, as well as
	// makes sure the gap between them is not too small
	public void pipesY() {

		// generate a value which will be a reference point for the position of the top
		// and bottom parts of the pipes
		int randY = -(int) ((Math.random() * 400) + 300);

		// generate the top part of the pipe 25 pixels above the random generated value
		// above
		this.yTop = randY - 25;

		// generate the bottom pipe such that there is a random sized width between the
		// top and bottom pipe(within a maximum and minimum width between pipes)
		this.yBot = (int) ((Math.random() * (-316 - randY)) + 993 + randY) - 25;
	}

	// method that moves the pipes
	public void move() {

		// speed is subtracted from this x
		this.x -= speed;

		// if the pipes are off the screen, their x and y values are reset, and
		// this.movepipes is set to a random number from (-3)-(3)
		if (this.x <= -138) {
			resetX();
			pipesY();
			this.movePipes = (int) ((Math.random() * 6) - 3);
		}

		// sets pipes y position to the moved position based on movePipes value
		pipesMoving(this.movePipes);

		// draws the top pipe at the moved position
		this.gc.drawImage(this.pipeTop, this.x, this.yTop);

		// draws the bottom pipe at the moved position
		this.gc.drawImage(this.pipeBot, this.x, this.yBot);

	}

	// method that draws the pipes without moving them
	public void draw() {

		// draws top pipe
		this.gc.drawImage(this.pipeTop, this.x, this.yTop);

		// draws bottom pipe
		this.gc.drawImage(this.pipeBot, this.x, this.yBot);
	}
}
