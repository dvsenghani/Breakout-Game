import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram  {
	//width and height of application in pixel
	public static final int APPLICATION_WIDTH = 600;
	public static final int APPLICATION_HEIGHT = 600;
	
	//dimension of the board game
	private static final int WIDTH = APPLICATION_WIDTH;
	
	//dimension of the paddle
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	
	//offset of the paddle up from the bottom
	private static final int PADDLE_Y_OFFSET = 30;
	
	///number of bricks per row
	private static final int NBRICKS_PER_ROW = 10;
	
	//number of rows per brick
	private static final int NBRICKS_ROWS = 10;
	
	//separation of bricks
	private static final int BRICK_SEP = 4;
	
	//bricks width
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW -1)* BRICK_SEP)/ NBRICKS_PER_ROW ;
	
	//BRICKS HEIGHT
	private static final int BRICK_HEIGHT = 16;
	
	//ball's radius in pixel
	private static final int BALL_RADIUS = 15;
	
	//offset of the top brick row from the top
	private static final int BRICK_Y_OFFSET = 70;
	
	//number of times to end game
	private static final int NTURNS = 3;
	
	//ball velocity
	private double vx, vy ;
	
	//random num generator for vx
	private RandomGenerator rGen = RandomGenerator.getInstance();
	
	//animation delay or pause time between ball movement
	private static final int DELAY = 10;
	
	//adding brick object
	private GRect brick;
	
	//adding paddle object
	private GRect paddle;
	
	//adding ball object
	private GOval ball;
	
	//counter for number of bricks
	private int brickCounter = 100;
	
	
	//method to draw paddle
	private void drawPaddle () {
		double x = getWidth () / 2 - PADDLE_WIDTH / 2;
		
		//paddle height stays the same throughout the game
		double y = getHeight () - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect (x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
		
	}
	
	//method to draw the ball
	private void drawBall() {
		double x = getWidth () / 2 - BALL_RADIUS;
		double y = getHeight() / 2 - BALL_RADIUS;
		ball = new GOval (x, y, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
				
		
	}

	//method to draw bricks
	private void drawBricks (double cx, double cy) {
		for (int row =0; row < NBRICKS_ROWS; row++) {
			for (int col=0; col < NBRICKS_PER_ROW; col++) {
				double x = cx - (NBRICKS_PER_ROW * BRICK_WIDTH) / 2 - ((NBRICKS_PER_ROW - 1)* BRICK_SEP) / 2 + col * BRICK_WIDTH +col * BRICK_SEP;
				
				double y = cy +row * BRICK_HEIGHT + row * BRICK_SEP;
				
				brick = new GRect (x, y, BRICK_WIDTH, BRICK_HEIGHT);
				add(brick);
				brick.setFilled(true);
				
				//brick's color depending on the row
				if ( row < 2)
					brick.setColor(Color.RED);
				if (row > 1 && row < 4)
					brick.setColor(Color.ORANGE);
				if (row > 3 && row < 6)
					brick.setColor(Color.YELLOW);
				if (row > 5 && row < 8 )
					brick.setColor(Color.GREEN);
				if (row > 7 && row < 10)
					brick.setColor(Color.CYAN);
			}
		}
	}
	
	private void getBallVelocity () {
		vy = 4.0;
		vx = rGen.nextDouble(1.0, 3.0);
		if (rGen.nextBoolean(0.5)) {
			vx = -vx;
		}
	}
	
	//method to move ball
	private void moveBall () {
		ball.move(vx, vy);
		if ((ball.getX()- vx <= 0 && vx < 0 ) || (ball.getX() + vx >= (getWidth() - BALL_RADIUS * 2) && vx > 0)) {
			vx = -vx;
		}
		if (ball.getY() - vy <= 0 && vy < 0) {
			vy = -vy;
			
		}
		
		//check for other objects
		GObject collider = getCollidingObject();
		if (collider == paddle) {
			if (ball.getY() >= getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2 && ball.getY() < getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2+4) {
				vy = -vy;
			}
		}
		else if (collider != null) {
			remove (collider);
			brickCounter--;
			vy = -vy;
			
		}
		pause(DELAY);
		
	}
	
	private GObject getCollidingObject() {
		if ((getElementAt(ball.getX(), ball.getY())) !=null) {
			return getElementAt(ball.getX(), ball.getY());
			
		}
		else if (getElementAt ((ball.getX() + BALL_RADIUS * 2), ball.getY()) != null) {
			return getElementAt ((ball.getX() + BALL_RADIUS * 2), ball.getY());
		}
		else if (getElementAt (ball.getX(), (ball.getY() + BALL_RADIUS * 2)) != null) {
			return getElementAt (ball.getX(), (ball.getY() + BALL_RADIUS * 2));
		}
		
		else if (getElementAt((ball.getX() + BALL_RADIUS * 2), (ball.getY() + BALL_RADIUS *2)) != null) {
			return getElementAt((ball.getX() + BALL_RADIUS * 2), (ball.getY() + BALL_RADIUS *2 )) ;
			
		}
		else {
			return null;
		}
	}
	
	//method to display winner
	private void printWinner() {
		GLabel winner = new GLabel ("Winner!!", getWidth() / 2, getHeight() / 2);
		winner.move(-winner.getWidth()/ 2, -winner.getHeight());
		winner.setColor(Color.RED);
		add(winner);
	}
	
	//method to display game over
	private void printGameOver() {
		GLabel gameOver = new GLabel ("Game Over!!", getWidth() / 2, getHeight() / 2);
		gameOver.setColor(Color.RED);
		add(gameOver);
		
	}
	public void mouseMoved (MouseEvent e) {
		if ((e.getX() < getWidth()  - PADDLE_WIDTH / 2 ) && (e.getX() > PADDLE_WIDTH / 2)) {
			paddle.setLocation(e.getX() - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
		}
	}
	
	//method to start game
	private void playGame () {
		waitForClick();
		getBallVelocity();
		while(true) {
			moveBall();
			if(ball.getY() >= getHeight()) {
				break;
			}
			if (brickCounter ==0) {
				break;
			}
		}
	}
	
	//setup the game
	private void setupGame() {
		drawBricks (getWidth() / 2, BRICK_Y_OFFSET);
		drawPaddle();
		drawBall();
		
	}
	
	//method to initialize game
	public void init() {
		setSize (APPLICATION_WIDTH, APPLICATION_HEIGHT);
		
	}
	
	//method to run game
	public void run() {
		for (int i =0; i < NTURNS; i++) {
			setupGame();
			playGame();
			
			if(brickCounter ==0) {
				ball.setVisible(false);
				printWinner();
				break;
			}
			if (brickCounter > 0) {
				removeAll();
				
			}
			
		}
		if(brickCounter > 0) {
			printGameOver();
		}
	}
}
