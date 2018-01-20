package org.usfirst.frc.team2832.robot.commands.auton;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

/**
 *
 */
public class TurnDegrees extends Command {

	private static double initialYaw;
	private static double currentYaw;
	private DriveTrain driveTrain = Robot.driveTrain;
	private double degrees;
	private boolean turnRight;

	//set turnRight to true to turn right, set to false to turn left
	public TurnDegrees(double degrees, boolean turnRight) {
		requires(Robot.driveTrain);
		this.degrees = degrees;
		this.turnRight = turnRight;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		initialYaw = Robot.driveTrain.getPigeonYaw();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		currentYaw = Robot.driveTrain.getPigeonYaw();
		
		double YawDifference = Math.abs(initialYaw - currentYaw);
		
		if (turnRight) {
			if (YawDifference < degrees - 20) {
				driveTrain.tankDrive(.5, -.5);
			} else if (YawDifference < degrees) {
				driveTrain.tankDrive(.2, -.2);
			} else if (YawDifference > degrees) {
				driveTrain.tankDrive(-.2, .2);
			}
		} else {
			if (YawDifference < degrees - 20) {
				driveTrain.tankDrive(-.5, .5);
			} else if (YawDifference < degrees) {
				driveTrain.tankDrive(-.2, .2);
			} else if (YawDifference > degrees) {
				driveTrain.tankDrive(.2, -.2);
			}
		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if ((Math.abs(initialYaw - currentYaw) > degrees - 2) && (Math.abs(initialYaw - currentYaw) < degrees + 2)) {
			return true;
		} else {
			return false;
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		driveTrain.tankDrive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		driveTrain.tankDrive(0, 0);
	}
}
