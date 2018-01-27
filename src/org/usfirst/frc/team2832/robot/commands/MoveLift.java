package org.usfirst.frc.team2832.robot.commands;

import org.usfirst.frc.team2832.robot.Controls.Controllers;
import org.usfirst.frc.team2832.robot.Robot;
import org.usfirst.frc.team2832.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;

public class MoveLift extends Command {

	private double currentPos;
	private double heightScale = 84d;
	private double heightSwitch = 50d;
	private double heightIntake = 0d;
	// Number of encoder counts to raise to

	public MoveLift() {
		requires(Robot.lift);
	}

	protected void execute() {
		currentPos = Robot.lift.getLiftPosition(); // Don't know yet what buttons to use here, setting to D-pad
													// temporarily
		if (Robot.controls.getPOV(Controllers.CONTROLLER_MAIN) != -1) {
			if (Robot.controls.getPOV(Controllers.CONTROLLER_MAIN) == 180) {
				if (currentPos < heightScale) {
					Robot.lift.setLiftPower(0.1);

				} else if (currentPos > heightScale) {
					Robot.lift.setLiftPower(-0.1);

				}
			}
			if (Robot.controls.getPOV(Controllers.CONTROLLER_MAIN) == 270) { // D-pad left
				if (currentPos < heightSwitch) {
					Robot.lift.setLiftPower(0.2);

				} else if (currentPos > heightSwitch) {
					Robot.lift.setLiftPower(-0.2);

				}

			}
			if (Robot.controls.getPOV(Controllers.CONTROLLER_MAIN) == 0) { // D-Pad up
				if (currentPos > heightIntake) {
					Robot.lift.setLiftPower(0.1);

				}
			}
		} else {
			Robot.lift.setLiftPower(0.0);
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.lift.setLiftPower(0);
	}

	protected void interrupted() {
		Robot.lift.setLiftPower(0);
	}
}