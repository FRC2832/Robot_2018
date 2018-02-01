package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {
	private double then;
	final static double DURATION = 300; //need to figure out the correct value for this
	
	public Climb() {
		requires(Robot.climber);
	}
	
	protected void initialize() {
		then = Timer.getFPGATimestamp();
	}
	
	protected void execute() {
		if(Robot.controls.getButtonPressed(Robot.climber.lowerButton.getController(), Robot.climber.lowerButton.getButton())) {
			Robot.climber.extendPiston();
			Robot.climber.setWinchMotorSpeed(-.3); //need to make this stop after a bit
		} else if(Robot.controls.getButtonPressed(Robot.climber.raiseButton.getController(), Robot.climber.raiseButton.getButton())) {
			Robot.climber.extendPiston();
			Robot.climber.setWinchMotorSpeed(-.3); //need to make this stop after a bit
		}
	}
	
	@Override
	protected boolean isFinished() {
		return then + DURATION < Timer.getFPGATimestamp();
	}
	
	protected void end() {
		Robot.climber.setWinchMotorSpeed(0);
		
	}
	
	protected void interrupted() {
		Robot.climber.setWinchMotorSpeed(0);
	}

}
