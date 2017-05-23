package shoot_the_duck;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ronald on 10/12/16.
 */
public class Duck {

    /**
     * How must time must pass in order to create a new duck?
     */
    public static long timeBetweenDucks = Framework.secInNanosec /2;


    /**
     * Last time when the duck was created.
     */
    public static long lastDuckTime = 0;

    /**
     * Duck lines
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    public static int[] [] ducklines = {
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.60), -2, 20},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.65), -3, 30},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.70), -4, 40},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.78), -5, 50},
    };

    /**
     * Indicate which is next duck line.
     */
    public static int nextDuckLines = 0;

    /**
     * X coordinate of the duck.
     */
    public int x;

    /**
     * Y coodinate of the duck.
     */
    public int y;

    /**
     * How fast the duck should move? And in which direction?
     */
    private int speed;

    /**
     * How many points this duck is worth.
     */
    public int score;

    /**
     * Duck image.
     */
    private BufferedImage duckImg;

    /**
     * Create a new duck
     *  X- starting x coordinate.
     *  Y- starting y coordinate
     *  The speed of the duck.
     *  Score: how many points the duck is worth.
     *  Image of the duck.
     */

    public Duck(int x, int y, int speed, int score, BufferedImage duckImg){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
        this.duckImg = duckImg;
    }
    /**
     * Move the duck.
     */
    public void update(){
        x += speed;
    }
    /**
     * Draw the duck to the screen.
     * g2d Graphics2D
     */
    public void draw (Graphics2D g2d){
        g2d.drawImage(duckImg, x, y, null);
    }

}
