package shoot_the_duck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Created by ronald on 08/12/16.
 */

/**
 * Creates a JPanel on which we draw and listen for keyword and mouse events.
 */

public abstract class Canvas extends JPanel implements KeyListener, MouseListener {

    // keyboard states -- here we store states for keyboard keys  --> is it down or not?
    private static boolean[] keyboardState = new boolean[525];

    // mouse states -- here we store states for mouse keys --> is it down or not?
    private static boolean[] mouseState = new boolean[3];


    // Create a constructor of type Canvas:
    public Canvas() {
        /**
         * We use double buffer to draw on the screen.
         */
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);

        // If you will draw your own mouse cursor or if you just want that mouse cursor to disappear +
        // Insert true into if condition and mouse cursor wil be REMOVED!!

        if (true) {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }

        //Adds the keyboard listener to JPanel to receive key events from this component.
        this.addKeyListener(this);

        //Adds the mouse listener to JPanel to receive mouse events from this component.
        this.addMouseListener(this);
    }

    /**
     * This method is overwritten in Framework.java and is used for drawing to the screen.
     */
    public abstract void Draw(Graphics2D g2d);

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        Draw(g2d);
    }

    //keyboard
    /**
     * Is keyboard "key" down?
     * key number of key for which you want to check the state.
     * true if the key is down and false if the key is up.
     */

    public static boolean keyBoardKeyState(int key) {
        return keyboardState[key];
    }

    // methods of the keyboard listener.
    @Override
    public void keyPressed(KeyEvent e) {
        keyboardState[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedFramework(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public abstract void keyReleasedFramework(KeyEvent e);

    //Mouse
    /**
     * Is mouse button "button" down?
     * Parameter "button" can be " MouseEvent.BUTTON1" - Indicates mouse button #1
     * or "MouseEvent.BUTTON@ - Indicates mouse button #2 ...
     * Button number of mouse button for which you want to check the state.
     * "true" if the button is down, "false" if the button is not down.
     */
    public static boolean mouseButtonState (int button){
        return mouseState[button - 1];
    }

    // Sets mouse key status.

    private void mouseKeyStatus (MouseEvent e, boolean status){
        if(e.getButton() == MouseEvent.BUTTON1)
            mouseState[0] = status;
        else if (e.getButton() == MouseEvent.BUTTON2)
            mouseState[1] = status;
        else if (e.getButton() == MouseEvent.BUTTON3)
            mouseState[2] = status;
    }
    //Methods of the mouse listener.

    @Override
    public void mousePressed(MouseEvent e) {
        mouseKeyStatus(e, true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseKeyStatus(e, false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}