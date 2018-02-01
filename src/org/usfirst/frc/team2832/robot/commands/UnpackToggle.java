package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class UnpackToggle extends Command {

	boolean startState;
	
	@Override
	public void initialize() {
		requires(Robot.unpacker);
		startState = Robot.unpacker.isExtended();
		Robot.unpacker.toggleExtend();
	}
	
	@Override
	public boolean isFinished() {
		return Robot.unpacker.isExtended() != startState;
	}

}
