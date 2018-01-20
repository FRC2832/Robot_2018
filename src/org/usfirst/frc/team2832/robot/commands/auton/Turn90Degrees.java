package org.usfirst.frc.team2832.robot.commands.auton;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

/**
 *
 */
public class Turn90Degrees extends Command {

	private static double initialYaw;
	private static double currentYaw;
	private DriveTrain driveTrain;
	private boolean turnRight;

	//set turnRight to true to turn right, set to false to turn left
	public Turn90Degrees(DriveTrain driveTrain, boolean turnRight) {
		requires(driveTrain);
		this.driveTrain = driveTrain;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		initialYaw = DriveTrain.getPigeonYaw();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		currentYaw = DriveTrain.getPigeonYaw();
		
		double YawDifference = Math.abs(initialYaw - currentYaw);
		
		if (turnRight) {
			if (YawDifference < 80) {
				driveTrain.tankDrive(.5, -.5);
			} else if (YawDifference < 90) {
				driveTrain.tankDrive(.3, -.3);
			} else if (YawDifference > 90) {
				driveTrain.tankDrive(-.3, .3);
			}
		} else {
			if (YawDifference < 80) {
				driveTrain.tankDrive(-.5, .5);
			} else if (YawDifference < 90) {
				driveTrain.tankDrive(-.3, .3);
			} else if (YawDifference > 90) {
				driveTrain.tankDrive(.3, -.3);
			}
		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if ((Math.abs(initialYaw - currentYaw) > 88) && (Math.abs(initialYaw - currentYaw) < 92)) {
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
