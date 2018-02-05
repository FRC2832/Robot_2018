package org.usfirst.frc.team2832.robot;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DrivingTest implements KeyEventDispatcher {

    private Robot robot;
    private int speed, direction;

    public void init() {
        robot = new Robot();
    }

    public void periodic() {
        robot.arcadeDrive(speed, direction);
    }

    private class Robot {

        public void getVelocity() {

        }

        public void arcadeDrive(double speed, double rotation) {

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent ke) {
        synchronized (DrivingTest.class) {
            switch (ke.getID()) {
                case KeyEvent.KEY_PRESSED:
                    if (ke.getKeyCode() == KeyEvent.VK_W)
                        speed++;
                    if (ke.getKeyCode() == KeyEvent.VK_S)
                        speed--;
                    if (ke.getKeyCode() == KeyEvent.VK_A)
                        direction--;
                    if (ke.getKeyCode() == KeyEvent.VK_D)
                        direction++;
                    break;

                case KeyEvent.KEY_RELEASED:
                    if (ke.getKeyCode() == KeyEvent.VK_W)
                        speed--;
                    if (ke.getKeyCode() == KeyEvent.VK_S)
                        speed++;
                    if (ke.getKeyCode() == KeyEvent.VK_A)
                        direction++;
                    if (ke.getKeyCode() == KeyEvent.VK_D)
                        direction--;
                    break;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        DrivingTest test = new DrivingTest();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(test);
        test.init();
        while(true) {
            test.periodic();
        }
    }
}
