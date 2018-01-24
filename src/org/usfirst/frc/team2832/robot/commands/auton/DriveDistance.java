package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.ENCODER;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives forward desired distance at desired speed
 */
public class DriveDistance extends Command {

	private double startLeft, startRight, distance, speeed;

	// Negative distances work
	public DriveDistance(double speeed, double distance) {
		requires(Robot.driveTrain);
		this.speeed = speeed;
		this.distance = distance;
	}

	/**
	 * Set the starting position by averaging encoder values
	 */
	protected void initialize() {
		startLeft = Robot.driveTrain.getEncoderPosition(ENCODER.LEFT);
		startRight = Robot.driveTrain.getEncoderPosition(ENCODER.RIGHT);
	}

	/**
	 * Drive strait at desired speed
	 */
	protected void execute() {
		Robot.driveTrain.arcadeDrive(speeed * Math.signum(distance), 0);
	}

	/**
	 * Terminate command if desired distance has been traveled
	 */
	protected boolean isFinished() {
		if (distance >= 0)
			return averageDist() > distance;
		else
			return averageDist() < distance;
	}

	private double averageDist() {
		return (Robot.driveTrain.getEncoderPosition(ENCODER.LEFT) - startLeft
				+ Robot.driveTrain.getEncoderPosition(ENCODER.RIGHT) - startRight) / 2f;
	}

	/**
	 * Remember to shut off motors when done
	 */
	protected void end() {
		Robot.driveTrain.arcadeDrive(0, 0);
	}

	protected void interrupted() {
		Robot.driveTrain.arcadeDrive(0, 0);
	}
}
