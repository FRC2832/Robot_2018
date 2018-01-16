package org.usfirst.frc.team2832.robot.commands.auton;

import org.usfirst.frc.team2832.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveDistance extends Command {

	private int start;

	public DriveDistance(DriveTrain driveTrain) {
        requires(driveTrain);
    }

    protected void initialize() {
    	
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
