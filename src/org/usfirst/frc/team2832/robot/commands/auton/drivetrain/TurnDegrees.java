package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

/**
 * A command to turn a set number of degrees
 */
public class TurnDegrees extends Command {

	private DriveTrain driveTrain;
	private double initialYaw, degrees, currentYaw;
	private boolean turnRight;

	// TODO: 2/9/2018 Switch this to take in one parameter which reflects direction, as well
	//set turnRight to true to turn right, set to false to turn left
	public TurnDegrees(double degrees, boolean turnRight) {
		requires(Robot.driveTrain);
		this.degrees = degrees;
		this.turnRight = turnRight;
		driveTrain = Robot.driveTrain;
	}

	/**
	 * Save the initial angle of the robot
	 */
	protected void initialize() {
		Robot.logger.log("Turn Degrees", "Turning " + degrees + " to the " + (turnRight ? "right" : "left"));
		initialYaw = Robot.driveTrain.getPigeonYaw();
	}

	/**
	 * Turn left or right based on direction and slow down as approaching angle
	 */
	protected void execute() {
		currentYaw = Robot.driveTrain.getPigeonYaw();
		double YawDifference = Math.abs(initialYaw - currentYaw);
		
		if (turnRight) {
			if (YawDifference < degrees - 15) {
				driveTrain.tankDrive(.5, -.5);
			} else if (YawDifference < degrees) {
				driveTrain.tankDrive(.2, -.2);
			} else if (YawDifference > degrees) {
				driveTrain.tankDrive(-.2, .2);
			}
		} else {
			if (YawDifference < degrees - 15) {
				driveTrain.tankDrive(-.5, .5);
			} else if (YawDifference < degrees) {
				driveTrain.tankDrive(-.2, .2);
			} else if (YawDifference > degrees) {
				driveTrain.tankDrive(.2, -.2);
			}
		}

	}

	/**
	 * Terminate if within four degrees of target angle
	 */
	protected boolean isFinished() {
		return ((Math.abs(initialYaw - currentYaw) > degrees - 2) && (Math.abs(initialYaw - currentYaw) < degrees + 2));
	}

	/**
	 * Oh, are we still turning?
	 */
	protected void end() {
		Robot.logger.log("Turn Degrees", "Ended");
		driveTrain.tankDrive(0, 0);
	}

	/**
	 * Let's not spin in circles like last year
	 */
	protected void interrupted() {
		Robot.logger.log("Turn Degrees", "Interrupted");
		driveTrain.tankDrive(0, 0);
	}
}
