package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.Robot.RobotType;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.Encoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives forward desired distance at desired speed
 */
public class DriveDistance extends Command {

	private static double CORRECTION;
	
	private double initialYaw, currentYaw, startLeft, startRight, distance, speeed, timeout, startTime, rampDown;
	private boolean usingPigeon;
	private boolean hasRun;

	// Negative distances work and drive forward
	public DriveDistance(double speeed, double distance, double timeout) {
		requires(Robot.driveTrain);
		CORRECTION = 40 * speeed;
		this.speeed = speeed;
		this.timeout = timeout;
		rampDown = 1;
		if (Robot.getRobotType() == RobotType.Competition) {
			this.distance = -1 * distance;
		} else if (Robot.getRobotType() == RobotType.Practice) {
			this.distance = distance;
		} else if (Robot.getRobotType() == RobotType.Programming) {
			this.distance = distance; //TODO: check val
		} else {
			this.distance = -1 * distance;
		}
	}

	/**
	 * Set the starting position by averaging encoder values
	 */
	protected void initialize() {
		hasRun = false;
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
		if(hasRun) {
			Robot.logger.log("Has run thing running (DriveDistance starting again");
			startLeft = Robot.driveTrain.getEncoderPosition(Encoder.LEFT);
			startRight = Robot.driveTrain.getEncoderPosition(Encoder.RIGHT);
			initialYaw = Robot.driveTrain.getPigeonYaw();
			startTime = Timer.getFPGATimestamp();
			usingPigeon = !Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.PIGEON);
			Robot.logger.log("Drive Distance", "Is " + (usingPigeon ? "" : "not") + " using pigeon for " + distance + "inch move");
			hasRun = false;
		}
		
		double distanceLeft = distance - maxDist();
		if (distanceLeft < 20) {
			rampDown = distanceLeft / 20;
			if (rampDown < 0.35) {
				rampDown = 0.35;
			}
		}
		
		
		if(usingPigeon) {
			currentYaw = Robot.driveTrain.getPigeonYaw();
			//This is a bit backwards
			double differenceYaw = initialYaw - currentYaw;
			if (differenceYaw != 0) {
				//Hence why this is backwards
				Robot.driveTrain.tankDrive((speeed + (differenceYaw / CORRECTION)) * rampDown, (speeed - (differenceYaw / CORRECTION)) * rampDown);
			}
		} else {
			Robot.driveTrain.tankDrive(speeed * rampDown, speeed * rampDown);
		}
	}

	/**
	 * Terminate command if desired distance has been traveled
	 */
	protected boolean isFinished() {
		if(Timer.getFPGATimestamp() > startTime + timeout) {
			Robot.logger.log("Ending by time");
			return true;			
		}
		if(!Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_L) || !Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_R)) {
			if (distance >= 0) {
				if (maxDist() >= distance) {
					Robot.logger.log("Ending by Distance");
					System.out.println("Ending DriveDistance at " + maxDist() + " inches");
					return true;	
				} else {
					return false;
				}
			} else {
				return maxDist() <= distance;
			}
		} else {
			return false;
		}
	}

	private double averageDist() {
		if(Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_R))
			return Robot.driveTrain.getEncoderPosition(Encoder.LEFT) - startLeft;
		else if(Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.ENCODER_L))
			return Robot.driveTrain.getEncoderPosition(Encoder.RIGHT) - startRight;
		else
			return (Robot.driveTrain.getEncoderPosition(Encoder.LEFT) - startLeft + Robot.driveTrain.getEncoderPosition(Encoder.RIGHT) - startRight) / 2f;
	}
	
	private double maxDist() {
		if (Math.abs(Robot.driveTrain.getEncoderPosition(Encoder.LEFT) - startLeft) > Math.abs(Robot.driveTrain.getEncoderPosition(Encoder.RIGHT) - startRight)) {
			return Robot.driveTrain.getEncoderPosition(Encoder.LEFT) - startLeft;			
		} else {
			return Robot.driveTrain.getEncoderPosition(Encoder.RIGHT) - startRight;	
		}
	}

	/**
	 * Remember to shut off motors when done
	 */
	protected void end() {
		Robot.logger.log("Drive Distance", "Ended");
		Robot.driveTrain.tankDrive(0, 0);
		hasRun = true;
	}

	protected void interrupted() {
		Robot.logger.log("Drive Distance", "Interrupted");
		Robot.driveTrain.tankDrive(0, 0);
		hasRun = true;
	}
}
