package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Robot;

import org.usfirst.frc.team2832.robot.statemachine.SubsystemModule;
import org.usfirst.frc.team2832.robot.statemachine.Subsystems;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

public class Climb extends SubsystemModule {

	Lift lift;

	public Climb() {
		lift = (Lift)Robot.subsystemHandler.get(Subsystems.Lift);
		requires(Subsystems.Lift);
	}

	@Override
	public void start() {

	}

	public void initialize() {
		//Robot.lift.pack();
		Robot.logger.log("Climb", "Starting");
		lift.setWinchBrakeMode(true);
	}

	public void execute() {
		lift.setWinchPower(3);
	}
	
	@Override
	public boolean isFinished() {
		return false;
	}

	public void end() {
		Robot.logger.log("Climb", "Ended");
		lift.setWinchPower(0);
		lift.setWinchBrakeMode(false);
		//Robot.lift.unpack();
	}

	public void interrupted() {
		Robot.logger.log("Climb", "Interrupted");
		lift.setWinchPower(0);
		lift.setWinchBrakeMode(false);
		//Robot.lift.unpack();
	}
}
