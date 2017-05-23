package shoot_the_duck;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ronald on 10/12/16.
 */
public class Framework extends Canvas {

    /**
     * Width of the frame.
     */
    public static int frameWidth;

    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds.
     */
    public static final long secInNanosec = 1000000000L;

    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds.
     */
    public static final long millisecInNanosec = 1000000L;

    /**
     * FPS-Frames per second.
     * How many times per second should the game update?
     */
    private final int GAME_FPS = 60;

    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    /**
     * Possible states of the game.
     */
    public enum GameState {
        STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAME_OVER, DESTROYED
    }

    /**
     * Current state of the game.
     */
    public static GameState gameState;

    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;

    /**
     * Is used for calculating elapsed time.
     */
    private long lastTime;

    // THE ACTUAL GAME:

    private Game game;

    private BufferedImage shootTheDuckMenuImg;

    public Framework() {
        super();
        gameState = GameState.VISUALIZING;
        // We start game in a new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop();
            }
        };
        gameThread.start();
    }

    /**
     * Set variables and object.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game
     * can be set in Game.jav.
     */
    private void Initialize() {

    }

    /**
     * Load files - images, sounds...
     * This method is intended to load files for this class.
     * Files for the actual game can be loaded in Game.java
     */
    private void LoadContent() {
        try {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/data/menu.jpg");
            shootTheDuckMenuImg = ImageIO.read(shootTheDuckMenuImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * In specific intervals of the time (GAME_UPDATE_PERIOD) the game/logic is updated and then the is drawn on the screen.
     */

    private void GameLoop() {
        /**
         * These two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct
         * frame resolution.
         */
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        /**
         * These variables are used for calculating the time that defines for how long we should put thread to sleep to
         * meet the GAME_FPS.
         */
        long beginTime, timeTaken, timeLeft;

        while (true) {
            beginTime = System.nanoTime();
            switch (gameState) {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    game.UpdateGame(gameTime, getMousePosition());
                    lastTime = System.nanoTime();
                    break;
                case GAME_OVER:
                    break;
                case MAIN_MENU:
                    break;
                case OPTIONS:
                    break;
                case GAME_CONTENT_LOADING:
                    break;
                case STARTING:
                    /**
                     * Sets variables and objects.
                     */
                    Initialize();
                    /**
                     * Load files - images, sounds, ...
                     */
                    LoadContent();

                    /**
                     * When all things that are called above finish, we change game status to main menu.
                     */
                    gameState = GameState.MAIN_MENU;
                    break;

                case VISUALIZING:
                    if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        /**
                         * When we get size of frame we change status.
                         */
                        gameState = GameState.STARTING;
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

                /**
                 * Repaint the screen.
                 */
                repaint();

                /**
                 * Here we calculate the time that defines for how long we should put thread to sleep to meet the GAME_FPS.
                 */
                timeTaken = System.nanoTime() - beginTime;
                timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / millisecInNanosec;

                /**
                 * If the time is less than 10 milliseconds so that some other thread can do some work.
                 */
                if (timeLeft < 10)
                    timeLeft = 10; // setting a minimum!!
                try {
                    /**
                     * Improves the necessary delay and also yields control so that other thread can do work.
                     */
                    Thread.sleep(timeLeft);
                } catch (InterruptedException ex) {

                }
            }
        }


        /**
         * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
         */

    public void Draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING:
                game.Draw(g2d, getMousePosition());
                break;
            case GAME_OVER:
                game.DrawGameOver(g2d, getMousePosition());
                break;
            case MAIN_MENU:
                g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawString("Use left mouse button to shoot the Duck.", frameWidth / 2 - 83, (int) (frameHeight * 0.65));
                g2d.drawString("Click with left mouse button to start the game.", frameWidth / 2 - 100, (int) (frameHeight * 0.67));
                g2d.drawString("Press ESC any time to exit the game.", frameWidth / 2 - 75, (int) (frameHeight * 0.70));
                g2d.setColor(Color.white);
                g2d.drawString("www.YellowBall.nl", 7, frameHeight - 5);
                break;
            case OPTIONS:
                break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("Game is LOADING", frameWidth / 2 - 50, frameHeight / 2);
                break;
        }
    }

    /**
     * Starts new game
     */
    private void newGame() {
        /**
         * We set the gameTime to zero and lastTime to current time for later calculations.
         */
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new Game();
    }

    /**
     * Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame() {
        /**
         * We set gameTime to zero and lastTime to current time for later calculations.
         */
        gameTime = 0;
        lastTime = System.nanoTime();
        game.RestartGame();

        /**
         * We change game status so that the game can start.
         */
        gameState = GameState.PLAYING;
    }

    /**
     * Returns the position of the mouse pointer in the game frame/window.
     * If mouse position is null then this method returns 0,0 coordinate.
     * return Point of mouse coordination.
     */

    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();
            if (mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);

        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    /**
     * This method is called when keyboard key is released.
     * e KeyEvent
     */


    public void keyReleasedFramework(KeyEvent e) {
        switch (gameState) {
            case GAME_OVER:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
                break;

            case PLAYING:
            case MAIN_MENU:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                break;
        }
    }

    /**
     * This method is called when mouse button is clicked.
     * e MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        switch (gameState) {
            case MAIN_MENU:
                if (e.getButton() == MouseEvent.BUTTON1)
                    newGame();
        }
    }
}

