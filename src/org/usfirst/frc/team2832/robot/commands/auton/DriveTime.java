package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveTime extends Command {

	private double startTime, duration, speeed;
	
    public DriveTime(double speeed, double duration) {
    	requires(Robot.driveTrain);
    	this.duration = duration;
    	this.speeed = speeed;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveTrain.arcadeDrive(speeed, 0f);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return startTime + duration < Timer.getFPGATimestamp();

    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.arcadeDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.driveTrain.arcadeDrive(0, 0);
    	System.out.println("AutonTest got interrupted!");
    }
}
