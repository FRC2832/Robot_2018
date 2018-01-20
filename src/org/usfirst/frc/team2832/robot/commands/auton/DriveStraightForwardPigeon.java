package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.ENCODER;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightForwardPigeon extends Command {
	
	private DriveTrain driveTrain = Robot.driveTrain;
	private double initialYaw, currentYaw, speed, start, distance;
	private double correction = 20;
	
	public DriveStraightForwardPigeon(double speed, double distance) {
		requires(Robot.driveTrain);
		this.speed = speed;
		this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	initialYaw = driveTrain.getPigeonYaw();
    	start = Robot.driveTrain.getEncoderPosition(ENCODER.BOTH);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	currentYaw = driveTrain.getPigeonYaw();
    	double differenceYaw = initialYaw - currentYaw;
    	if (differenceYaw > 0) {
    		driveTrain.tankDrive(speed - (differenceYaw / correction), speed + (differenceYaw / correction));
    	} else {
    		driveTrain.tankDrive(speed + (differenceYaw / correction), speed - (differenceYaw / correction));
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.getEncoderPosition(ENCODER.BOTH) > start + distance;
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
