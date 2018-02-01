package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.Encoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives forward desired distance at desired speed
 */
public class DriveDistance extends Command {

	public static double CORRECTION = 20;
	
	private DriveTrain driveTrain = Robot.driveTrain;
	private double initialYaw, currentYaw, startLeft, startRight, distance, speeed, timeout, startTime;

	// Negative distances work
	public DriveDistance(double speeed, double distance, double timeout) {
		requires(Robot.driveTrain);
		this.speeed = speeed;
		this.distance = distance;
		this.timeout = timeout;
	}

	/**
	 * Set the starting position by averaging encoder values
	 */
	protected void initialize() {
		startLeft = Robot.driveTrain.getEncoderPosition(Encoder.LEFT);
		startRight = Robot.driveTrain.getEncoderPosition(Encoder.RIGHT);
		initialYaw = driveTrain.getPigeonYaw();
		startTime = Timer.getFPGATimestamp();
	}

	/**
	 * Drive strait at desired speed
	 */
	protected void execute() {
		currentYaw = driveTrain.getPigeonYaw();
    	double differenceYaw = initialYaw - currentYaw;
    	if (differenceYaw > 0) {
    		driveTrain.tankDrive(speeed - (differenceYaw / CORRECTION), speeed + (differenceYaw / CORRECTION));
    	} else {
    		driveTrain.tankDrive(speeed + (differenceYaw / CORRECTION), speeed - (differenceYaw / CORRECTION));
    	}
	}

	/**
	 * Terminate command if desired distance has been traveled
	 */
	protected boolean isFinished() {
		if(Timer.getFPGATimestamp() > startTime + timeout)
			return true;
		if (distance >= 0)
			return averageDist() > distance;
		else
			return averageDist() < distance;
	}

	private double averageDist() {
		return (Robot.driveTrain.getEncoderPosition(Encoder.LEFT) - startLeft
				+ Robot.driveTrain.getEncoderPosition(Encoder.RIGHT) - startRight) / 2f;
	}

	/**
	 * Remember to shut off motors when done
	 */
	protected void end() {
    	driveTrain.tankDrive(0, 0);
	}

	protected void interrupted() {
    	driveTrain.tankDrive(0, 0);
	}
}
