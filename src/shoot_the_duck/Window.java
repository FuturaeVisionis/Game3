package shoot_the_duck;

import javax.swing.*;

/**
 * Created by ronald on 11/12/16.
 */
public class Window extends JFrame {

    private Window() {

        /**
         * Sets the title for this frame.
         */
        this.setTitle("Shoot the Duck");

        /**
         * Sets the size of the frame.
         */
        if (true) { // full screen mode
            //Disables decorations for this frame.
            this.setUndecorated(true);

            //Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);

        } else { // Window mode
            //Size of the frame
            this.setSize(800, 600);
            //Puts frame to center of screen.
            this.setLocationRelativeTo(null);
            //So that frame cannot be resizable by user.
            this.setResizable(false);
        }
/**
 * Exit the application when user closes frame.
 */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new Framework());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
