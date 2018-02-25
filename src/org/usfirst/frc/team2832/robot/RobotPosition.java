package org.usfirst.frc.team2832.robot;

import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.Encoder;

/**
 * @author John
 *
 */
public class RobotPosition {
	
	private double leftX;
	private double leftY;
	private double rightX;
	private double rightY;
	
	private double lastRightEncoder;
	private double lastLeftEncoder;
	private double currentRightEncoder;
	private double currentLeftEncoder;
	private double currentYaw;
	
	public RobotPosition() {
		lastRightEncoder = Robot.driveTrain.getEncoderPosition(Encoder.RIGHT);
		lastLeftEncoder = Robot.driveTrain.getEncoderPosition(Encoder.LEFT);
		leftX = 0;
		leftY = 0;
		rightX = 0;
		rightY = 0;
	}
	
	public void startingOnLeft() {
		leftX = 32;
		leftY = 20;
		rightX = 24+32;
		rightY = 20;
	}
	public void startingInCenter() {
		leftX = 152;
		leftY = 20;
		rightX = 152+24;
		rightY = 20;
	}
	public void startingOnRight() {
		leftX = 282-24;
		leftY = 20;
		rightX = 282;
		rightY = 20;
	}
	/*
	 * Runs every 20 milliseconds, called from robot periodic()
	 */
	public void updatePosition() {
		currentRightEncoder = Robot.driveTrain.getEncoderPosition(Encoder.RIGHT);
		currentLeftEncoder = Robot.driveTrain.getEncoderPosition(Encoder.LEFT);
		currentYaw = Robot.driveTrain.getPigeonYaw();
		
		leftX = leftX + ((currentLeftEncoder - lastLeftEncoder) * Math.cos((currentYaw / 180) * Math.PI));
		rightX = rightX + ((currentRightEncoder - lastRightEncoder) * Math.cos((currentYaw / 180) * Math.PI));
		leftY = leftY + ((currentLeftEncoder - lastLeftEncoder) * Math.sin((currentYaw / 180) * Math.PI));
		rightY = rightY + ((currentRightEncoder - lastRightEncoder) * Math.sin((currentYaw / 180) * Math.PI));
				
		lastRightEncoder = Robot.driveTrain.getEncoderPosition(Encoder.RIGHT);
		lastLeftEncoder = Robot.driveTrain.getEncoderPosition(Encoder.LEFT);	
	}

	public double getLeftX() {
		return leftX;
	}
	public double getLeftY() {
		return leftY;
	}
	public double getRightX() {
		return rightX;
	}
	public double getRightY() {
		return rightY;
	}	
}
