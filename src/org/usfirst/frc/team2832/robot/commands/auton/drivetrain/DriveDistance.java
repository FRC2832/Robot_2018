package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.Encoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives forward desired distance at desired speed
 */
public class DriveDistance extends Command {

	private final static double CORRECTION = 20;
	
	private double initialYaw, currentYaw, startLeft, startRight, distance, speeed, timeout, startTime;
	private boolean usingPigeon;

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
		initialYaw = Robot.driveTrain.getPigeonYaw();
		startTime = Timer.getFPGATimestamp();
		usingPigeon = !Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.PIGEON);
		Robot.logger.log("Drive Distance", "Is " + (usingPigeon ? "" : "not") + " using pigeon for " + distance + "inch move");
	}

	/**
	 * Drive strait at desired speed
	 */
	protected void execute() {
		if(usingPigeon) {
			currentYaw = Robot.driveTrain.getPigeonYaw();
			//This is a bit backwards
			double differenceYaw = initialYaw - currentYaw;
			if (differenceYaw != 0) {
				//Hence why this is backwards
				Robot.driveTrain.tankDrive(speeed + (differenceYaw / CORRECTION), speeed - (differenceYaw / CORRECTION));
			}
		} else {
			Robot.driveTrain.tankDrive(speeed, speeed);
		}
	}

	/**
	 * Terminate command if desired distance has been traveled
	 */
	protected boolean isFinished() {
		if(Timer.getFPGATimestamp() > startTime + timeout)
			return true;
		if(!Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_L) || !Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_R)) {
			if (distance >= 0)
				return averageDist() > distance;
			else
				return averageDist() < distance;
		} else {
			return false;
		}
	}

	private double averageDist() {
		if(Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_R))
			return Robot.driveTrain.getEncoderPosition(Encoder.LEFT) - startLeft;
		else if(Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_L))
			return startLeft + Robot.driveTrain.getEncoderPosition(Encoder.RIGHT) - startRight;
		else
			return (Robot.driveTrain.getEncoderPosition(Encoder.LEFT) - startLeft + Robot.driveTrain.getEncoderPosition(Encoder.RIGHT) - startRight) / 2f;
	}

	/**
	 * Remember to shut off motors when done
	 */
	protected void end() {
		Robot.logger.log("Drive Distance", "Ended");
		Robot.driveTrain.tankDrive(0, 0);
	}

	protected void interrupted() {
		Robot.logger.log("Drive Distance", "Interrupted");
		Robot.driveTrain.tankDrive(0, 0);
	}
}
