package shoot_the_duck;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ronald on 10/12/16.
 */
public class Game {

    /**
     * We use this to create a random number.
     */
    private Random random;

    /**
     * Font that we will use to write statistic to screen.
     */
    private Font font;

    /**
     * Array list of the ducks.
     */
    private ArrayList<Duck> ducks;

    /**
     * How many ducks leave the screen alive?
     */
    private int runawayDucks;

    /**
     * How many ducks the player killed?
     */
    private int killedDucks;

    /**
     * For each killed duck, the player gets points.
     */
    private int score;

    /**
     * How many times a player is shot?
     */
    private int shoots;

    /**
     * Last time of the shoot.
     */
    private long lastTimeShoot;

    /**
     * The time that must elapse between shots.
     */
    private long timeBetweenShots;

    /**
     * Game background image.
     */
    private BufferedImage backgroundImg;

    /**
     * Bottom grass.
     */
    private BufferedImage grassImg;

    /**
     * Duck image.
     */
    private BufferedImage duckImg;

    /**
     * Shotgun sight image;
     */
    private BufferedImage sightImg;

    /**
     * Middle width of the sight image.
     */
    private int sightImgMiddleWidth;

    /**
     * Middle height of the sight image.
     */
    private int sightImgMiddleHeight;

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                /**
                 * Sets variables and objects for the game.
                 */
                Initialize();

                /**
                 * Load game files (images, sounds, ...
                 */
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    /**
     * Set variables objects for the game.
     */
    private void Initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);

        ducks = new ArrayList<>();
        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        shoots = 0;

        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;

    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent() {
        try {
            URL backgroundImgUrl = this.getClass().getResource("/data/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);

            URL grassImgUrl = this.getClass().getResource("/data/grass.png");
            grassImg = ImageIO.read(grassImgUrl);

            URL duckImgUrl = this.getClass().getResource("/data/duck.png");
            duckImg = ImageIO.read(duckImgUrl);

            URL sightImgUrl = this.getClass().getResource("/data/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;

        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Restart the game - reset some variables.
     */
    public void RestartGame() {
        /**
         * Removes all of the ducks from this list.
         */
        ducks.clear();

        /**
         * We set last duck time to zero.
         */

        Duck.lastDuckTime = 0;
        runawayDucks = 0;
        killedDucks = 0;
        shoots = 0;
        lastTimeShoot = 0;
    }

    /**
     * Update game logic.
     */

    public void UpdateGame(long gameTime, Point mousePosition) {
        /**
         * Creates a new duck, if it's the time, and assigns it to the array list.
         */
        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            ducks.add(new Duck(Duck.ducklines[Duck.nextDuckLines][0] + random.nextInt(200),
                    Duck.ducklines[Duck.nextDuckLines][1], Duck.ducklines[Duck.nextDuckLines][2],
                    Duck.ducklines[Duck.nextDuckLines][3], duckImg));

            /**
             * Here we increase nextDuckLines so that next duck will be created in next line.
             */
            Duck.nextDuckLines++;
            if (Duck.nextDuckLines >= Duck.ducklines.length)
                Duck.nextDuckLines = 0;

            Duck.lastDuckTime = System.nanoTime();
        }
        /**
         * Update all of the ducks.
         */
        for (int i = 0; i < ducks.size(); i++) {
            /**
             * Move the duck.
             */
            ducks.get(i).update();

            /**
             * Checks if the duck leaves teh screen and removes it if it does.
             */
            if (ducks.get(i).x < 0 - duckImg.getWidth()) {
                ducks.remove(i);
                runawayDucks++;
            }
        }
        /**
         * Does the player shoot?
         */
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            /**
             * Checks if it can shoot again.
             */
            if (System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
                shoots++;

                /**
                 * We go over all the ducks and we look if any of them was shot.
                 */
                for (int i = 0; i < ducks.size(); i++) {
                    /**
                     * We check if the mouse was over ducks head or body when player has shot.
                     */
                    if (new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27, 30).contains(mousePosition) ||
                            new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition)) {
                        killedDucks++;
                        score += ducks.get(i).score;

                        /**
                         * Remove the duck from the ArrayList.
                         */
                        ducks.remove(i);

                        // We found the duck that the player shot, so we can leave the for loop.
                        break;

                    }
                }
                lastTimeShoot = System.nanoTime();
            }
        }
        /**
         * When 200 ducks runaway, the game ends.
         */
        if (runawayDucks >= 200)
            Framework.gameState = Framework.GameState.GAME_OVER;
    }

    /**
     * Draw the game to the screen.
     * g2d Graphics2D
     * mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        /**
         * Here we draw all the ducks.
         */
        for (int i = 0; i < ducks.size(); i++) {
            ducks.get(i).draw(g2d);
        }
        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("RUNAWAY:" + runawayDucks, 10, 21);
        g2d.drawString("KILLS:" + killedDucks, 160, 21);
        g2d.drawString("SHOOTS:" + shoots, 299, 21);
        g2d.drawString("SCORE:" + score, 440, 21);

    }

    /**
     * Draw the game over the screen.
     * g2d Graphics2D
     * mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition) {
        Draw(g2d, mousePosition);
        /**
         * The first text is used for shade.
         */
        g2d.setColor(Color.black);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 39, (int) (Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int) (Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, (int) (Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int) (Framework.frameHeight * 0.70));
    }
}
