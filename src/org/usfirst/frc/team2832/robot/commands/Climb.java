package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {

	private double start;
	final static double DURATION = 15; // TODO: 2/9/2018 Need to figure out the correct value for this
	
	public Climb() {
		requires(Robot.lift);
	}
	
	protected void initialize() {
		Robot.lift.pack();
		this.start = Timer.getFPGATimestamp();
		Robot.logger.log("Climb", "Starting");
		Robot.lift.setWinchBrakeMode(true);
	}
	
	protected void execute() {
		// TODO: 2/9/2018 Potentially check motor current in the case that limit switch fails
		if(Timer.getFPGATimestamp() < start + DURATION) {// && !Robot.lift.getLiftLimitSwitch()) {
			Robot.lift.setWinchPower(3);
		} else {
			Robot.lift.setWinchPower(0);
			Robot.logger.log("Climb", "At top");
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {
		Robot.logger.log("Climb", "Ended");
		Robot.lift.setWinchPower(0);
		Robot.lift.setWinchBrakeMode(false);
		Robot.lift.unpack();
	}
	
	protected void interrupted() {
		Robot.logger.log("Climb", "Interrupted");
		Robot.lift.setWinchPower(0);
		Robot.lift.setWinchBrakeMode(false);
		Robot.lift.unpack();
	}
}
