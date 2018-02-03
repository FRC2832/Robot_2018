package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Ingestor;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ExpelCube extends Command {

	private double startTime;
	private Ingestor Ingestor = Robot.ingestor;
	
    public ExpelCube() {
    	requires(Robot.ingestor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Ingestor.setMotorSpeed(1.0);;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return startTime + 3 < Timer.getFPGATimestamp();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Ingestor.stopMotors();
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Ingestor.stopMotors();
    }
}
