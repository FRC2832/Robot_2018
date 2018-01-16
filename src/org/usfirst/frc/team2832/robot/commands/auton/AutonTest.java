package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonTest extends Command {

	private DriveTrain driveTrain;
	private long startTime;
	
    public AutonTest(DriveTrain driveTrain) {
    	requires(driveTrain);
    	this.driveTrain = driveTrain;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	driveTrain.getDrive().arcadeDrive(0.2f, 0f);
    	System.out.println("HELLO!!!");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return startTime + 3000f < System.currentTimeMillis();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
