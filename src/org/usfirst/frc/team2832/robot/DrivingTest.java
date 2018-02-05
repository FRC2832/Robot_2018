package org.usfirst.frc.team2832.robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class DrivingTest implements KeyEventDispatcher {

    public static Robot robot;
    private int speed, direction;

    public void init() {
        robot = new Robot();
    }

    public void periodic() {
        robot.arcadeDrive(speed, direction);
        robot.update();
    }

    private class Robot {

        private double xVelocity, yVelocity, angle, x, y;

        public double getVelocity() {
            return (xVelocity + yVelocity) / 2;
        }

        public void update() {
            if(Math.abs(xVelocity) < 0.1)
                xVelocity = 0;
            if(Math.abs(yVelocity) < 0.1)
                xVelocity = 0;
            if(xVelocity != 0)
                xVelocity -= Math.signum(xVelocity) * 1;
            if(yVelocity != 0)
                yVelocity -= Math.signum(yVelocity) * 1;
            x += xVelocity;
            y += yVelocity;
        }

        public void arcadeDrive(double speed, double rotation) {
            angle += rotation / 100d;
            xVelocity += speed * Math.cos(angle);
            yVelocity += speed * Math.sin(angle);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent ke) {
        synchronized (DrivingTest.class) {
            switch (ke.getID()) {
                case KeyEvent.KEY_PRESSED:
                    if (ke.getKeyCode() == KeyEvent.VK_ESCAPE)
                        System.exit(0);
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

        JFrame frame = new JFrame("Skidesign");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);
        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.drawRect(10, 10, frame.getWidth() - 40, frame.getHeight() - 60);
                g2d.setColor(Color.cyan);
                g2d.drawOval((int)robot.x, (int)robot.y, 30, 30);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Angle: " + Double.toString(DrivingTest.robot.angle), 30, 30);
                g2d.drawString("X Velocity: " + Double.toString(DrivingTest.robot.xVelocity), 30, 45);
                g2d.drawString("Y Velocity: " + Double.toString(DrivingTest.robot.yVelocity), 30, 60);
                g2d.drawString("X: " + Double.toString(DrivingTest.robot.x), 30, 75);
                g2d.drawString("Y: " + Double.toString(DrivingTest.robot.y), 30, 90);
                g2d.drawString("Direction: " + Double.toString(test.direction), 30, 105);
                g2d.drawString("Speed: " + Double.toString(test.speed), 30, 120);
            }
        });
        frame.setVisible(true);

        while(true) {
            test.periodic();
            frame.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {

            }
        }
    }
}
