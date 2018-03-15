package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {

	public Climb() {
		requires(Robot.lift);
	}
	
	protected void initialize() {
		//Robot.lift.pack();
		Robot.logger.log("Climb", "Starting");
		Robot.lift.setWinchBrakeMode(true);
	}
	
	protected void execute() {
		Robot.lift.setWinchPower(1);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {
		Robot.logger.log("Climb", "Ended");
		Robot.lift.setWinchPower(0);
		Robot.lift.setWinchBrakeMode(false);
		//Robot.lift.unpack();
	}
	
	protected void interrupted() {
		Robot.logger.log("Climb", "Interrupted");
		Robot.lift.setWinchPower(0);
		Robot.lift.setWinchBrakeMode(false);
		//Robot.lift.unpack();
	}
}
