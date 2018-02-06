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
		Robot.lift.pack();
		this.start = Timer.getFPGATimestamp();

	}
	
	protected void execute() {
		if(Timer.getFPGATimestamp() > start + DURATION) {
			Robot.lift.setWinchPower(3);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {
		Robot.lift.setWinchPower(0);
	}
	
	protected void interrupted() {
		Robot.lift.setWinchPower(0);
	}

}
