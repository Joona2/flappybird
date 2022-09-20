package flappybird;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/*
 * Program name: Score Class
 * Programmer: Joona Huomo
 * Date: January 31st 2021
 * Description: Score class that gets the current score of the player, and coins and displays them on the canvas
 */

public class Score {

	//gets canvas from fxml file, creates gc
	@FXML
	Canvas gameCanvas;
	GraphicsContext gc;
	
	// method that creates a score object, and sets it to the canvas
	public Score(Canvas gameCanvas, GraphicsContext gc) {
		this.gameCanvas = gameCanvas;
		this.gc = gc;
	}

	
	// method that displays the points on the screen
	public void display(int points) {
		
		
		// makes a score string that holds the score, and sets it's position on the
		// canvas as well as its font and colour
		String scoreString = Integer.toString(points);
		
		gc.setFont(Font.font("System", FontWeight.BOLD, 36));
		gc.setFill(Color.ORANGE);
		gc.fillText(scoreString, 20, 50);
	}
	
	// method that displays the coins on the scoreboard
	public void displayCoins() throws IOException{
		
		//reads the number of coins from the coins file
		BufferedReader reader = new BufferedReader(new FileReader("src/flappybird/coins.txt"));
		String insideTxt = reader.readLine();
		reader.close();
		
		//creates int prevcoins and sets it to 0
		int prevCoins = 0;
		
		//attempts to parse the information from the coins file into the prevcoins variable
		try {
			prevCoins = Integer.parseInt(insideTxt);
		}catch (Exception e) {}
		
		
		// makes a coins string that holds the coins, and sets it's position on the
		// canvas as well as its font and colour
		String coinsString = "$" + Integer.toString(prevCoins);
		
		gc.setFont(Font.font("System", FontWeight.BOLD, 36));
		gc.setFill(Color.ORANGE);
		gc.fillText(coinsString, 650, 50);
	}

}

