package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain.ENCODER;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive strait forward with correction from pigeon IMU
 */
public class DriveStraightForwardPigeon extends Command {
	
	public static double CORRECTION = 20;
	
	private DriveTrain driveTrain = Robot.driveTrain;
	private double initialYaw, currentYaw, speed, start, distance;
	
	public DriveStraightForwardPigeon(double speed, double distance) {
		requires(Robot.driveTrain);
		this.speed = speed;
		this.distance = distance;
    }

    /**
     * Set the initial yaw value and starting encoder position
     */
    protected void initialize() {
    	initialYaw = driveTrain.getPigeonYaw();
    	start = Robot.driveTrain.getEncoderPosition(ENCODER.AVERAGE);
    }

    /**
     * Drive with speed adjusted based on difference between starting and current angle
     */
    protected void execute() {
    	currentYaw = driveTrain.getPigeonYaw();
    	double differenceYaw = initialYaw - currentYaw;
    	if (differenceYaw > 0) {
    		driveTrain.tankDrive(speed - (differenceYaw / CORRECTION), speed + (differenceYaw / CORRECTION));
    	} else {
    		driveTrain.tankDrive(speed + (differenceYaw / CORRECTION), speed - (differenceYaw / CORRECTION));
    	}
    }

    /**
     * Terminate if average encoder position surpasses target distance
     */
    protected boolean isFinished() {
        return Robot.driveTrain.getEncoderPosition(ENCODER.AVERAGE) > start + distance;
    }

    /**
     * Important - we don't want uncontrolled motors
     */
    protected void end() {
    	driveTrain.tankDrive(0, 0);
    }

    /**
     * Sanity check
     */
    protected void interrupted() {
    	driveTrain.tankDrive(0, 0);
    }
}
