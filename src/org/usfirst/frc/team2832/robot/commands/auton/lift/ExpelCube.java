package org.usfirst.frc.team2832.robot.commands.auton.lift;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Ingestor;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Expels the cube
 */
public class ExpelCube extends Command {

    private double EXPEL_DURATION = 3;
    private double EXPEL_SPEED = 1;

	private double startTime;
	private Ingestor Ingestor = Robot.ingestor;
	
	public ExpelCube(double speed, double time) {
    	EXPEL_DURATION = time;
    	EXPEL_SPEED = speed;
    	requires(Robot.ingestor);
    }
	public ExpelCube(double speed) {
    	this(speed, 3);
    }
    public ExpelCube() {
    	this(1, 3);
    	requires(Robot.ingestor);
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
        Robot.logger.log("Expell Cube", "Started");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Ingestor.setMotorSpeed(EXPEL_SPEED);
    	Robot.ingestor.unpinch();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return startTime + EXPEL_DURATION < Timer.getFPGATimestamp();
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.logger.log("Expell Cube", "Ended");
    	Ingestor.stopMotors();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.logger.log("Expell Cube", "Interrrupted");
    	Ingestor.stopMotors();
    }
}
