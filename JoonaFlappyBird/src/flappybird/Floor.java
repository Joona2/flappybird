package flappybird;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/*
 * Program name: Floor Class
 * Programmer: Joona Huomo 
 * Date: January 31st 2021
 * Description: Floor class that contains all the information about the floor,
 * including collision, speed, and movement
 */

public class Floor {

	// floor image created
	String image = "images/floor.png";
	Image floor = new Image(image);

	// declaring positioning variables
	double x = 0;
	double y = 720;
	int imageCount = 0;
	int imageCount2 = 0;

	// declaring gc, and linking canvas from fxml file
	GraphicsContext gc;
	@FXML
	Canvas gameCanvas;

	// method that creates a floor object
	public Floor(GraphicsContext gc, Canvas gameCanvas) {
		this.gc = gc;
		this.gameCanvas = gameCanvas;
	}

	// method that gets the hitbox of the floor
	public Rectangle2D getBoundary() {
		return new Rectangle2D(this.x, this.y, this.floor.getWidth(), this.floor.getHeight());
	}

	// method that moves the floor
	public void move() {

		// since the floor moves at the same speed as the pipes, pipes.speed is added to
		// the x value of the floor
		this.x -= Pipes.speed;

		// if the floor is 750 pixels off the screen, it gets reset
		if (this.x <= -750) {
			this.x = 0;
		}

		// draws the floor at the moved position
		this.gc.drawImage(this.floor, this.x, this.y);
	}

	// method that draws the floor without moving it
	public void draw() {
		this.gc.drawImage(this.floor, this.x, this.y);
	}
}
