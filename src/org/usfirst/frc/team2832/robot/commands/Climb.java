package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.ButtonMapping;
import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {

	private double start;
	final static double DURATION = 15; //need to figure out the correct value for this
	
	public Climb() {
		requires(Robot.lift);
	}
	
	protected void initialize() {
		//pack is extending the pistons
		Robot.lift.pack();
		this.start = Timer.getFPGATimestamp();

	}
	
	protected void execute() {
		//Potentially check motor currrent in the case that limit switch fails
		if(Timer.getFPGATimestamp() < start + DURATION) {// && !Robot.lift.getLiftLimitSwitch()) {
			Robot.lift.setWinchPower(3);
		} else {
			Robot.lift.setWinchPower(0);
			Robot.lift.setWinchBrakeMode(true);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {
		Robot.lift.setWinchPower(0);
		Robot.lift.setWinchBrakeMode(false);
		Robot.lift.unpack();
	}
	
	protected void interrupted() {
		Robot.lift.setWinchPower(0);
		Robot.lift.setWinchBrakeMode(false);
		Robot.lift.unpack();
	}

}
