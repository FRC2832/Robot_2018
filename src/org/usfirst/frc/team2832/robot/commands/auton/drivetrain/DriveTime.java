package org.usfirst.frc.team2832.robot.commands.auton.drivetrain;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * A command to drive forward for a set amount of time
 */
public class DriveTime extends Command {

	private double startTime, duration, speeed;
	
    public DriveTime(double speeed, double duration) {
    	requires(Robot.driveTrain);
    	this.duration = duration;
    	this.speeed = speeed;
    }

    /**
     * Store the starting time
     */
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
    }

    /**
     * Drive strait at set speed
     */
    protected void execute() {
    	Robot.driveTrain.arcadeDrive(speeed, 0f);
    }

    /**
     * Terminate when allocated time block expires
     */
    protected boolean isFinished() {
        return startTime + duration < Timer.getFPGATimestamp();
    }

    /**
     * I have forgotten this one too many times
     */
    protected void end() {
    	Robot.driveTrain.arcadeDrive(0, 0);
    }
    
    /**
     * Never again
     */
    protected void interrupted() {
    	Robot.driveTrain.arcadeDrive(0, 0);
    }
}
