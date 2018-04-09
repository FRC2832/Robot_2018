package org.usfirst.frc.team2832.robot.commands.auton.lift;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.statemachine.SubsystemModule;
import org.usfirst.frc.team2832.robot.subsystems.Ingestor;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Expels the cube
 */
public class ExpelCube extends SubsystemModule {

    private final double EXPEL_DURATION = 3;
    private final double EXPEL_SPEED = 1;

	private double startTime;
	private Ingestor Ingestor = Robot.ingestor;
	
    public ExpelCube() {
    	//requires(Robot.ingestor);
    }

    @Override
    public void start() {

    }

    // Called just before this Command runs the first time
    public void initialize() {
    	startTime = Timer.getFPGATimestamp();
        Robot.logger.log("Expell Cube", "Started");
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	Ingestor.setMotorSpeed(EXPEL_SPEED);
    	Robot.ingestor.unpinch();
    }

    // Make this return true when this Command no longer needs to run execute()
    public boolean isFinished() {
    	return startTime + EXPEL_DURATION < Timer.getFPGATimestamp();
    }

    // Called once after isFinished returns true
    public void end() {
        Robot.logger.log("Expell Cube", "Ended");
    	Ingestor.stopMotors();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    public void interrupted() {
        Robot.logger.log("Expell Cube", "Interrrupted");
    	Ingestor.stopMotors();
    }
}
