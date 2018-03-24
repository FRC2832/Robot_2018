package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 *
 */
public class DrivePastLine extends TimedCommand {

	private final static double CORRECTION = 20;
	private double differenceYaw, initialYaw, currentYaw, speed;
	private boolean usingPigeon;
	
    public DrivePastLine() {
        super(7);
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	initialYaw = Robot.driveTrain.getPigeonYaw();
    	usingPigeon = !Robot.driveTrain.hasFlag(DriveTrain.DriveTrainFlags.PIGEON);
    	speed = 0.6;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(usingPigeon) {
			currentYaw = Robot.driveTrain.getPigeonYaw();
			//This is a bit backwards
			differenceYaw = initialYaw - currentYaw;
			if (differenceYaw != 0) {
				//Hence why this is backwards
				Robot.driveTrain.tankDrive(speed + (differenceYaw / CORRECTION), speed - (differenceYaw / CORRECTION));
			}
		} else {
			Robot.driveTrain.tankDrive(speed, speed);
		}
    }

    // Called once after timeout
    protected void end() {
    	Robot.driveTrain.tankDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.driveTrain.tankDrive(0, 0);
    }
}
