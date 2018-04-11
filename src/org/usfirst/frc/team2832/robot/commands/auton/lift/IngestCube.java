package org.usfirst.frc.team2832.robot.commands.auton.lift;

import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IngestCube extends Command {

	private double startTime, duration;
	private final double INGEST_SPEED = -1;
	
    public IngestCube(double duration) {
    	requires(Robot.ingestor);
    	this.duration = duration;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
        Robot.logger.log("Ingest Cube", "Started");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.ingestor.setMotorSpeed(INGEST_SPEED);
    	Robot.ingestor.unpinch();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() > startTime + duration;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ingestor.pinch();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.ingestor.pinch();
    }
}
